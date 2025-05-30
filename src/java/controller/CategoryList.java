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
import java.util.Objects;
import model.Category;
import model.Material;

public class CategoryList extends HttpServlet {

    private static final int DEFAULT_PAGE_SIZE = 2; // Tăng số lượng hiển thị hợp lý

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String status = request.getParameter("status");

        CategoryDAO dao = new CategoryDAO();
        MaterialDAO mDao = new MaterialDAO();

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

            // Đảm bảo page không vượt quá giới hạn
            if (page > totalPages && totalPages > 0) {
                page = totalPages;
            }

            int start = (page - 1) * pageSize;
            int end = Math.min(page * pageSize, totalSize);

            List<Category> pagedCategories = dao.getListByPage(
                    (ArrayList<Category>) filterCategories, start, end);

            // Truyền dữ liệu sang JSP
            setRequestAttributes(request, pagedCategories, search, status, page,
                    totalPages, pageSize, totalSize, parentCategories, allCategories);

            request.getRequestDispatcher("categorylist.jsp").forward(request, response);

        } catch (Exception e) {
            // Log lỗi và hiển thị trang lỗi
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading categories.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
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
                // Action không hợp lệ, redirect về danh sách
                response.sendRedirect("categorylist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
            doGet(request, response);
        }
    }

    private void handleAddCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryName = request.getParameter("categoryName");
        String parentIdStr = request.getParameter("parentId");

        // Validate dữ liệu đầu vào
        String errorMessage = validateCategoryInput(categoryName, parentIdStr);

        if (errorMessage != null) {
            // Hiển thị lỗi và modal
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("showAddModal", true);
            request.setAttribute("categoryName", categoryName);
            request.setAttribute("parentId", parentIdStr);
            doGet(request, response);
            return;
        }

        // Parse parent ID
        Integer parentId = parseParentId(parentIdStr);

        // Thêm category mới
        CategoryDAO dao = new CategoryDAO();
        boolean success = dao.insertCategory(categoryName.trim(), parentId);

        if (success) {
            // Redirect với thông báo thành công
            response.sendRedirect("categorylist?message=Category added successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to add category. Please try again.");
            request.setAttribute("showAddModal", true);
            doGet(request, response);
        }
    }

    private void handleUpdateStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryIdStr = request.getParameter("categoryId");
        String statusParam = request.getParameter("statusParam");

        if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Invalid category ID.");
            doGet(request, response);
            return;
        }

        try {
            int categoryId = Integer.parseInt(categoryIdStr);

            CategoryDAO dao = new CategoryDAO();
            MaterialDAO mDao = new MaterialDAO();
            Category category = dao.getCategoryById(categoryId);

            if (category == null) {
                request.setAttribute("errorMessage", "Category not found.");
                doGet(request, response);
                return;
            }

            String currentStatus = category.getStatus();
            String newStatus = (statusParam != null && statusParam.equals("on")) ? "active" : "inactive";

            // Chỉ xử lý khi có thay đổi trạng thái
            if (!currentStatus.equals(newStatus)) {

                // CHỈ HIỆN MODAL XÁC NHẬN KHI CHUYỂN TỪ ACTIVE SANG INACTIVE
                if ("active".equals(currentStatus) && "inactive".equals(newStatus)) {
                    String validationError = validateStatusChange(category, newStatus, mDao);

                    if (validationError != null) {
                        request.setAttribute("errorMessage", validationError);
                        doGet(request, response);
                        return;
                    }

                    // Hiển thị modal xác nhận cho việc chuyển từ active -> inactive
                    request.setAttribute("showConfirmModal", true);
                    request.setAttribute("confirmCategoryId", categoryId);
                    request.setAttribute("confirmNewStatus", newStatus);
                    request.setAttribute("confirmCategory", category);

                    // Tính toán số liệu để hiển thị
                    setConfirmModalData(request, category, mDao, dao);
                    doGet(request, response);
                    return;
                } else {
                    // TRƯỜNG HỢP CHUYỂN TỪ INACTIVE -> ACTIVE: CẬP NHẬT TRỰC TIẾP
                    // Vẫn cần validate để đảm bảo không có lỗi
                    String validationError = validateStatusChange(category, newStatus, mDao);

                    if (validationError != null) {
                        request.setAttribute("errorMessage", validationError);
                        doGet(request, response);
                        return;
                    }

                    // Cập nhật trực tiếp mà không cần xác nhận
                    updateCategoryAndRelatedEntities(category, newStatus, dao, mDao);
                }
            }

            // Redirect để tránh resubmit
            String redirectUrl = buildRedirectUrl(request);
            response.sendRedirect(redirectUrl);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid category ID format.");
            doGet(request, response);
        }
    }

    private void setConfirmModalData(HttpServletRequest request, Category category,
            MaterialDAO mDao, CategoryDAO dao) {
        int countMaterials = 0;
        int countSubCategories = 0;

        if (category.getParentId() == null) {
            // Parent category
            Category parentCate = dao.getParentCategoryById(category.getCategoryId());
            if (parentCate != null && parentCate.getSubCategories() != null) {
                countSubCategories = parentCate.getSubCategoryCount();
                for (Category subCate : parentCate.getSubCategories()) {
                    countMaterials += mDao.countMaterialByCategoryId(subCate.getCategoryId());
                }
            }
        } else {
            // Sub category
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

        // Validate parent ID nếu được cung cấp
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

        // Thu thập tất cả materials liên quan
        if (category.getParentId() == null) {
            // Parent category - lấy materials từ tất cả subcategories
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
            // Subcategory - lấy materials trực tiếp
            allMaterials = mDao.getAllMaterialsByCategoryId(category.getCategoryId());
        }

        // Kiểm tra constraints với materials
        for (Material material : allMaterials) {
            if (mDao.isMaterialInOrderWithStatus(material.getMaterialId(), "pending")) {
                return "Cannot change category status because it contains materials that are in pending orders.";
            }

            if (mDao.isMaterialInOrderWithStatus(material.getMaterialId(), "approved")) {
                if (mDao.isMaterialInPendingImport(material.getMaterialId())) {
                    return "Cannot change category status because it contains materials that are in pending import.";
                }

                if (mDao.isMaterialInPendingExport(material.getMaterialId())) {
                    return "Cannot change category status because it contains materials that are in pending export.";
                }
            }
        }

        return null; // Không có lỗi
    }

    private void updateCategoryAndRelatedEntities(Category category, String newStatus,
            CategoryDAO dao, MaterialDAO mDao) {

        if ("inactive".equals(newStatus)) {
            List<Material> allMaterials = new ArrayList<>();

            if (category.getParentId() == null) {
                // Parent category - cập nhật tất cả subcategories và materials
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
                // Subcategory - chỉ cập nhật materials của subcategory này
                allMaterials = mDao.getAllMaterialsByCategoryId(category.getCategoryId());
            }

            // Cập nhật trạng thái tất cả materials
            for (Material material : allMaterials) {
                material.setStatus(newStatus);
                mDao.updateMaterial(material);
            }
        }

        // Cập nhật trạng thái category
        category.setStatus(newStatus);
        dao.updateCategory(category);
    }

    private int getPageNumber(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        if (pageParam == null || pageParam.trim().isEmpty()) {
            return 1;
        }

        try {
            int page = Integer.parseInt(pageParam);
            return Math.max(1, page); // Đảm bảo page >= 1
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

    private String buildRedirectUrl(HttpServletRequest request) {
        StringBuilder redirectUrl = new StringBuilder("categorylist");
        List<String> params = new ArrayList<>();

        String currentPage = request.getParameter("page");
        String search = request.getParameter("search");
        String statusFilter = request.getParameter("statusFilter");

        if (currentPage != null && !currentPage.trim().isEmpty()) {
            params.add("page=" + currentPage);
        }
        if (search != null && !search.trim().isEmpty()) {
            params.add("search=" + search);
        }
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            params.add("status=" + statusFilter);
        }

        if (!params.isEmpty()) {
            redirectUrl.append("?").append(String.join("&", params));
        }

        return redirectUrl.toString();
    }

    private void handleConfirmUpdateStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryIdStr = request.getParameter("categoryId");
        String newStatus = request.getParameter("newStatus");

        try {
            int categoryId = Integer.parseInt(categoryIdStr);

            CategoryDAO dao = new CategoryDAO();
            MaterialDAO mDao = new MaterialDAO();
            Category category = dao.getCategoryById(categoryId);

            if (category != null) {
                updateCategoryAndRelatedEntities(category, newStatus, dao, mDao);
            }

            String redirectUrl = buildRedirectUrl(request);
            response.sendRedirect(redirectUrl);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid category ID format.");
            doGet(request, response);
        }
    }

    private void handleUpdateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryIdStr = request.getParameter("categoryId");
        String categoryName = request.getParameter("categoryName");
        String parentIdStr = request.getParameter("parentId");
        String categoryStatus = request.getParameter("categoryStatus");

        // Validate dữ liệu đầu vào
        String errorMessage = validateUpdateCategoryInput(categoryIdStr, categoryName, parentIdStr, categoryStatus);

        if (errorMessage != null) {
            // Hiển thị lỗi và modal
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("showUpdateModal", true);
            request.setAttribute("updateCategoryId", categoryIdStr);
            request.setAttribute("categoryName", categoryName);
            request.setAttribute("parentId", parentIdStr);
            request.setAttribute("categoryStatus", categoryStatus);
            doGet(request, response);
            return;
        }

        try {
            int categoryId = Integer.parseInt(categoryIdStr);
            CategoryDAO dao = new CategoryDAO();
            Category currentCategory = dao.getCategoryById(categoryId);

            if (currentCategory == null) {
                request.setAttribute("errorMessage", "Category not found.");
                doGet(request, response);
                return;
            }

            // Parse parent ID
            Integer parentId = parseParentId(parentIdStr);

            // Validate circular reference
            if (parentId != null && dao.isCircularParent(categoryId, parentId)) {
                request.setAttribute("errorMessage", "Cannot update: circular hierarchy detected!");
                request.setAttribute("showUpdateModal", true);
                request.setAttribute("updateCategoryId", categoryIdStr);
                request.setAttribute("categoryName", categoryName);
                request.setAttribute("parentId", parentIdStr);
                request.setAttribute("categoryStatus", categoryStatus);
                doGet(request, response);
                return;
            }

            Integer currentParentId = currentCategory.getParentId() != null ? currentCategory.getParentId().getCategoryId() : null;
            boolean hasParentChange = !Objects.equals(currentParentId, parentId);

            // CHỈ VALIDATE KHI CÓ THAY ĐỔI PARENT
            if (hasParentChange && parentId != null) {
                // Validate max 2 levels (only parent and child)
                Category parentCategory = dao.getCategoryById(parentId);
                if (parentCategory != null && parentCategory.getParentId() != null) {
                    request.setAttribute("errorMessage", "Cannot update: exceeds 2-level hierarchy (only parent and child allowed).");
                    request.setAttribute("showUpdateModal", true);
                    request.setAttribute("updateCategoryId", categoryIdStr);
                    request.setAttribute("categoryName", categoryName);
                    request.setAttribute("parentId", parentIdStr);
                    request.setAttribute("categoryStatus", categoryStatus);
                    doGet(request, response);
                    return;
                }

                // Kiểm tra nếu category hiện tại có children thì không được phép chuyển thành child của category khác
                List<Category> childrenOfCurrentCategory = dao.getSubCategoryByParentId(categoryId);
                if (childrenOfCurrentCategory != null && !childrenOfCurrentCategory.isEmpty()) {
                    request.setAttribute("errorMessage", "Cannot update: this category has child categories. A parent category cannot become a child category.");
                    request.setAttribute("showUpdateModal", true);
                    request.setAttribute("updateCategoryId", categoryIdStr);
                    request.setAttribute("categoryName", categoryName);
                    request.setAttribute("parentId", parentIdStr);
                    request.setAttribute("categoryStatus", categoryStatus);
                    doGet(request, response);
                    return;
                }
            }

            // Validate status change if needed
            if (!currentCategory.getStatus().equals(categoryStatus)) {
                MaterialDAO mDao = new MaterialDAO();
                String validationError = validateStatusChange(currentCategory, categoryStatus, mDao);

                if (validationError != null) {
                    request.setAttribute("errorMessage", validationError);
                    request.setAttribute("showUpdateModal", true);
                    request.setAttribute("updateCategoryId", categoryIdStr);
                    request.setAttribute("categoryName", categoryName);
                    request.setAttribute("parentId", parentIdStr);
                    request.setAttribute("categoryStatus", categoryStatus);
                    doGet(request, response);
                    return;
                }
            }

            // Kiểm tra xem có thay đổi gì không
            boolean hasNameChange = !currentCategory.getName().equals(categoryName.trim());
            boolean hasStatusChange = !currentCategory.getStatus().equals(categoryStatus);
            // Sửa lại phần kiểm tra thay đổi parent
            currentParentId = currentCategory.getParentId() != null ? currentCategory.getParentId().getCategoryId() : null;
            hasParentChange = !Objects.equals(currentParentId, parentId);
            boolean hasChanges = hasNameChange || hasStatusChange || hasParentChange;

            // Nếu không có thay đổi gì
            if (!hasChanges) {
                String redirectUrl = buildRedirectUrl(request);
                response.sendRedirect(redirectUrl + (redirectUrl.contains("?") ? "&" : "?") + "message=No changes detected");
                return;
            }

            // Hiển thị modal xác nhận với thông tin chi tiết
            request.setAttribute("showUpdateConfirmModal", true);
            request.setAttribute("updateConfirmCategoryId", categoryId);
            request.setAttribute("updateConfirmName", categoryName.trim());
            request.setAttribute("updateConfirmParentId", parentId);
            request.setAttribute("updateConfirmStatus", categoryStatus);
            request.setAttribute("updateConfirmCategory", currentCategory);

            // LUÔN TÍNH TOÁN VÀ HIỂN THỊ THÔNG TIN SUB-CATEGORIES VÀ MATERIALS
            // Không chỉ khi thay đổi status sang inactive
            MaterialDAO mDao = new MaterialDAO();
            setConfirmModalData(request, currentCategory, mDao, dao);

            doGet(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid category ID format.");
            doGet(request, response);
        }
    }

    private String validateUpdateCategoryInput(String categoryIdStr, String categoryName,
            String parentIdStr, String categoryStatus) {

        // Validate category ID
        if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            return "Category ID cannot be empty!";
        }

        try {
            Integer.parseInt(categoryIdStr);
        } catch (NumberFormatException e) {
            return "Invalid category ID format.";
        }

        // Validate category name
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return "Category name cannot be empty!";
        }

        if (!categoryName.trim().matches("^[\\p{L}\\s]+$")) {
            return "Category name must only contain letters and spaces (no numbers or symbols).";
        }

        // Validate parent ID if provided
        if (parentIdStr != null && !parentIdStr.trim().equals("0") && !parentIdStr.trim().isEmpty()) {
            try {
                Integer.parseInt(parentIdStr);
            } catch (NumberFormatException e) {
                return "Invalid parent category ID.";
            }
        }

        // Validate status
        if (categoryStatus == null || (!categoryStatus.equals("active") && !categoryStatus.equals("inactive"))) {
            return "Invalid category status.";
        }

        return null;
    }

    private void handleConfirmUpdateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryIdStr = request.getParameter("categoryId");
        String categoryName = request.getParameter("categoryName");
        String parentIdStr = request.getParameter("parentId");
        String categoryStatus = request.getParameter("categoryStatus");

        try {
            int categoryId = Integer.parseInt(categoryIdStr);
            CategoryDAO dao = new CategoryDAO();
            Category currentCategory = dao.getCategoryById(categoryId);

            if (currentCategory == null) {
                request.setAttribute("errorMessage", "Category not found.");
                doGet(request, response);
                return;
            }

            // Parse parent ID
            Integer parentId = parseParentId(parentIdStr);

            // Create updated category object
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

            // Update category
            dao.updateCategory(updatedCategory);

//            if (!updateSuccess) {
//                request.setAttribute("errorMessage", "Failed to update category. Please try again.");
//                doGet(request, response);
//                return;
//            }
            // If status changed to inactive, update related entities
            if (!currentCategory.getStatus().equals(categoryStatus) && "inactive".equals(categoryStatus)) {
                MaterialDAO mDao = new MaterialDAO();
                updateCategoryAndRelatedEntities(updatedCategory, categoryStatus, dao, mDao);
            }

            // Redirect với thông báo thành công
            String redirectUrl = buildRedirectUrl(request);
            response.sendRedirect(redirectUrl + (redirectUrl.contains("?") ? "&" : "?") + "message=Category updated successfully");

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid category ID format.");
            doGet(request, response);
        }
    }
}
