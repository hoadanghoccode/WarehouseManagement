package controller;

import dal.UnitDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Units;
import model.SubUnit;
import model.UnitConversion;
import java.util.List;

public class UnitServlet extends HttpServlet {

    private UnitDAO dao;

    @Override
    public void init() throws ServletException {
        dao = new UnitDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String entity = request.getParameter("entity");
        String action = request.getParameter("action");
        if (entity == null || action == null) {
            loadAllData(request, response);
            return;
        }

        try {
            switch (entity) {
                case "unit":
                    handleUnitAction(action, request, response);
                    break;
                case "subunit":
                    handleSubUnitAction(action, request, response);
                    break;
                case "conversion":
                    handleConversionAction(action, request, response);
                    break;
                default:
                    setErrorMessage(request, "Invalid entity: " + entity);
                    loadAllData(request, response);
                    break;
            }
        } catch (Exception e) {
            logError("doGet", e);
            setErrorMessage(request, "Error: " + e.getMessage());
            loadAllData(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String entity = request.getParameter("entity");
        String action = request.getParameter("action");
        if ("clearMessages".equals(action)) {
            handleClearMessages(request, response);
            return;
        }
        if (entity == null || action == null) {
            setErrorMessage(request, "Entity or action not provided");
            loadAllData(request, response);
            return;
        }

        try {
            switch (entity) {
                case "unit":
                    handleUnitPostAction(action, request, response);
                    break;
                case "subunit":
                    handleSubUnitPostAction(action, request, response);
                    break;
                case "conversion":
                    handleConversionPostAction(action, request, response);
                    break;
                default:
                    setErrorMessage(request, "Invalid entity: " + entity);
                    loadAllData(request, response);
                    break;
            }
        } catch (Exception e) {
            logError("doPost", e);
            setErrorMessage(request, "Error: " + e.getMessage());
            loadAllData(request, response);
        }
    }

    private void loadAllData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("units", dao.getAllUnits());
        request.setAttribute("subunits", dao.getAllSubUnits());
        request.setAttribute("conversions", dao.getAllUnitConversions());
        request.getRequestDispatcher("/unitlist.jsp").forward(request, response);
    }

    private void setSuccessMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("successMsg", message);
    }

