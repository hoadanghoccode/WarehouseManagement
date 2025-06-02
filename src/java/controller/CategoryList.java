package controller;

import dal.CategoryDAO;
import dal.MaterialDAO;
//import dal.SubCategoryDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
//import model.SubCategory;

public class CategoryList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String status = request.getParameter("status");

        CategoryDAO dao = new CategoryDAO();
        MaterialDAO mDao = new MaterialDAO();

        List<Category> parentCategories = dao.getAllParentCategory(status);
        List<Category> filterCategories;

        // Nếu có tìm kiếm thì lọc
        if (search != null && !search.trim().isEmpty()) {
            filterCategories = dao.searchCategoryByName((ArrayList<Category>) parentCategories, search.trim());
        } else {
            filterCategories = parentCategories;
        }
        for (Category pcate : filterCategories) {
            for (Category subCate : pcate.getSubCategories()) {
//                subCate.setMaterialCount(mDao.countMaterialByCategoryId(subCate.getCategoryId()));
            }
        }

        // Phân trang
        int page, numperpage = 1;
        int size = filterCategories.size();
        int num = (size % numperpage == 0) ? (size / numperpage) : (size / numperpage + 1);

        String xpage = request.getParameter("page");
        if (xpage == null) {
            page = 1;
        } else {
            page = Integer.parseInt(xpage);
        }

        int start = (page - 1) * numperpage;
        int end = Math.min(page * numperpage, size);

        List<Category> pagedParentCategories = dao.getListByPage(
                (ArrayList<Category>) filterCategories, start, end);

        // Truyền dữ liệu sang JSP
        request.setAttribute("categoryList", pagedParentCategories);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("page", page);
        request.setAttribute("num", num);
        request.setAttribute("pageSize", numperpage);
        request.setAttribute("totalCategories", size);
        request.setAttribute("parentCategories", parentCategories);

        request.getRequestDispatcher("categorylist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String categoryName = request.getParameter("categoryName");
            String parentIdStr = request.getParameter("parentId");

            String errorMessage = null;

            // Validate tên category
            if (categoryName == null || categoryName.trim().isEmpty()) {
                errorMessage = "Category name cannot be empty!";
            } else if (!categoryName.trim().matches("^[\\p{L}\\s]+$")) {
                errorMessage = "Category name must only contain letters and spaces (no numbers or symbols).";
            }

            // Parse parent ID
            Integer parentId = null;
            if (parentIdStr != null && !parentIdStr.trim().equals("0") && !parentIdStr.trim().isEmpty()) {
                try {
                    parentId = Integer.parseInt(parentIdStr);
                } catch (NumberFormatException e) {
                    errorMessage = "Invalid parent category ID.";
                }
            }

            if (errorMessage != null) {
                // Trả lại lỗi và hiển thị modal
                request.setAttribute("errorMessage", errorMessage);
                request.setAttribute("showAddModal", true);
                request.setAttribute("categoryName", categoryName);
                request.setAttribute("parentId", parentIdStr);

                doGet(request, response);
                return;
            }

            // Gọi DAO để thêm (không kiểm tra thành công/thất bại)
            CategoryDAO dao = new CategoryDAO();
            dao.insertCategory(categoryName.trim(), parentId);

            // Luôn giả định là thành công vì hàm insert là void
            response.sendRedirect("categorylist?status=success");
            return;
        }

        // Xử lý update status
        // Sửa phần xử lý updateStatus trong doPost method
        if ("updateStatus".equals(action)) {
            String categoryIdStr = request.getParameter("categoryId");
            String statusParam = request.getParameter("statusParam"); 

            try {
                int categoryId = Integer.parseInt(categoryIdStr);

                CategoryDAO dao = new CategoryDAO();
                Category category = dao.getCategoryById(categoryId);

                if (category != null) {
                    
                    // Logic: 
                    // - Nếu checkbox được check thì statusParam sẽ có giá trị "on"
                    // - Nếu checkbox không được check thì statusParam sẽ là null
                    String newStatus = (statusParam != null && statusParam.equals("on")) ? "active" : "inactive";

                    category.setStatus(newStatus);

                    dao.updateCategory(category);

                    // Redirect về trang hiện tại để tránh resubmit form
                    String currentPage = request.getParameter("page");
                    String search = request.getParameter("search");
                    String statusFilter = request.getParameter("statusFilter");

                    StringBuilder redirectUrl = new StringBuilder("categorylist");
                    List<String> params = new ArrayList<>();

                    if (currentPage != null && !currentPage.isEmpty()) {
                        params.add("page=" + currentPage);
                    }
                    if (search != null && !search.trim().isEmpty()) {
                        params.add("search=" + search);
                    }
                    if (statusFilter != null && !statusFilter.isEmpty()) {
                        params.add("status=" + statusFilter);
                    }

                    if (!params.isEmpty()) {
                        redirectUrl.append("?").append(String.join("&", params));
                    }

                    response.sendRedirect(redirectUrl.toString());
                    return;
                }
            } catch (NumberFormatException e) {
                // Log error hoặc xử lý lỗi
                System.out.println("Invalid category ID: " + categoryIdStr);
            }
        }
        // Nếu không có action nào được xử lý, quay về doGet
        doGet(request, response);
    }
}
