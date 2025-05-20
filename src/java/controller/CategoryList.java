package controller;

import dal.CategoryDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;

public class CategoryList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String search = request.getParameter("search");
        CategoryDAO dao = new CategoryDAO();
        List<Category> list1;

        if (search != null && !search.trim().isEmpty()) {
            list1 = dao.searchCategoryByName(search);
        } else {
            list1 = dao.getAllCategory();
        }

        int page, numperpage = 1;
        int size = list1.size();
        int num = (size % numperpage == 0) ? (size / numperpage) : (size / numperpage + 1);

        String xpage = request.getParameter("page");
        if (xpage == null) {
            page = 1;
        } else {
            page = Integer.parseInt(xpage);
        }

        int start = (page - 1) * numperpage;
        int end = Math.min(page * numperpage, size);

        List<Category> list = dao.getListByPage((ArrayList<Category>) list1, start, end);

        request.setAttribute("categoryList", list);
        request.setAttribute("search", search);
        request.setAttribute("page", page);
        request.setAttribute("num", num);

        request.getRequestDispatcher("categorylist.jsp").forward(request, response);
    }
}
