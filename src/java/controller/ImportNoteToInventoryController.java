package controller;

import dal.Import_noteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ImportNoteToInventoryController", urlPatterns = {"/import-note-to-inventory"})
public class ImportNoteToInventoryController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int importNoteId = Integer.parseInt(request.getParameter("importNoteId"));
        String[] arr = request.getParameterValues("detailIds");
        List<Integer> detailIds = new ArrayList<>();
        if (arr != null) {
            for (String s : arr) detailIds.add(Integer.parseInt(s));
        }

        boolean success = false;
        String message = null;
        Import_noteDAO inDao = new Import_noteDAO();
        try {
            success = inDao.importNoteDetailsToInventory(importNoteId, detailIds);
        } catch (SQLException e) {
            message = e.getMessage();
        }

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (success) {
            out.print("{\"success\":true}");
        } else {
            out.print("{\"success\":false,\"message\":\"" 
                    + (message != null ? message.replace("\"","\\\"") : "Lỗi xử lý") + "\"}");
        }
        out.flush();
    }
}
