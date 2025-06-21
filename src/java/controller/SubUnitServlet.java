package controller;

import dal.SubUnitDAO;
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

public class SubUnitServlet extends HttpServlet {
    private SubUnitDAO subUnitDAO;

    @Override
    public void init() throws ServletException {
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
                viewSubUnitDetails(request, response);
                break;
            case "list":
                listSubUnits(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "add":
                addSubUnit(request, response);
                break;
            case "edit":
                updateSubUnit(request, response);
                break;
            case "delete":
            case "permanentDelete":
                deleteSubUnit(request, response);
                break;
            default:
                listSubUnits(request, response);
                break;
        }
    }

    private void listSubUnits(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int pageSize = 5;
        int start = (page - 1) * pageSize;

        // Use the new getAllSubUnits method with status filter
        List<SubUnit> allSubUnits = subUnitDAO.getAllSubUnits(status);

        if (search != null && !search.isEmpty()) {
            allSubUnits.removeIf(s -> !s.getName().toLowerCase().contains(search.toLowerCase()));
        }

        int totalSubUnits = allSubUnits.size();
        List<SubUnit> subUnits = allSubUnits.subList(Math.min(start, totalSubUnits), Math.min(start + pageSize, totalSubUnits));
        int totalPages = (int) Math.ceil((double) totalSubUnits / pageSize);

        request.setAttribute("subUnits", subUnits);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("totalSubUnits", totalSubUnits);
        request.getRequestDispatcher("/subunit.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("action", "add");
        responseMap.put("success", true);
        sendJsonResponse(response, responseMap);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int subUnitId;
        try {
            subUnitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Invalid subunit ID");
            sendJsonResponse(response, responseMap);
            return;
        }

        SubUnit subUnit = subUnitDAO.getSubUnitById(subUnitId);
        if (subUnit == null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Subunit not found");
            sendJsonResponse(response, responseMap);
            return;
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("action", "edit");
        responseMap.put("subUnit", subUnit);
        responseMap.put("success", true);
        sendJsonResponse(response, responseMap);
    }

    private void addSubUnit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String status = request.getParameter("status");

        if (name == null || name.trim().isEmpty()) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Subunit name is required");
            sendJsonResponse(response, responseMap);
            return;
        }

        if (subUnitDAO.isDuplicateSubUnitName(name.trim(), null)) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Subunit name already exists");
            sendJsonResponse(response, responseMap);
            return;
        }

        SubUnit subUnit = new SubUnit();
        subUnit.setName(name.trim());
        subUnit.setIsActive(status != null && status.equalsIgnoreCase("active"));
        subUnit.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        subUnit.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        boolean isSuccess = subUnitDAO.createSubUnit(subUnit);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", isSuccess);
        responseMap.put("message", isSuccess ? "Subunit created successfully" : "Error creating subunit");
        sendJsonResponse(response, responseMap);
    }

    private void updateSubUnit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int subUnitId;
        try {
            subUnitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Invalid subunit ID");
            sendJsonResponse(response, responseMap);
            return;
        }

        String name = request.getParameter("name");
        String status = request.getParameter("status");
        boolean confirmed = request.getParameter("confirmed") != null && request.getParameter("confirmed").equals("true");

        if (name == null || name.trim().isEmpty()) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Subunit name is required");
            sendJsonResponse(response, responseMap);
            return;
        }

        if (subUnitDAO.isDuplicateSubUnitName(name.trim(), subUnitId)) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Subunit name already exists");
            sendJsonResponse(response, responseMap);
            return;
        }

        SubUnit subUnit = subUnitDAO.getSubUnitById(subUnitId);
        if (subUnit == null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Subunit not found");
            sendJsonResponse(response, responseMap);
            return;
        }

        boolean newStatus = status != null && status.equalsIgnoreCase("active");
        boolean isDeactivating = subUnit.getIsActive() && !newStatus;

        if (isDeactivating && !confirmed) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Changing status to Inactive will permanently delete associated units. Please confirm this action.");
            responseMap.put("requiresConfirmation", true);
            sendJsonResponse(response, responseMap);
            return;
        }

        boolean isSuccess;
        String errorMessage = "Error updating subunit";
        if (isDeactivating) {
            subUnit.setName(name.trim());
            subUnit.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            isSuccess = subUnitDAO.deleteSubUnit(subUnitId);
            if (!isSuccess) {
                errorMessage = "Failed to deactivate subunit and delete associated units. Check logs for details.";
            }
        } else {
            subUnit.setName(name.trim());
            subUnit.setIsActive(newStatus);
            subUnit.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            isSuccess = subUnitDAO.updateSubUnit(subUnit);
            if (!isSuccess) {
                errorMessage = "Failed to update subunit name or status. Check logs for details.";
            }
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", isSuccess);
        responseMap.put("message", isSuccess ? "Subunit updated successfully" : errorMessage);
        sendJsonResponse(response, responseMap);
    }

    private void deleteSubUnit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int subUnitId;
        try {
            subUnitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Invalid subunit ID");
            sendJsonResponse(response, responseMap);
            return;
        }

        String action = request.getParameter("action");
        boolean isSuccess;
        String errorMessage;
        if ("permanentDelete".equals(action)) {
            isSuccess = subUnitDAO.permanentDeleteSubUnit(subUnitId);
            errorMessage = "Error permanently deleting subunit";
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", isSuccess);
            responseMap.put("message", isSuccess ? "Subunit permanently deleted" : errorMessage);
            sendJsonResponse(response, responseMap);
        } else {
            isSuccess = subUnitDAO.deleteSubUnit(subUnitId);
            errorMessage = "Error deactivating subunit";
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", isSuccess);
            responseMap.put("message", isSuccess ? "Subunit deactivated successfully" : errorMessage);
            sendJsonResponse(response, responseMap);
        }
    }

    private void viewSubUnitDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int subUnitId;
        try {
            subUnitId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Invalid subunit ID");
            sendJsonResponse(response, responseMap);
            return;
        }

        SubUnit subUnit = subUnitDAO.getSubUnitById(subUnitId);
        if (subUnit == null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", false);
            responseMap.put("message", "Subunit not found");
            sendJsonResponse(response, responseMap);
            return;
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", true);
        responseMap.put("name", subUnit.getName());
        responseMap.put("status", subUnit.getIsActive() ? "Active" : "Inactive");
        responseMap.put("createdAt", subUnit.getCreatedAt().toString());
        responseMap.put("updatedAt", subUnit.getUpdatedAt().toString());
        sendJsonResponse(response, responseMap);
    }

    private void sendJsonResponse(HttpServletResponse response, Map<String, Object> responseMap) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        out.print(gson.toJson(responseMap));
        out.flush();
    }
}