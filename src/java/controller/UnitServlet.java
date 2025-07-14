package controller;

import dal.UnitDAO;
import model.Unit;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.gson.Gson;

public class UnitServlet extends HttpServlet {
    private static final int PAGE_SIZE = 5;
    private UnitDAO unitDAO;

    @Override
    public void init() throws ServletException {
        unitDAO = new UnitDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";
        switch (action) {
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "view":
                viewUnitDetails(request, response);
                break;
            default:
                listUnits(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";
        switch (action) {
            case "add":
                addUnit(request, response);
                break;
            case "edit":
                updateUnit(request, response);
                break;
            case "deactivate":
                deactivateUnit(request, response);
                break;
            case "permanentDelete":
                permanentDeleteUnit(request, response);
                break;
            default:
                listUnits(request, response);
                break;
        }
    }

    // List units with pagination and filters
    private void listUnits(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        int page = 1;
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null) page = Integer.parseInt(pageStr);
        } catch (NumberFormatException e) {
            page = 1;
        }

        // Fetch units with status filter
        List<Unit> units = unitDAO.getAllUnits(status);

        // Apply search filter
        if (search != null && !search.isEmpty()) {
            units.removeIf(u -> !u.getName().toLowerCase().contains(search.toLowerCase()));
        }

        // Add transaction dependency information for each unit
        List<Map<String, Object>> unitMaps = units.stream().map(unit -> {
            Map<String, Object> unitMap = new HashMap<>();
            unitMap.put("unitId", unit.getUnitId());
            unitMap.put("name", unit.getName());
            unitMap.put("status", unit.getStatus());
            unitMap.put("createdAt", unit.getCreatedAt() != null ? unit.getCreatedAt().toString() : "N/A");
            unitMap.put("updatedAt", unit.getUpdatedAt() != null ? unit.getUpdatedAt().toString() : "N/A");
            unitMap.put("hasDependencies", unitDAO.hasTransactionDependencies(unit.getUnitId()));
            return unitMap;
        }).collect(Collectors.toList());

        // Pagination
        int totalUnits = unitMaps.size();
        int totalPages = (int) Math.ceil((double) totalUnits / PAGE_SIZE);
        if (page < 1) page = 1;
        if (page > totalPages && totalPages > 0) page = totalPages;
        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, totalUnits);
        List<Map<String, Object>> paginatedUnits = unitMaps.subList(start, end);

