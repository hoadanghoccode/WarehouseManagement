package controller;

import dal.Import_noteDAO;
import dal.UserDAO;
import dal.WarehouseDAO;
import model.Import_note;
import model.Users;
import model.Warehouse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ImportNoteListController", urlPatterns = {"/import-note-list"})
public class ImportNoteListController extends HttpServlet {

    private static final int DEFAULT_PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Import_noteDAO inDao = new Import_noteDAO();
        UserDAO uDao = new UserDAO();
        WarehouseDAO wDao = new WarehouseDAO();

        // Filter parameters
        String searchUsername = request.getParameter("search");
        String importedParam = request.getParameter("imported");
        Boolean importedFilter = null;
        if (importedParam != null && !importedParam.isEmpty()) {
            importedFilter = Boolean.parseBoolean(importedParam);
        }
        String sortOrder = request.getParameter("sortOrder");

        // Get user IDs based on search username
        List<Integer> userIds = searchUsername != null && !searchUsername.trim().isEmpty() 
            ? inDao.getUserIdsByUsername(searchUsername) 
            : null;

        // Fetch all notes
        List<Import_note> allNotes;
        if (searchUsername != null && !searchUsername.trim().isEmpty() && userIds.isEmpty()) {
            // If search is provided but no users found, return empty list
            allNotes = new ArrayList<>();
        } else {
            allNotes = inDao.getAllImportNotes(userIds, importedFilter, sortOrder);
        }

        // Pagination calculation
        int page = getPageNumber(request);
        int totalSize = allNotes.size();
        int totalPages = (totalSize + DEFAULT_PAGE_SIZE - 1) / DEFAULT_PAGE_SIZE;
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }
        int start = (page - 1) * DEFAULT_PAGE_SIZE;
        int end = Math.min(start + DEFAULT_PAGE_SIZE, totalSize);
        List<Import_note> pagedNotes = allNotes.subList(start, end);

        // Resolve Users and Warehouses for paged notes
        List<Users> users = new ArrayList<>();
        List<Warehouse> warehouses = new ArrayList<>();
        for (Import_note in : pagedNotes) {
            users.add(uDao.getUserById(in.getUserId()));
            warehouses.add(wDao.getWarehouseById(in.getWarehouseId()));
        }

        // Set request attributes
        request.setAttribute("importNotes", pagedNotes);
        request.setAttribute("users", users);
        request.setAttribute("warehouses", warehouses);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalNotes", totalSize);
        request.setAttribute("search", searchUsername);
        request.setAttribute("imported", importedParam);
        request.setAttribute("sortOrder", sortOrder);

        // Forward to JSP
        request.getRequestDispatcher("importNoteList.jsp")
               .forward(request, response);
    }

    private int getPageNumber(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        if (pageParam == null || pageParam.trim().isEmpty()) {
            return 1;
        }
        try {
            return Math.max(1, Integer.parseInt(pageParam));
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}