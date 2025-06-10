package controller;

import dal.UnitDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Units;

public class UnitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        UnitDAO dao = new UnitDAO();

        try {
            System.out.println(">> UnitServlet: action = " + action);
            if (action == null) {
                action = "list";
            }

            switch (action) {
                case "list":
                    List<Units> units = dao.getAllUnits();
                    System.out.println(">> Fetched " + (units != null ? units.size() : 0) + " units.");
                    request.setAttribute("units", units != null ? units : new ArrayList<>());
                    request.getRequestDispatcher("/unitlist.jsp").forward(request, response);
                    break;

                case "edit":
                    String unitIdStr = request.getParameter("id");
                    if (unitIdStr != null && !unitIdStr.isEmpty()) {
                        int unitId = Integer.parseInt(unitIdStr);
                        Units unit = dao.getUnitById(unitId);
                        request.setAttribute("unit", unit);
                        request.setAttribute("action", "edit");
                        request.setAttribute("units", dao.getAllUnits());
                    }
                    request.getRequestDispatcher("/unitlist.jsp").forward(request, response);
                    break;

                case "detail":
                    String detailIdStr = request.getParameter("id");
                    if (detailIdStr != null && !detailIdStr.isEmpty()) {
                        int unitId = Integer.parseInt(detailIdStr);
                        Units unit = dao.getUnitById(unitId);
                        request.setAttribute("unit", unit);
                        request.setAttribute("action", "detail");
                        request.setAttribute("units", dao.getAllUnits());
                    }
                    request.getRequestDispatcher("/unitlist.jsp").forward(request, response);
                    break;

                default:
                    List<Units> defaultUnits = dao.getAllUnits();
                    request.setAttribute("units", defaultUnits != null ? defaultUnits : new ArrayList<>());
                    request.getRequestDispatcher("/unitlist.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            System.err.println("UnitServlet Error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMsg", "Đã xảy ra lỗi: " + e.getMessage());
            request.setAttribute("units", dao.getAllUnits());
            request.getRequestDispatcher("/unitlist.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        UnitDAO dao = new UnitDAO();

        try {
            if (action == null || action.isEmpty()) {
                request.setAttribute("errorMsg", "Thiếu hành động");
                request.setAttribute("units", dao.getAllUnits());
                request.getRequestDispatcher("/unitlist.jsp").forward(request, response);
                return;
            }

            switch (action) {
                case "add":
                    handleAdd(request, response, dao);
                    break;
                case "update":
                    handleUpdate(request, response, dao);
                    break;
                case "delete":
                    String deleteIdStr = request.getParameter("id");
                    if (deleteIdStr != null && !deleteIdStr.isEmpty()) {
                        int unitId = Integer.parseInt(deleteIdStr);
                        boolean success = dao.deleteUnit(unitId);
                        if (!success) {
                            request.setAttribute("errorMsg", "Không thể xóa đơn vị với ID " + unitId + ". Có thể đơn vị không tồn tại hoặc đang được sử dụng.");
                            request.setAttribute("units", dao.getAllUnits());
                            request.getRequestDispatcher("/unitlist.jsp").forward(request, response);
                            return;
                        }
                    }
                    response.sendRedirect("unit?action=list");
                    break;
                default:
                    request.setAttribute("errorMsg", "Hành động không hợp lệ");
                    request.setAttribute("units", dao.getAllUnits());
                    request.getRequestDispatcher("/unitlist.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            System.err.println("UnitServlet Error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMsg", "Đã xảy ra lỗi: " + e.getMessage());
            request.setAttribute("units", dao.getAllUnits());
            request.getRequestDispatcher("/unitlist.jsp").forward(request, response);
        }
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response, UnitDAO dao)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String isActiveStr = request.getParameter("isActive");
        boolean isActive = "true".equalsIgnoreCase(isActiveStr);

        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorMsg", "Vui lòng nhập tên đơn vị.");
            request.setAttribute("units", dao.getAllUnits());
            request.getRequestDispatcher("/unitlist.jsp").forward(request, response);
            return;
        }

        Units unit = new Units();
        unit.setName(name.trim());
        unit.setActive(isActive);
        dao.createUnit(unit);
        response.sendRedirect("unit?action=list");
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response, UnitDAO dao)
            throws ServletException, IOException {
        String unitIdStr = request.getParameter("unitId");
        String name = request.getParameter("name");
        String isActiveStr = request.getParameter("isActive");

        if (unitIdStr == null || unitIdStr.isEmpty() || name == null || name.trim().isEmpty()) {
            request.setAttribute("errorMsg", "Vui lòng nhập ID và tên đơn vị.");
            request.setAttribute("units", dao.getAllUnits());
            request.getRequestDispatcher("/unitlist.jsp").forward(request, response);
            return;
        }

        int unitId = Integer.parseInt(unitIdStr);
        boolean isActive = "true".equalsIgnoreCase(isActiveStr);

        Units unit = new Units();
        unit.setUnitId(unitId);
        unit.setName(name.trim());
        unit.setActive(isActive);
        dao.updateUnit(unit);
        response.sendRedirect("unit?action=list");
    }

    @Override
    public String getServletInfo() {
        return "Handles CRUD operations for Units";
    }
}