        // Set attributes for JSP
        request.setAttribute("units", paginatedUnits);
        request.setAttribute("totalUnits", totalUnits);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.getRequestDispatcher("/unit.jsp").forward(request, response);
    }

    // Show form for adding a new unit
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", true);
        sendJsonResponse(response, responseMap);
    }

    // Show form for editing a unit
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int unitId;
        try {
            unitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            sendJsonResponse(response, Map.of("success", false, "message", "Invalid unit ID"));
            return;
        }
        // Check for transaction dependencies
        if (unitDAO.hasTransactionDependencies(unitId)) {
            sendJsonResponse(response, Map.of("success", false, "message", "Cannot edit unit: It is used in transactions"));
            return;
        }
        Unit unit = unitDAO.getUnitById(unitId);
        if (unit == null) {
            sendJsonResponse(response, Map.of("success", false, "message", "Unit not found"));
            return;
        }
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", true);
        responseMap.put("unit", unit);
        sendJsonResponse(response, responseMap);
    }

    // Add a new unit
    private void addUnit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String status = request.getParameter("status");
        if (name == null || name.trim().isEmpty()) {
            sendJsonResponse(response, Map.of("success", false, "message", "Unit name is required"));
            return;
        }
        if (unitDAO.isDuplicateUnitName(name.trim(), null)) {
            sendJsonResponse(response, Map.of("success", false, "message", "Unit name already exists"));
            return;
        }
        Unit unit = new Unit();
        unit.setName(name.trim());
        unit.setStatus(status != null && !status.isEmpty() ? status : "active");
        unit.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        unit.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        boolean isSuccess = unitDAO.createUnit(unit);
        sendJsonResponse(response, Map.of(
            "success", isSuccess,
            "message", isSuccess ? "Unit created successfully" : "Error creating unit"
        ));
    }

    // Update an existing unit
    private void updateUnit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int unitId;
        try {
            unitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            sendJsonResponse(response, Map.of("success", false, "message", "Invalid unit ID"));
            return;
        }
        // Check for transaction dependencies
        if (unitDAO.hasTransactionDependencies(unitId)) {
            sendJsonResponse(response, Map.of("success", false, "message", "Cannot edit unit: It is used in transactions"));
            return;
        }
        String name = request.getParameter("name");
        String status = request.getParameter("status");
        if (name == null || name.trim().isEmpty()) {
            sendJsonResponse(response, Map.of("success", false, "message", "Unit name is required"));
            return;
        }
        if (unitDAO.isDuplicateUnitName(name.trim(), unitId)) {
            sendJsonResponse(response, Map.of("success", false, "message", "Unit name already exists"));
            return;
        }
        Unit unit = unitDAO.getUnitById(unitId);
        if (unit == null) {
            sendJsonResponse(response, Map.of("success", false, "message", "Unit not found"));
            return;
        }
        unit.setName(name.trim());
        unit.setStatus(status != null && !status.isEmpty() ? status : "active");
        unit.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        boolean isSuccess = unitDAO.updateUnit(unit);
        sendJsonResponse(response, Map.of(
            "success", isSuccess,
            "message", isSuccess ? "Unit updated successfully" : "Error updating unit"
        ));
    }

    // Deactivate a unit
    private void deactivateUnit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int unitId;
        try {
            unitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            sendJsonResponse(response, Map.of("success", false, "message", "Invalid unit ID"));
            return;
        }
        // Check for transaction dependencies
        if (unitDAO.hasTransactionDependencies(unitId)) {
            sendJsonResponse(response, Map.of("success", false, "message", "Cannot deactivate unit: It is used in transactions"));
            return;
        }
        boolean isSuccess = unitDAO.deleteUnit(unitId);
        sendJsonResponse(response, Map.of(
            "success", isSuccess,
            "message", isSuccess ? "Unit deactivated successfully" : "Error deactivating unit"
        ));
    }

    // Permanently delete a unit
    private void permanentDeleteUnit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int unitId;
        try {
            unitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            sendJsonResponse(response, Map.of("success", false, "message", "Invalid unit ID"));
            return;
        }
        // Check for transaction dependencies
        if (unitDAO.hasTransactionDependencies(unitId)) {
            sendJsonResponse(response, Map.of("success", false, "message", "Cannot permanently delete unit: It is used in transactions"));
            return;
        }
        boolean isSuccess = unitDAO.permanentDeleteUnit(unitId);
        sendJsonResponse(response, Map.of(
            "success", isSuccess,
            "message", isSuccess ? "Unit permanently deleted successfully" : "Error permanently deleting unit"
        ));
    }

    // View unit details
    private void viewUnitDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int unitId;
        try {
            unitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            sendJsonResponse(response, Map.of("success", false, "message", "Invalid unit ID"));
            return;
        }
        Unit unit = unitDAO.getUnitById(unitId);
        if (unit == null) {
            sendJsonResponse(response, Map.of("success", false, "message", "Unit not found"));
            return;
        }
        Map<String, Object> unitMap = new HashMap<>();
        unitMap.put("unitId", unit.getUnitId());
        unitMap.put("name", unit.getName());
        unitMap.put("status", unit.getStatus());
        unitMap.put("createdAt", unit.getCreatedAt() != null ? unit.getCreatedAt().toString() : "N/A");
        unitMap.put("updatedAt", unit.getUpdatedAt() != null ? unit.getUpdatedAt().toString() : "N/A");
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", true);
        responseMap.put("unit", unitMap);
        sendJsonResponse(response, responseMap);
    }

    // Send JSON response
    private void sendJsonResponse(HttpServletResponse response, Map<String, Object> responseMap) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(responseMap));
        out.flush();
    }
}