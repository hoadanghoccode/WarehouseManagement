package controller;

import dal.CategoryDAO;
import dal.MaterialDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Objects;
import model.Category;
import model.Material;

public class CategoryList extends HttpServlet {

    private static final int DEFAULT_PAGE_SIZE = 4;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Xử lý error parameter từ redirect cho modal
        String error = request.getParameter("error");
        if (error != null && !error.trim().isEmpty()) {
            request.setAttribute("errorMessage", error);

            // Kiểm tra loại modal cần hiển thị
            String modalType = request.getParameter("modalType");
            if ("add".equals(modalType)) {
                request.setAttribute("showAddModal", true);
                request.setAttribute("categoryName", request.getParameter("categoryName"));
                request.setAttribute("parentId", request.getParameter("parentId"));
            } else if ("update".equals(modalType)) {
                request.setAttribute("showUpdateModal", true);
                request.setAttribute("updateCategoryId", request.getParameter("categoryId"));
                request.setAttribute("categoryName", request.getParameter("categoryName"));
                request.setAttribute("parentId", request.getParameter("parentId"));
                request.setAttribute("categoryStatus", request.getParameter("categoryStatus"));
            }
        }

        String search = request.getParameter("search");
        String status = request.getParameter("status");

        CategoryDAO dao = new CategoryDAO();
        MaterialDAO mDao = new MaterialDAO();

        HttpSession session = request.getSession();

