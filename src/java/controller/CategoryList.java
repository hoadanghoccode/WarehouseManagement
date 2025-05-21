package controller;

import dal.CategoryDAO;
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

        // Danh sách tất cả category (có thể lọc theo search)
        List<Category> allCategories;
        if (search != null && !search.trim().isEmpty()) {
            allCategories = dao.searchCategoryByName(search);
        } else {
            allCategories = dao.getAllCategory();
        }

        // Phân loại ra category cha (parentId == null)
        List<Category> parentCategories = new ArrayList<>();
        for (Category cat : allCategories) {
            if (cat.getParentId() == null) {
                parentCategories.add(cat);
            }
        }

        // Gán sub-categories vào mỗi category cha
        for (Category parent : parentCategories) {
            List<Category> subList = new ArrayList<>();
            for (Category cat : allCategories) {
                if (cat.getParentId() != null && cat.getParentId().getCategoryId() == parent.getCategoryId()) {
                    subList.add(cat);
                }
            }
            parent.setSubCategories(subList);
            parent.setSubCategoryCount(subList.size());
        }

        // Paging trên category cha
        int page, numperpage = 2;
        int size = parentCategories.size();
        int num = (size % numperpage == 0) ? (size / numperpage) : (size / numperpage + 1);

        String xpage = request.getParameter("page");
        if (xpage == null) {
            page = 1;
        } else {
            page = Integer.parseInt(xpage);
        }

        int start = (page - 1) * numperpage;
        int end = Math.min(page * numperpage, size);

        List<Category> pagedParentCategories = dao.getListByPage((ArrayList<Category>) parentCategories, start, end);

        request.setAttribute("categoryList", pagedParentCategories); // chỉ chứa category cha (đã có danh sách con bên trong)
        request.setAttribute("search", search);
        request.setAttribute("page", page);
        request.setAttribute("num", num);

        request.getRequestDispatcher("categorylist.jsp").forward(request, response);
    }

}
