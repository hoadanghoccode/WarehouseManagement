package controller;

import dal.UnitDAO;
import dal.SubUnitDAO;
import model.Units;
import model.SubUnit;
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
import com.google.gson.Gson;

public class UnitServlet extends HttpServlet {
    private static final int PAGE_SIZE = 5;
    private UnitDAO unitDAO;
    private SubUnitDAO subUnitDAO;

    @Override
    public void init() throws ServletException {
        unitDAO = new UnitDAO();
        subUnitDAO = new SubUnitDAO();
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

    private void listUnits(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        String subUnitIdStr = request.getParameter("subUnitId");
        String status = request.getParameter("status");
        int page = 1;
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null) page = Integer.parseInt(pageStr);
        } catch (NumberFormatException e) {
            page = 1;
        }

        // Use the new getAllUnits method with status filter
        List<Units> units = unitDAO.getAllUnits(status);

        if (search != null && !search.isEmpty()) {
            units.removeIf(u -> !u.getName().toLowerCase().contains(search.toLowerCase()));
        }
        if (subUnitIdStr != null && !subUnitIdStr.isEmpty()) {
            try {
                int subUnitId = Integer.parseInt(subUnitIdStr);
                units.removeIf(u -> u.getSubUnitId() != subUnitId);
            } catch (NumberFormatException e) {
                // Ignore invalid subUnitId
            }
        }

        int totalUnits = units.size();
        int totalPages = (int) Math.ceil((double) totalUnits / PAGE_SIZE);
        if (page < 1) page = 1;
        if (page > totalPages && totalPages > 0) page = totalPages;
        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, totalUnits);
        List<Units> paginatedUnits = units.subList(start, end);
        List<SubUnit> subUnits = subUnitDAO.getAllSubUnits(true); // SubUnits for filter dropdown
        request.setAttribute("units", paginatedUnits);
        request.setAttribute("subUnits", subUnits);
        request.setAttribute("totalUnits", totalUnits);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("search", search);
        request.setAttribute("subUnitId", subUnitIdStr);
        request.setAttribute("status", status);
        request.getRequestDispatcher("/unit.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<SubUnit> subUnits = subUnitDAO.getAllSubUnits(true);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", true);
        responseMap.put("subUnits", subUnits);
        sendJsonResponse(response, responseMap);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int unitId;
        try {
            unitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Invalid unit ID");
            sendJsonResponse(response, responseMap);
            return;
        }
        Units unit = unitDAO.getUnitById(unitId);
        if (unit == null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Unit not found");
            sendJsonResponse(response, responseMap);
            return;
        }
        List<SubUnit> subUnits = subUnitDAO.getAllSubUnits(true);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", true);
        responseMap.put("unit", unit);
        responseMap.put("subUnits", subUnits);
        sendJsonResponse(response, responseMap);
    }

    private void addUnit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String status = request.getParameter("status");
        int subUnitId;
        double factor;
        try {
            subUnitId = Integer.parseInt(request.getParameter("subUnitId"));
            factor = Double.parseDouble(request.getParameter("factor"));
        } catch (NumberFormatException e) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Invalid input data");
            sendJsonResponse(response, responseMap);
            return;
        }
        if (name == null || name.trim().isEmpty()) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Unit name is required");
            sendJsonResponse(response, responseMap);
            return;
        }
        if (unitDAO.isDuplicateUnitName(name.trim(), null)) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Unit name already exists");
            sendJsonResponse(response, responseMap);
            return;
        }
        Units unit = new Units();
        unit.setName(name.trim());
        unit.setSubUnitId(subUnitId);
        unit.setFactor(factor);
        unit.setIsActive(status != null && status.equalsIgnoreCase("active"));
        unit.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        unit.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        boolean isSuccess = unitDAO.createUnit(unit);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", isSuccess);
        responseMap.put("message", isSuccess ? "Unit created successfully" : "Error creating unit");
        sendJsonResponse(response, responseMap);
    }

    private void updateUnit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int unitId;
        try {
            unitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Invalid unit ID");
            sendJsonResponse(response, responseMap);
            return;
        }
        String name = request.getParameter("name");
        String status = request.getParameter("status");
        int subUnitId;
        double factor;
        try {
            subUnitId = Integer.parseInt(request.getParameter("subUnitId"));
            factor = Double.parseDouble(request.getParameter("factor"));
        } catch (NumberFormatException e) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Invalid input data");
            sendJsonResponse(response, responseMap);
            return;
        }
        if (name == null || name.trim().isEmpty()) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Unit name is required");
            sendJsonResponse(response, responseMap);
            return;
        }
        if (unitDAO.isDuplicateUnitName(name.trim(), unitId)) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Unit name already exists");
            sendJsonResponse(response, responseMap);
            return;
        }
        Units unit = unitDAO.getUnitById(unitId);
        if (unit == null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Unit not found");
            sendJsonResponse(response, responseMap);
            return;
        }
        unit.setName(name.trim());
        unit.setSubUnitId(subUnitId);
        unit.setFactor(factor);
        unit.setIsActive(status != null && status.equalsIgnoreCase("active"));
        unit.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        boolean isSuccess = unitDAO.updateUnit(unit);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", isSuccess);
        responseMap.put("message", isSuccess ? "Unit updated successfully" : "Error updating unit");
        sendJsonResponse(response, responseMap);
    }

    private void deactivateUnit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int unitId;
        try {
            unitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Invalid unit ID");
            sendJsonResponse(response, responseMap);
            return;
        }
        boolean isSuccess = unitDAO.deleteUnit(unitId);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", isSuccess);
        responseMap.put("message", isSuccess ? "Unit deactivated successfully" : "Error deactivating unit");
        sendJsonResponse(response, responseMap);
    }

    private void permanentDeleteUnit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int unitId;
        try {
            unitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Invalid unit ID");
            sendJsonResponse(response, responseMap);
            return;
        }
        boolean isSuccess = unitDAO.permanentDeleteUnit(unitId);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", isSuccess);
        responseMap.put("message", isSuccess ? "Unit permanently deleted" : "Error permanently deleting unit");
        sendJsonResponse(response, responseMap);
    }

    private void viewUnitDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int unitId;
        try {
            unitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            sendJsonResponse(response, Map.of("success", false, "message", "Invalid unit ID"));
            return;
        }
        Units unit = unitDAO.getUnitById(unitId);
        if (unit == null) {
            sendJsonResponse(response, Map.of("success", false, "message", "Unit not found"));
            return;
        }
        SubUnit subUnit = subUnitDAO.getSubUnitById(unit.getSubUnitId());
        Map<String, Object> unitMap = new HashMap<>();
        unitMap.put("name", unit.getName());
        unitMap.put("subUnitName", subUnit != null ? subUnit.getName() : "N/A");
        unitMap.put("factor", unit.getFactor());
        unitMap.put("status", unit.getIsActive() ? "Active" : "Inactive");
        unitMap.put("createdAt", unit.getCreatedAt() != null ? unit.getCreatedAt().toString() : "N/A");
        unitMap.put("updatedAt", unit.getUpdatedAt() != null ? unit.getUpdatedAt().toString() : "N/A");
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", true);
        responseMap.put("unit", unitMap);
        sendJsonResponse(response, responseMap);
    }

    private void sendJsonResponse(HttpServletResponse response, Map<String, Object> responseMap) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(responseMap));
        out.flush();
    }
}