        // CHỈ CHECK SUCCESS MESSAGE TỪ SESSION
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            request.setAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }

        // KHÔNG CHECK ERROR MESSAGE TỪ SESSION NỮA VÌ ĐÃ CÓ MODAL XỬ LÝ
        // Chỉ giữ lại nếu cần thiết cho các trường hợp đặc biệt khác
        try {
            List<Category> parentCategories = dao.getAllParentCategory(status);
            List<Category> filterCategories;
            List<Category> allCategories = dao.getIndentedCategories();

            // Tìm kiếm categories
            if (search != null && !search.trim().isEmpty()) {
                filterCategories = dao.searchCategoryByName((ArrayList<Category>) parentCategories, search.trim());
            } else {
                filterCategories = parentCategories;
            }

            // Đếm số lượng materials cho mỗi subcategory
            for (Category pcate : filterCategories) {
                if (pcate.getSubCategories() != null) {
                    for (Category subCate : pcate.getSubCategories()) {
                        subCate.setMaterialCount(mDao.countMaterialByCategoryId(subCate.getCategoryId()));
                    }
                }
            }

            // Phân trang
            int page = getPageNumber(request);
            int pageSize = DEFAULT_PAGE_SIZE;
            int totalSize = filterCategories.size();
            int totalPages = (totalSize % pageSize == 0) ? (totalSize / pageSize) : (totalSize / pageSize + 1);

            if (page > totalPages && totalPages > 0) {
                page = totalPages;
            }

            int start = (page - 1) * pageSize;
            int end = Math.min(page * pageSize, totalSize);

            List<Category> pagedCategories = dao.getListByPage(
                    (ArrayList<Category>) filterCategories, start, end);

            setRequestAttributes(request, pagedCategories, search, status, page,
                    totalPages, pageSize, totalSize, parentCategories, allCategories);

            request.getRequestDispatcher("categorylist.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading categories.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                handleAddCategory(request, response);
            } else if ("updateStatus".equals(action)) {
                handleUpdateStatus(request, response);
            } else if ("confirmUpdateStatus".equals(action)) {
                handleConfirmUpdateStatus(request, response);
            } else if ("updateModal".equals(action)) {
                handleUpdateCategory(request, response);
            } else if ("confirmUpdateCategory".equals(action)) {
                handleConfirmUpdateCategory(request, response);
            } else {
                response.sendRedirect("categorylist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "An error occurred while processing your request.");
            response.sendRedirect("categorylist");
        }
    }

    private void handleAddCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryName = request.getParameter("categoryName");
        String parentIdStr = request.getParameter("parentId");
        HttpSession session = request.getSession();

        // Validate dữ liệu đầu vào
        String errorMessage = validateCategoryInput(categoryName, parentIdStr);

        if (errorMessage != null) {
            // KHÔNG LƯU VÀO SESSION - REDIRECT VỚI PARAMETER ĐỂ HIỂN THỊ MODAL
            String redirectUrl = "categorylist?error=" + java.net.URLEncoder.encode(errorMessage, "UTF-8")
                    + "&modalType=add&categoryName=" + java.net.URLEncoder.encode(categoryName != null ? categoryName : "", "UTF-8")
                    + "&parentId=" + java.net.URLEncoder.encode(parentIdStr != null ? parentIdStr : "", "UTF-8");
            response.sendRedirect(redirectUrl);
            return;
        }

        Integer parentId = parseParentId(parentIdStr);

        CategoryDAO dao = new CategoryDAO();
        boolean success = dao.insertCategory(categoryName.trim(), parentId);

        if (success) {
            session.setAttribute("successMessage", "Category created successfully!");
        } else {
            // TRƯỜNG HỢP LỖI HỆ THỐNG - HIỂN THỊ MODAL
            String redirectUrl = "categorylist?error=" + java.net.URLEncoder.encode("Failed to create category. Please try again.", "UTF-8")
                    + "&modalType=add&categoryName=" + java.net.URLEncoder.encode(categoryName, "UTF-8")
                    + "&parentId=" + java.net.URLEncoder.encode(parentIdStr != null ? parentIdStr : "", "UTF-8");
            response.sendRedirect(redirectUrl);
            return;
        }

        response.sendRedirect("categorylist");
    }

    private void handleUpdateStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryIdStr = request.getParameter("categoryId");
        String statusParam = request.getParameter("statusParam");
        HttpSession session = request.getSession();

        if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            // LỖI HỆ THỐNG - HIỂN THỊ ALERT
            response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Invalid category ID.", "UTF-8"));
            return;
        }

        try {
            int categoryId = Integer.parseInt(categoryIdStr);

            CategoryDAO dao = new CategoryDAO();
            MaterialDAO mDao = new MaterialDAO();
            Category category = dao.getCategoryById(categoryId);

            if (category == null) {
                session.setAttribute("successMessage", "Category not found.");
                response.sendRedirect("categorylist");
                return;
            }

            String currentStatus = category.getStatus();
            String newStatus = (statusParam != null && statusParam.equals("on")) ? "active" : "inactive";

            if (!currentStatus.equals(newStatus)) {
                if ("active".equals(currentStatus) && "inactive".equals(newStatus)) {
                    String validationError = validateStatusChange(category, newStatus, mDao);

                    if (validationError != null) {
                        // VALIDATION ERROR - HIỂN THỊ MODAL
                        response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode(validationError, "UTF-8"));
                        return;
                    }

                    // Hiển thị modal xác nhận
                    request.setAttribute("showConfirmModal", true);
                    request.setAttribute("confirmCategoryId", categoryId);
                    request.setAttribute("confirmNewStatus", newStatus);
                    request.setAttribute("confirmCategory", category);

                    setConfirmModalData(request, category, mDao, dao);
                    doGet(request, response);
                    return;
                } else {
                    // CHUYỂN TỪ INACTIVE -> ACTIVE
                    String validationError = validateStatusChange(category, newStatus, mDao);

                    if (validationError != null) {
                        // VALIDATION ERROR - HIỂN THỊ MODAL
                        response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode(validationError, "UTF-8"));
                        return;
                    }

                    boolean success = updateCategoryAndRelatedEntities(category, newStatus, dao, mDao);
                    if (success) {
                        session.setAttribute("successMessage", "Status changed successfully!");
                    } else {
                        // LỖI HỆ THỐNG - HIỂN THỊ MODAL
                        response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Failed to change status. Please try again.", "UTF-8"));
                        return;
                    }
                    response.sendRedirect("categorylist");
                    return;
                }
            }

        } catch (NumberFormatException e) {
            // LỖI HỆ THỐNG - HIỂN THỊ ALERT
            response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Invalid category ID format.", "UTF-8"));
        } catch (Exception e) {
            // LỖI HỆ THỐNG - HIỂN THỊ ALERT  
            response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Failed to change status: " + e.getMessage(), "UTF-8"));
        }
    }

    private void handleConfirmUpdateStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryIdStr = request.getParameter("categoryId");
        String newStatus = request.getParameter("newStatus");
        HttpSession session = request.getSession();

        try {
            int categoryId = Integer.parseInt(categoryIdStr);

            CategoryDAO dao = new CategoryDAO();
            MaterialDAO mDao = new MaterialDAO();
            Category category = dao.getCategoryById(categoryId);

            if (category != null) {
                boolean success = updateCategoryAndRelatedEntities(category, newStatus, dao, mDao);
                if (success) {
                    session.setAttribute("successMessage", "Status changed successfully!");
                } else {
                    response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Failed to change status. Please try again.", "UTF-8"));
                    return;
                }
            } else {
                response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Category not found.", "UTF-8"));
                return;
            }

            response.sendRedirect("categorylist");

        } catch (NumberFormatException e) {
            response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Invalid category ID format.", "UTF-8"));
        } catch (Exception e) {
            response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Failed to change status: " + e.getMessage(), "UTF-8"));
        }
    }

    private void handleUpdateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryIdStr = request.getParameter("categoryId");
        String categoryName = request.getParameter("categoryName");
        String parentIdStr = request.getParameter("parentId");
        String categoryStatus = request.getParameter("categoryStatus");
        HttpSession session = request.getSession();

        // Validate dữ liệu đầu vào
        String errorMessage = validateUpdateCategoryInput(categoryIdStr, categoryName, parentIdStr, categoryStatus);

        if (errorMessage != null) {
            // VALIDATION ERROR - HIỂN THỊ MODAL
            String redirectUrl = "categorylist?error=" + java.net.URLEncoder.encode(errorMessage, "UTF-8")
                    + "&modalType=update&categoryId=" + java.net.URLEncoder.encode(categoryIdStr != null ? categoryIdStr : "", "UTF-8")
                    + "&categoryName=" + java.net.URLEncoder.encode(categoryName != null ? categoryName : "", "UTF-8")
                    + "&parentId=" + java.net.URLEncoder.encode(parentIdStr != null ? parentIdStr : "", "UTF-8")
                    + "&categoryStatus=" + java.net.URLEncoder.encode(categoryStatus != null ? categoryStatus : "", "UTF-8");
            response.sendRedirect(redirectUrl);
            return;
        }

        try {
            int categoryId = Integer.parseInt(categoryIdStr);
            CategoryDAO dao = new CategoryDAO();
            MaterialDAO mDao = new MaterialDAO();
            Category currentCategory = dao.getCategoryById(categoryId);

            if (currentCategory == null) {
                response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Category not found.", "UTF-8"));
                return;
            }

            Integer parentId = parseParentId(parentIdStr);

            // Kiểm tra có thay đổi không
            boolean hasNameChange = !currentCategory.getName().equals(categoryName.trim());
            boolean hasStatusChange = !currentCategory.getStatus().equals(categoryStatus);
            Integer currentParentId = currentCategory.getParentId() != null ? currentCategory.getParentId().getCategoryId() : null;
            boolean hasParentChange = !Objects.equals(currentParentId, parentId);
            boolean hasChanges = hasNameChange || hasStatusChange || hasParentChange;

            if (!hasChanges) {
                session.setAttribute("successMessage", "No changes detected.");
                response.sendRedirect("categorylist");
                return;
            }

            // Các validation khác...
            String orderValidationError = validateCategoryForOrderConstraints(currentCategory, mDao);
            if (orderValidationError != null) {
                response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode(orderValidationError, "UTF-8"));
                return;
            }

            if (hasParentChange && parentId != null && dao.isCircularParent(categoryId, parentId)) {
                String redirectUrl = "categorylist?error=" + java.net.URLEncoder.encode("Cannot update: circular hierarchy detected!", "UTF-8")
                        + "&modalType=update&categoryId=" + categoryIdStr
                        + "&categoryName=" + java.net.URLEncoder.encode(categoryName, "UTF-8")
                        + "&parentId=" + java.net.URLEncoder.encode(parentIdStr, "UTF-8")
                        + "&categoryStatus=" + java.net.URLEncoder.encode(categoryStatus, "UTF-8");
                response.sendRedirect(redirectUrl);
                return;
            }

            if (hasParentChange && parentId != null) {
                Category parentCategory = dao.getCategoryById(parentId);
                if (parentCategory != null && parentCategory.getParentId() != null) {
                    String redirectUrl = "categorylist?error=" + java.net.URLEncoder.encode("Cannot update: exceeds 2-level hierarchy (only parent and child allowed).", "UTF-8")
                            + "&modalType=update&categoryId=" + categoryIdStr
                            + "&categoryName=" + java.net.URLEncoder.encode(categoryName, "UTF-8")
                            + "&parentId=" + java.net.URLEncoder.encode(parentIdStr, "UTF-8")
                            + "&categoryStatus=" + java.net.URLEncoder.encode(categoryStatus, "UTF-8");
                    response.sendRedirect(redirectUrl);
                    return;
                }

                List<Category> childrenOfCurrentCategory = dao.getSubCategoryByParentId(categoryId);
                if (childrenOfCurrentCategory != null && !childrenOfCurrentCategory.isEmpty()) {
                    String redirectUrl = "categorylist?error=" + java.net.URLEncoder.encode("Cannot update: this category has child categories. A parent category cannot become a child category.", "UTF-8")
                            + "&modalType=update&categoryId=" + categoryIdStr
                            + "&categoryName=" + java.net.URLEncoder.encode(categoryName, "UTF-8")
                            + "&parentId=" + java.net.URLEncoder.encode(parentIdStr, "UTF-8")
                            + "&categoryStatus=" + java.net.URLEncoder.encode(categoryStatus, "UTF-8");
                    response.sendRedirect(redirectUrl);
                    return;
                }
            }

            if (hasStatusChange) {
                String validationError = validateStatusChange(currentCategory, categoryStatus, mDao);
                if (validationError != null) {
                    response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode(validationError, "UTF-8"));
                    return;
                }
            }

            // Hiển thị modal xác nhận
            request.setAttribute("showUpdateConfirmModal", true);
            request.setAttribute("updateConfirmCategoryId", categoryId);
            request.setAttribute("updateConfirmName", categoryName.trim());
            request.setAttribute("updateConfirmParentId", parentId);
            request.setAttribute("updateConfirmStatus", categoryStatus);
            request.setAttribute("updateConfirmCategory", currentCategory);

            setConfirmModalData(request, currentCategory, mDao, dao);
            doGet(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Invalid category ID format.", "UTF-8"));
        } catch (Exception e) {
            response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("An error occurred while updating category: " + e.getMessage(), "UTF-8"));
        }
    }

    private void handleConfirmUpdateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryIdStr = request.getParameter("categoryId");
        String categoryName = request.getParameter("categoryName");
        String parentIdStr = request.getParameter("parentId");
        String categoryStatus = request.getParameter("categoryStatus");
        HttpSession session = request.getSession();

        try {
            int categoryId = Integer.parseInt(categoryIdStr);
            CategoryDAO dao = new CategoryDAO();
            Category currentCategory = dao.getCategoryById(categoryId);

            if (currentCategory == null) {
                response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Category not found.", "UTF-8"));
                return;
            }
            Integer parentId = parseParentId(parentIdStr);

            Category updatedCategory = new Category();
            updatedCategory.setCategoryId(categoryId);
            updatedCategory.setName(categoryName.trim());
            updatedCategory.setStatus(categoryStatus);

            if (parentId == null) {
                updatedCategory.setParentId(null);
            } else {
                Category parentCategory = dao.getCategoryById(parentId);
                updatedCategory.setParentId(parentCategory);
            }

            boolean success = dao.updateCategory(updatedCategory);

            if (!success) {
                response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Failed to update category. Please try again.", "UTF-8"));
                return;
            }

            if (!currentCategory.getStatus().equals(categoryStatus) && "inactive".equals(categoryStatus)) {
                MaterialDAO mDao = new MaterialDAO();
                updateCategoryAndRelatedEntities(updatedCategory, categoryStatus, dao, mDao);
            }

            session.setAttribute("successMessage", "Category updated successfully!");
            response.sendRedirect("categorylist");

        } catch (NumberFormatException e) {
            response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Invalid category ID format.", "UTF-8"));
        } catch (Exception e) {
            response.sendRedirect("categorylist?error=" + java.net.URLEncoder.encode("Failed to update category: " + e.getMessage(), "UTF-8"));
        }
    }

    // Các method helper khác giữ nguyên...
    private void setConfirmModalData(HttpServletRequest request, Category category,
            MaterialDAO mDao, CategoryDAO dao) {
        int countMaterials = 0;
        int countSubCategories = 0;

        if (category.getParentId() == null) {
            Category parentCate = dao.getParentCategoryById(category.getCategoryId());
            if (parentCate != null && parentCate.getSubCategories() != null) {
                countSubCategories = parentCate.getSubCategoryCount();
                for (Category subCate : parentCate.getSubCategories()) {
                    countMaterials += mDao.countMaterialByCategoryId(subCate.getCategoryId());
                }
            }
        } else {
            countMaterials = mDao.countMaterialByCategoryId(category.getCategoryId());
        }

        request.setAttribute("countMaterial", countMaterials);
        request.setAttribute("countSub", countSubCategories);
    }

    private String validateCategoryInput(String categoryName, String parentIdStr) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return "Category name cannot be empty!";
        }

        if (!categoryName.trim().matches("^[\\p{L}\\s]+$")) {
            return "Category name must only contain letters and spaces (no numbers or symbols).";
        }

        if (parentIdStr != null && !parentIdStr.trim().equals("0") && !parentIdStr.trim().isEmpty()) {
            try {
                Integer.parseInt(parentIdStr);
            } catch (NumberFormatException e) {
                return "Invalid parent category ID.";
            }
        }

        return null;
    }

    private Integer parseParentId(String parentIdStr) {
        if (parentIdStr != null && !parentIdStr.trim().equals("0") && !parentIdStr.trim().isEmpty()) {
            try {
                return Integer.parseInt(parentIdStr);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String validateStatusChange(Category category, String newStatus, MaterialDAO mDao) {
        List<Material> allMaterials = new ArrayList<>();

        if (category.getParentId() == null) {
            CategoryDAO dao = new CategoryDAO();
            Category parent = dao.getParentCategoryById(category.getCategoryId());

            if (parent != null && parent.getSubCategories() != null) {
                for (Category subCategory : parent.getSubCategories()) {
                    List<Material> subMaterials = mDao.getAllMaterialsByCategoryId(subCategory.getCategoryId());
                    if (subMaterials != null && !subMaterials.isEmpty()) {
                        allMaterials.addAll(subMaterials);
                    }
                }
            }
        } else {
            allMaterials = mDao.getAllMaterialsByCategoryId(category.getCategoryId());
        }

        for (Material material : allMaterials) {
            if (mDao.isMaterialInOrderWithStatus(material.getMaterialId(), "pending")) {
                return "Cannot change category status because it contains materials that are in pending orders.";
            }

            if (mDao.isMaterialInOrderWithStatus(material.getMaterialId(), "approved")) {
                if (mDao.isMaterialInPendingImportOrExport(material.getMaterialId())) {
                    return "Cannot change category status because it contains materials that are in pending import or export.";
                }
            }
        }

        return null;
    }

    private boolean updateCategoryAndRelatedEntities(Category category, String newStatus,
            CategoryDAO dao, MaterialDAO mDao) {

        if ("inactive".equals(newStatus)) {
            List<Material> allMaterials = new ArrayList<>();

            if (category.getParentId() == null) {
                List<Category> subCategories = dao.getSubCategoryByParentId(category.getCategoryId());

                for (Category subCategory : subCategories) {
                    List<Material> subMaterials = mDao.getAllMaterialsByCategoryId(subCategory.getCategoryId());
                    if (subMaterials != null) {
                        allMaterials.addAll(subMaterials);
                    }

                    subCategory.setStatus(newStatus);
                    dao.updateCategory(subCategory);
                }
            } else {
                allMaterials = mDao.getAllMaterialsByCategoryId(category.getCategoryId());
            }

            for (Material material : allMaterials) {
                material.setStatus(newStatus);
                mDao.updateMaterial(material);
            }
        }

        category.setStatus(newStatus);
        return dao.updateCategory(category);
    }

    private String validateCategoryForOrderConstraints(Category category, MaterialDAO mDao) {
        List<Material> allMaterials = new ArrayList<>();

        if (category.getParentId() == null) {
            CategoryDAO dao = new CategoryDAO();
            Category parent = dao.getParentCategoryById(category.getCategoryId());

            if (parent != null && parent.getSubCategories() != null) {
                for (Category subCategory : parent.getSubCategories()) {
                    List<Material> subMaterials = mDao.getAllMaterialsByCategoryId(subCategory.getCategoryId());
                    if (subMaterials != null && !subMaterials.isEmpty()) {
                        allMaterials.addAll(subMaterials);
                    }
                }
            }
        } else {
            allMaterials = mDao.getAllMaterialsByCategoryId(category.getCategoryId());
        }

        for (Material material : allMaterials) {
            if (mDao.isMaterialInOrderWithStatus(material.getMaterialId(), "pending")) {
                return "Cannot update category because it contains materials that are in pending orders.";
            }

            if (mDao.isMaterialInOrderWithStatus(material.getMaterialId(), "approved")) {
                if (mDao.isMaterialInPendingImportOrExport(material.getMaterialId())) {
                    return "Cannot update category because it contains materials that are in pending import or export.";
                }
            }
        }

        return null;
    }

    private String validateUpdateCategoryInput(String categoryIdStr, String categoryName,
            String parentIdStr, String categoryStatus) {

        if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            return "Category ID cannot be empty!";
        }

        try {
            Integer.parseInt(categoryIdStr);
        } catch (NumberFormatException e) {
            return "Invalid category ID format.";
        }

        if (categoryName == null || categoryName.trim().isEmpty()) {
            return "Category name cannot be empty!";
        }

        if (!categoryName.trim().matches("^[\\p{L}\\s]+$")) {
            return "Category name must only contain letters and spaces (no numbers or symbols).";
        }

        if (parentIdStr != null && !parentIdStr.trim().equals("0") && !parentIdStr.trim().isEmpty()) {
            try {
                Integer.parseInt(parentIdStr);
            } catch (NumberFormatException e) {
                return "Invalid parent category ID.";
            }
        }

        if (categoryStatus == null || (!categoryStatus.equals("active") && !categoryStatus.equals("inactive"))) {
            return "Invalid category status.";
        }

        return null;
    }

    private int getPageNumber(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        if (pageParam == null || pageParam.trim().isEmpty()) {
            return 1;
        }

        try {
            int page = Integer.parseInt(pageParam);
            return Math.max(1, page);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private void setRequestAttributes(HttpServletRequest request, List<Category> pagedCategories,
            String search, String status, int page, int totalPages,
            int pageSize, int totalSize, List<Category> parentCategories,
            List<Category> allCategories) {

        request.setAttribute("categoryList", pagedCategories);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("page", page);
        request.setAttribute("num", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalCategories", totalSize);
        request.setAttribute("parentCategories", parentCategories);
        request.setAttribute("allCategories", allCategories);
    }
}
