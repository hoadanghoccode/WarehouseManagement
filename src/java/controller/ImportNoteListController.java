//ImportNoteListController.java
package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.Import_noteDAO;
import dal.UserDAO;
import dal.WarehouseDAO;
import model.Import_note;
import model.Users;
import model.Warehouse;

@WebServlet(name = "ImportNoteListController", urlPatterns = {"/import-note-list"})
public class ImportNoteListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Import_noteDAO inDao = new Import_noteDAO();
        UserDAO uDao = new UserDAO();
        WarehouseDAO wDao = new WarehouseDAO();

        String importedParam = request.getParameter("imported");  
        Boolean importedFilter = null;
        if (importedParam != null && !importedParam.isEmpty()) {
            importedFilter = Boolean.parseBoolean(importedParam);
        }

        String sortOrder = request.getParameter("sortOrder");
        List<Import_note> importNotes = inDao.getAllImportNotes(importedFilter, sortOrder);

        List<Users> users = new ArrayList<>();
        List<Warehouse> warehouses = new ArrayList<>();
        for (Import_note in : importNotes) {
            Users user = uDao.getUserById(in.getUserId());
            Warehouse warehouse = wDao.getWarehouseById(in.getWarehouseId());
            if (user != null)     users.add(user);
            if (warehouse != null) warehouses.add(warehouse);
        }

        request.setAttribute("importNotes", importNotes);
        request.setAttribute("users", users);
        request.setAttribute("warehouses", warehouses);
        request.getRequestDispatcher("importNoteList.jsp").forward(request, response);
    }
}