    private void setErrorMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("errorMsg", message);
    }

    // Unit Actions
    private void handleUnitAction(String action, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (action) {
            case "edit":
                handleUnitEdit(request, response);
                break;
            case "detail":
                handleUnitDetail(request, response);
                break;
            case "checkConversions":
                handleCheckUnitConversions(request, response);
                break;
            default:
                loadAllData(request, response);
                break;
        }
    }

    private void handleUnitEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String unitIdStr = request.getParameter("id");
        if (unitIdStr != null && !unitIdStr.isEmpty()) {
            try {
                int unitId = Integer.parseInt(unitIdStr);
                Units unit = dao.getUnitById(unitId);
                if (unit != null) {
                    request.setAttribute("unit", unit);
                    request.setAttribute("action", "edit");
                    request.setAttribute("entity", "unit");
                } else {
                    setErrorMessage(request, "Unit not found with ID: " + unitId);
                }
            } catch (NumberFormatException e) {
                setErrorMessage(request, "Invalid unit ID: " + unitIdStr);
            }
        } else {
            setErrorMessage(request, "Unit ID not provided");
        }
        loadAllData(request, response);
    }

    private void handleUnitDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String unitIdStr = request.getParameter("id");
        if (unitIdStr != null && !unitIdStr.isEmpty()) {
            try {
                int unitId = Integer.parseInt(unitIdStr);
                Units unit = dao.getUnitById(unitId);
                if (unit != null) {
                    request.setAttribute("unit", unit);
                    request.setAttribute("action", "detail");
                    request.setAttribute("entity", "unit");
                } else {
                    setErrorMessage(request, "Unit not found with ID: " + unitId);
                }
            } catch (NumberFormatException e) {
                setErrorMessage(request, "Invalid unit ID: " + unitIdStr);
            }
        } else {
            setErrorMessage(request, "Unit ID not provided");
        }
        loadAllData(request, response);
    }

    private void handleCheckUnitConversions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String unitIdStr = request.getParameter("id");
        if (unitIdStr != null && !unitIdStr.isEmpty()) {
            try {
                int unitId = Integer.parseInt(unitIdStr);
                List<UnitConversion> conversions = dao.getUnitConversionsByUnitId(unitId);
                int countConversions = conversions != null ? conversions.size() : 0;
                Units unit = dao.getUnitById(unitId);

                if (unit != null) {
                    request.setAttribute("countConversions", countConversions);
                    request.setAttribute("unit", unit);
                    request.setAttribute("entity", "unit");
                    request.setAttribute("action", "confirmDeactivateUnit");
                } else {
                    setErrorMessage(request, "Unit not found with ID: " + unitId);
                }
            } catch (NumberFormatException e) {
                setErrorMessage(request, "Invalid unit ID: " + unitIdStr);
            }
        } else {
            setErrorMessage(request, "Unit ID not provided");
        }
        loadAllData(request, response);
    }

    private void handleUnitPostAction(String action, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (action) {
            case "add":
                handleUnitAdd(request, response);
                break;
            case "update":
                handleUnitUpdate(request, response);
                break;
            case "delete":
                handleUnitDelete(request, response);
                break;
            case "confirmDeactivateUnit":
                handleConfirmDeactivateUnit(request, response);
                break;
            default:
                setErrorMessage(request, "Invalid action: " + action);
                loadAllData(request, response);
                break;
        }
    }

    private void handleUnitAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String isActiveStr = request.getParameter("isActive");

        if (name == null || name.trim().isEmpty()) {
            setErrorMessage(request, "Please enter the unit name");
            loadAllData(request, response);
            return;
        }

        boolean isActive = "true".equalsIgnoreCase(isActiveStr);
        Units unit = new Units();
        unit.setName(name.trim());
        unit.setActive(isActive);

        try {
            dao.createUnit(unit);
            setSuccessMessage(request, "Unit added successfully");
            response.sendRedirect("unit");
        } catch (Exception e) {
            logError("handleUnitAdd", e);
            setErrorMessage(request, "Error adding unit: " + e.getMessage());
            loadAllData(request, response);
        }
    }

    private void handleUnitUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String unitIdStr = request.getParameter("unitId");
        String name = request.getParameter("name");
        String isActiveStr = request.getParameter("isActive");
        String originalStatus = request.getParameter("originalStatus");

        if (unitIdStr == null || unitIdStr.isEmpty() || name == null || name.trim().isEmpty()) {
            setErrorMessage(request, "Please provide unit ID and name");
            loadAllData(request, response);
            return;
        }

        try {
            int unitId = Integer.parseInt(unitIdStr);
            boolean isActive = "true".equalsIgnoreCase(isActiveStr);

            Units unit = dao.getUnitById(unitId);
            if (unit == null) {
                setErrorMessage(request, "Unit not found with ID: " + unitId);
                loadAllData(request, response);
                return;
            }

            if ("true".equals(originalStatus) && !isActive) {
                List<UnitConversion> conversions = dao.getUnitConversionsByUnitId(unitId);
                int countConversions = conversions != null ? conversions.size() : 0;
                request.setAttribute("countConversions", countConversions);
                request.setAttribute("unit", unit);
                request.setAttribute("entity", "unit");
                request.setAttribute("action", "confirmDeactivateUnit");
                loadAllData(request, response);
                return;
            }

            unit.setName(name.trim());
            unit.setActive(isActive);
            dao.updateUnit(unit);
            setSuccessMessage(request, "Unit updated successfully");
            response.sendRedirect("unit");
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Invalid unit ID: " + unitIdStr);
            loadAllData(request, response);
        } catch (Exception e) {
            logError("handleUnitUpdate", e);
            setErrorMessage(request, "Error updating unit: " + e.getMessage());
            loadAllData(request, response);
        }
    }

    private void handleUnitDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String unitIdStr = request.getParameter("id");
        if (unitIdStr == null || unitIdStr.isEmpty()) {
            setErrorMessage(request, "Unit ID not provided");
            loadAllData(request, response);
            return;
        }

        try {
            int unitId = Integer.parseInt(unitIdStr);
            boolean success = dao.deleteUnit(unitId);
            if (!success) {
                setErrorMessage(request, "Cannot delete unit with ID " + unitId + ". Unit may not exist or is in use.");
                loadAllData(request, response);
            } else {
                setSuccessMessage(request, "Unit deleted successfully");
                response.sendRedirect("unit");
            }
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Invalid unit ID: " + unitIdStr);
            loadAllData(request, response);
        } catch (Exception e) {
            logError("handleUnitDelete", e);
            setErrorMessage(request, "Error deleting unit: " + e.getMessage());
            loadAllData(request, response);
        }
    }

    private void handleConfirmDeactivateUnit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String unitIdStr = request.getParameter("unitId");
        String newStatus = request.getParameter("newStatus");

        if (unitIdStr == null || unitIdStr.isEmpty() || newStatus == null || !newStatus.equals("false")) {
            setErrorMessage(request, "Invalid parameters for deactivation");
            loadAllData(request, response);
            return;
        }

        try {
            int unitId = Integer.parseInt(unitIdStr);
            Units unit = dao.getUnitById(unitId);
            if (unit == null) {
                setErrorMessage(request, "Unit not found with ID: " + unitId);
                loadAllData(request, response);
                return;
            }

            dao.deleteUnitConversionsByUnitId(unitId);
            unit.setActive(false);
            dao.updateUnit(unit);
            setSuccessMessage(request, "Unit deactivated successfully");
            response.sendRedirect("unit");
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Invalid unit ID: " + unitIdStr);
            loadAllData(request, response);
        } catch (Exception e) {
            logError("handleConfirmDeactivateUnit", e);
            setErrorMessage(request, "Error deactivating unit: " + e.getMessage());
            loadAllData(request, response);
        }
    }

    // SubUnit Actions
    private void handleSubUnitAction(String action, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (action) {
            case "edit":
                handleSubUnitEdit(request, response);
                break;
            case "detail":
                handleSubUnitDetail(request, response);
                break;
            case "checkConversions":
                handleCheckConversions(request, response);
                break;
            default:
                loadAllData(request, response);
                break;
        }
    }

    private void handleSubUnitEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String subUnitIdStr = request.getParameter("id");
        if (subUnitIdStr != null && !subUnitIdStr.isEmpty()) {
            try {
                int subUnitId = Integer.parseInt(subUnitIdStr);
                SubUnit subUnit = dao.getSubUnitById(subUnitId);
                if (subUnit != null) {
                    request.setAttribute("subunit", subUnit);
                    request.setAttribute("action", "edit");
                    request.setAttribute("entity", "subunit");
                } else {
                    setErrorMessage(request, "Subunit not found with ID: " + subUnitId);
                }
            } catch (NumberFormatException e) {
                setErrorMessage(request, "Invalid subunit ID: " + subUnitIdStr);
            }
        } else {
            setErrorMessage(request, "Subunit ID not provided");
        }
        loadAllData(request, response);
    }

    private void handleSubUnitDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String subUnitIdStr = request.getParameter("id");
        if (subUnitIdStr != null && !subUnitIdStr.isEmpty()) {
            try {
                int subUnitId = Integer.parseInt(subUnitIdStr);
                SubUnit subUnit = dao.getSubUnitById(subUnitId);
                if (subUnit != null) {
                    request.setAttribute("subunit", subUnit);
                    request.setAttribute("action", "detail");
                    request.setAttribute("entity", "subunit");
                } else {
                    setErrorMessage(request, "Subunit not found with ID: " + subUnitId);
                }
            } catch (NumberFormatException e) {
                setErrorMessage(request, "Invalid subunit ID: " + subUnitIdStr);
            }
        } else {
            setErrorMessage(request, "Subunit ID not provided");
        }
        loadAllData(request, response);
    }

    private void handleCheckConversions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String subUnitIdStr = request.getParameter("id");
        if (subUnitIdStr != null && !subUnitIdStr.isEmpty()) {
            try {
                int subUnitId = Integer.parseInt(subUnitIdStr);
                List<UnitConversion> conversions = dao.getUnitConversionsBySubUnitId(subUnitId);
                int countConversions = conversions != null ? conversions.size() : 0;
                SubUnit subUnit = dao.getSubUnitById(subUnitId);

                if (subUnit != null) {
                    request.setAttribute("countConversions", countConversions);
                    request.setAttribute("subunit", subUnit);
                    request.setAttribute("entity", "subunit");
                    request.setAttribute("action", "confirmDeactivateSubunit");
                } else {
                    setErrorMessage(request, "Subunit not found with ID: " + subUnitId);
                }
            } catch (NumberFormatException e) {
                setErrorMessage(request, "Invalid subunit ID: " + subUnitIdStr);
            }
        } else {
            setErrorMessage(request, "Subunit ID not provided");
        }
        loadAllData(request, response);
    }

    private void handleSubUnitPostAction(String action, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (action) {
            case "add":
                handleSubUnitAdd(request, response);
                break;
            case "update":
                handleSubUnitUpdate(request, response);
                break;
            case "delete":
                handleSubUnitDelete(request, response);
                break;
            case "confirmDeactivateSubunit":
                handleConfirmDeactivateSubunit(request, response);
                break;
            default:
                setErrorMessage(request, "Invalid action: " + action);
                loadAllData(request, response);
                break;
        }
    }

    private void handleSubUnitAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String status = request.getParameter("status");

        if (name == null || name.trim().isEmpty()) {
            setErrorMessage(request, "Please enter the subunit name");
            loadAllData(request, response);
            return;
        }
        if (status == null || (!status.equals("active") && !status.equals("inactive"))) {
            setErrorMessage(request, "Invalid status");
            loadAllData(request, response);
            return;
        }

        SubUnit subUnit = new SubUnit();
        subUnit.setName(name.trim());
        subUnit.setStatus(status);

        try {
            dao.createSubUnit(subUnit);
            setSuccessMessage(request, "Subunit added successfully");
            response.sendRedirect("unit");
        } catch (Exception e) {
            logError("handleSubUnitAdd", e);
            setErrorMessage(request, "Error adding subunit: " + e.getMessage());
            loadAllData(request, response);
        }
    }

    private void handleSubUnitUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String subUnitIdStr = request.getParameter("subUnitId");
        String name = request.getParameter("name");
        String status = request.getParameter("status");
        String originalStatus = request.getParameter("originalStatus");

        if (subUnitIdStr == null || subUnitIdStr.isEmpty() || name == null || name.trim().isEmpty()) {
            setErrorMessage(request, "Please provide subunit ID and name");
            loadAllData(request, response);
            return;
        }
        if (status == null || (!status.equals("active") && !status.equals("inactive"))) {
            setErrorMessage(request, "Invalid status");
            loadAllData(request, response);
            return;
        }

        try {
            int subUnitId = Integer.parseInt(subUnitIdStr);
            SubUnit subUnit = dao.getSubUnitById(subUnitId);
            if (subUnit == null) {
                setErrorMessage(request, "Subunit not found with ID: " + subUnitId);
                loadAllData(request, response);
                return;
            }

            if ("active".equals(originalStatus) && "inactive".equals(status)) {
                List<UnitConversion> conversions = dao.getUnitConversionsBySubUnitId(subUnitId);
                int countConversions = conversions != null ? conversions.size() : 0;
                request.setAttribute("countConversions", countConversions);
                request.setAttribute("subunit", subUnit);
                request.setAttribute("entity", "subunit");
                request.setAttribute("action", "confirmDeactivateSubunit");
                loadAllData(request, response);
                return;
            }

            subUnit.setName(name.trim());
            subUnit.setStatus(status);
            dao.updateSubUnit(subUnit);
            setSuccessMessage(request, "Subunit updated successfully");
            response.sendRedirect("unit");
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Invalid subunit ID: " + subUnitIdStr);
            loadAllData(request, response);
        } catch (Exception e) {
            logError("handleSubUnitUpdate", e);
            setErrorMessage(request, "Error updating subunit: " + e.getMessage());
            loadAllData(request, response);
        }
    }

    private void handleSubUnitDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String subUnitIdStr = request.getParameter("id");
        if (subUnitIdStr == null || subUnitIdStr.isEmpty()) {
            setErrorMessage(request, "Subunit ID not provided");
            loadAllData(request, response);
            return;
        }

        try {
            int subUnitId = Integer.parseInt(subUnitIdStr);
            boolean success = dao.deleteSubUnit(subUnitId);
            if (!success) {
                setErrorMessage(request, "Cannot delete subunit with ID " + subUnitId + ". Subunit may not exist or is in use.");
                loadAllData(request, response);
            } else {
                setSuccessMessage(request, "Subunit deleted successfully");
                response.sendRedirect("unit");
            }
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Invalid subunit ID: " + subUnitIdStr);
            loadAllData(request, response);
        } catch (Exception e) {
            logError("handleSubUnitDelete", e);
            setErrorMessage(request, "Error deleting subunit: " + e.getMessage());
            loadAllData(request, response);
        }
    }

    private void handleConfirmDeactivateSubunit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String subUnitIdStr = request.getParameter("subUnitId");
        String newStatus = request.getParameter("newStatus");

        if (subUnitIdStr == null || subUnitIdStr.isEmpty() || newStatus == null || !newStatus.equals("inactive")) {
            setErrorMessage(request, "Invalid parameters for deactivation");
            loadAllData(request, response);
            return;
        }

        try {
            int subUnitId = Integer.parseInt(subUnitIdStr);
            SubUnit subUnit = dao.getSubUnitById(subUnitId);
            if (subUnit == null) {
                setErrorMessage(request, "Subunit not found with ID: " + subUnitId);
                loadAllData(request, response);
                return;
            }

            dao.deleteUnitConversionsBySubUnitId(subUnitId);
            subUnit.setStatus("inactive");
            dao.updateSubUnit(subUnit);
            setSuccessMessage(request, "Subunit deactivated successfully");
            response.sendRedirect("unit");
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Invalid subunit ID: " + subUnitIdStr);
            loadAllData(request, response);
        } catch (Exception e) {
            logError("handleConfirmDeactivateSubunit", e);
            setErrorMessage(request, "Error deactivating subunit: " + e.getMessage());
            loadAllData(request, response);
        }
    }

    // UnitConversion Actions
    private void handleConversionAction(String action, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (action) {
            case "edit":
                handleConversionEdit(request, response);
                break;
            case "detail":
                handleConversionDetail(request, response);
                break;
            default:
                loadAllData(request, response);
                break;
        }
    }

    private void handleConversionEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String conversionIdStr = request.getParameter("id");
        if (conversionIdStr != null && !conversionIdStr.isEmpty()) {
            try {
                int conversionId = Integer.parseInt(conversionIdStr);
                UnitConversion conversion = dao.getUnitConversionById(conversionId);
                if (conversion != null) {
                    request.setAttribute("conversion", conversion);
                    request.setAttribute("action", "edit");
                    request.setAttribute("entity", "conversion");
                } else {
                    setErrorMessage(request, "Unit conversion not found with ID: " + conversionId);
                }
            } catch (NumberFormatException e) {
                setErrorMessage(request, "Invalid unit conversion ID: " + conversionIdStr);
            }
        } else {
            setErrorMessage(request, "Unit conversion ID not provided");
        }
        loadAllData(request, response);
    }

    private void handleConversionDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String conversionIdStr = request.getParameter("id");
        if (conversionIdStr != null && !conversionIdStr.isEmpty()) {
            try {
                int conversionId = Integer.parseInt(conversionIdStr);
                UnitConversion conversion = dao.getUnitConversionById(conversionId);
                if (conversion != null) {
                    request.setAttribute("conversion", conversion);
                    request.setAttribute("action", "detail");
                    request.setAttribute("entity", "conversion");
                } else {
                    setErrorMessage(request, "Unit conversion not found with ID: " + conversionId);
                }
            } catch (NumberFormatException e) {
                setErrorMessage(request, "Invalid unit conversion ID: " + conversionIdStr);
            }
        } else {
            setErrorMessage(request, "Unit conversion ID not provided");
        }
        loadAllData(request, response);
    }

    private void handleConversionPostAction(String action, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (action) {
            case "add":
                handleConversionAdd(request, response);
                break;
            case "update":
                handleConversionUpdate(request, response);
                break;
            case "delete":
                handleConversionDelete(request, response);
                break;
            default:
                setErrorMessage(request, "Invalid action: " + action);
                loadAllData(request, response);
                break;
        }
    }

    private void handleConversionAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String unitIdStr = request.getParameter("unitId");
        String subUnitIdStr = request.getParameter("subUnitId");
        String factorStr = request.getParameter("factor");

        if (unitIdStr == null || subUnitIdStr == null || factorStr == null ||
            unitIdStr.isEmpty() || subUnitIdStr.isEmpty() || factorStr.isEmpty()) {
            setErrorMessage(request, "Please provide all unit conversion details");
            loadAllData(request, response);
            return;
        }

        try {
            int unitId = Integer.parseInt(unitIdStr);
            int subUnitId = Integer.parseInt(subUnitIdStr);
            double factor = Double.parseDouble(factorStr);

            if (factor <= 0) {
                setErrorMessage(request, "Conversion factor must be greater than 0");
                loadAllData(request, response);
                return;
            }

            UnitConversion conversion = new UnitConversion();
            conversion.setUnitId(unitId);
            conversion.setSubUnitId(subUnitId);
            conversion.setFactor(factor);

            dao.createUnitConversion(conversion);
            setSuccessMessage(request, "Unit conversion added successfully");
            response.sendRedirect("unit");
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Invalid data: Unit ID, Subunit ID, or factor is not in correct format");
            loadAllData(request, response);
        } catch (Exception e) {
            logError("handleConversionAdd", e);
            setErrorMessage(request, "Error adding unit conversion: " + e.getMessage());
            loadAllData(request, response);
        }
    }

    private void handleConversionUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String conversionIdStr = request.getParameter("unitConversionId");
        String unitIdStr = request.getParameter("unitId");
        String subUnitIdStr = request.getParameter("subUnitId");
        String factorStr = request.getParameter("factor");

        if (conversionIdStr == null || unitIdStr == null || subUnitIdStr == null || factorStr == null ||
            conversionIdStr.isEmpty() || unitIdStr.isEmpty() || subUnitIdStr.isEmpty() || factorStr.isEmpty()) {
            setErrorMessage(request, "Please provide all unit conversion details");
            loadAllData(request, response);
            return;
        }

        try {
            int conversionId = Integer.parseInt(conversionIdStr);
            int unitId = Integer.parseInt(unitIdStr);
            int subUnitId = Integer.parseInt(subUnitIdStr);
            double factor = Double.parseDouble(factorStr);

            if (factor <= 0) {
                setErrorMessage(request, "Conversion factor must be greater than 0");
                loadAllData(request, response);
                return;
            }

            UnitConversion conversion = new UnitConversion();
            conversion.setUnitConversionId(conversionId);
            conversion.setUnitId(unitId);
            conversion.setSubUnitId(subUnitId);
            conversion.setFactor(factor);

            dao.updateUnitConversion(conversion);
            setSuccessMessage(request, "Unit conversion updated successfully");
            response.sendRedirect("unit");
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Invalid data: Conversion ID, Unit ID, Subunit ID, or factor is not in correct format");
            loadAllData(request, response);
        } catch (Exception e) {
            logError("handleConversionUpdate", e);
            setErrorMessage(request, "Error updating unit conversion: " + e.getMessage());
            loadAllData(request, response);
        }
    }

    private void handleConversionDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String conversionIdStr = request.getParameter("id");
        if (conversionIdStr == null || conversionIdStr.isEmpty()) {
            setErrorMessage(request, "Unit conversion ID not provided");
            loadAllData(request, response);
            return;
        }

        try {
            int conversionId = Integer.parseInt(conversionIdStr);
            boolean success = dao.deleteUnitConversion(conversionId);
            if (!success) {
                setErrorMessage(request, "Cannot delete unit conversion with ID " + conversionId + ". Conversion may not exist.");
                loadAllData(request, response);
            } else {
                setSuccessMessage(request, "Unit conversion deleted successfully");
                response.sendRedirect("unit");
            }
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Invalid unit conversion ID: " + conversionIdStr);
            loadAllData(request, response);
        } catch (Exception e) {
            logError("handleConversionDelete", e);
            setErrorMessage(request, "Error deleting unit conversion: " + e.getMessage());
            loadAllData(request, response);
        }
    }

    private void logError(String method, Exception e) {
        System.err.println("UnitServlet Error in " + method + ": " + e.getMessage());
        e.printStackTrace();
    }

    private void handleClearMessages(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("successMsg");
        session.removeAttribute("errorMsg");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public String getServletInfo() {
        return "Handles CRUD operations for Units, SubUnits, and UnitConversions";
    }
}