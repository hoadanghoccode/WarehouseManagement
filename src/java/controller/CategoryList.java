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
        CategoryDAO dao = new CategoryDAO();
        MaterialDAO mDao = new MaterialDAO();

        List<Category> parentCategories = dao.getAllParentCategory();
        List<Category> filterCategories;

        // Nếu có tìm kiếm thì lọc
        if (search != null && !search.trim().isEmpty()) {
            filterCategories = dao.searchCategoryByName((ArrayList<Category>) parentCategories, search.trim());
        } else {
            filterCategories = parentCategories;
        }
        for (Category pcate : filterCategories) {
            for (Category subCate : pcate.getSubCategories()) {
                subCate.setMaterialCount(mDao.countMaterialByCategoryId(subCate.getCategoryId()));
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
        request.setAttribute("page", page);
        request.setAttribute("num", num);
        request.setAttribute("pageSize", numperpage);
        request.setAttribute("totalCategories", size);

        
        request.getRequestDispatcher("categorylist.jsp").forward(request, response);
    }

}
