package controller;

import dal.InventoryHistoryDAO;
import model.InventoryHistory;
import model.MaterialInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class InventoryHistoryServlet extends HttpServlet {
    private static final int PAGE_SIZE = 7; // Updated to 7

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Log raw parameters for debugging
            System.out.println("Raw materialId: " + request.getParameter("materialId"));
            System.out.println("Raw subUnitId: " + request.getParameter("subUnitId"));

            // Validate materialId and subUnitId parameters
            String materialIdParam = request.getParameter("materialId");
            String subUnitIdParam = request.getParameter("subUnitId");
            if (materialIdParam == null || materialIdParam.trim().isEmpty() || 
                subUnitIdParam == null || subUnitIdParam.trim().isEmpty()) {
                request.setAttribute("errorMsg", "Material ID and Subunit ID are required.");
                request.getRequestDispatcher("/inventoryhistory.jsp").forward(request, response);
                return;
            }

            int materialId, subUnitId;
            try {
                materialId = Integer.parseInt(materialIdParam);
                subUnitId = Integer.parseInt(subUnitIdParam);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMsg", "Invalid material ID or subunit ID format.");
                request.getRequestDispatcher("/inventoryhistory.jsp").forward(request, response);
                return;
            }

            // Initialize DAO
            InventoryHistoryDAO dao = new InventoryHistoryDAO();

            // Verify materialId and subUnitId exist in database
            if (!dao.isMaterialExists(materialId)) {
                request.setAttribute("errorMsg", "Material ID " + materialId + " does not exist.");
                request.getRequestDispatcher("/inventoryhistory.jsp").forward(request, response);
                return;
            }
            if (!dao.isSubUnitExists(subUnitId)) {
                request.setAttribute("errorMsg", "Subunit ID " + subUnitId + " does not exist.");
                request.getRequestDispatcher("/inventoryhistory.jsp").forward(request, response);
                return;
            }

            // Get and validate other parameters
            String dateRange = request.getParameter("dateRange") != null ? request.getParameter("dateRange") : "7";
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String transactionType = request.getParameter("transactionType") != null ? request.getParameter("transactionType") : "all";
            String sortBy = request.getParameter("sortBy") != null ? request.getParameter("sortBy") : "date_desc";
            String totalPeriod = request.getParameter("totalPeriod") != null ? request.getParameter("totalPeriod") : "7";
            String totalStartDate = request.getParameter("totalStartDate");
            String totalEndDate = request.getParameter("totalEndDate");
            int page;
            try {
                page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }

            // Log parameters
            System.out.println("Parameters: materialId=" + materialId + ", subUnitId=" + subUnitId + 
                              ", dateRange=" + dateRange + ", startDate=" + startDate + 
                              ", endDate=" + endDate + ", transactionType=" + transactionType + 
                              ", sortBy=" + sortBy + ", page=" + page +
                              ", totalPeriod=" + totalPeriod + ", totalStartDate=" + totalStartDate + ", totalEndDate=" + totalEndDate);

            // Calculate date range for history
            LocalDate endDateObj = LocalDate.now(); // 2025-06-24
            LocalDate startDateObj;
            if ("custom".equals(dateRange) && startDate != null && endDate != null) {
                try {
                    startDateObj = LocalDate.parse(startDate);
                    endDateObj = LocalDate.parse(endDate);
                    if (startDateObj.isAfter(endDateObj)) {
                        request.setAttribute("errorMsg", "Start date cannot be after end date.");
                        request.getRequestDispatcher("/inventoryhistory.jsp").forward(request, response);
                        return;
                    }
                } catch (DateTimeParseException e) {
                    request.setAttribute("errorMsg", "Invalid date format for custom range.");
                    request.getRequestDispatcher("/inventoryhistory.jsp").forward(request, response);
                    return;
                }
            } else {
                int days = switch (dateRange) {
                    case "30" -> 30;
                    case "180" -> 180;
                    case "365" -> 365;
                    default -> 7;
                };
                startDateObj = endDateObj.minusDays(days);
            }

            System.out.println("Date range for history: " + startDateObj + " to " + endDateObj);

            // Calculate custom total period
            LocalDate totalEndDateObj = LocalDate.now(); // 2025-06-24
            LocalDate totalStartDateObj = null;
            if ("custom_total".equals(totalPeriod) && totalStartDate != null && totalEndDate != null) {
                try {
                    totalStartDateObj = LocalDate.parse(totalStartDate);
                    totalEndDateObj = LocalDate.parse(totalEndDate);
                    if (totalStartDateObj.isAfter(totalEndDateObj)) {
                        request.setAttribute("errorMsg", "Total start date cannot be after total end date.");
                        request.getRequestDispatcher("/inventoryhistory.jsp").forward(request, response);
                        return;
                    }
                } catch (DateTimeParseException e) {
                    request.setAttribute("errorMsg", "Invalid date format for custom total range.");
                    request.getRequestDispatcher("/inventoryhistory.jsp").forward(request, response);
                    return;
                }
            } else {
                int days = switch (totalPeriod) {
                    case "30" -> 30;
                    case "180" -> 180;
                    default -> 7;
                };
                totalStartDateObj = totalEndDateObj.minusDays(days);
            }

            System.out.println("Total period range: " + totalStartDateObj + " to " + totalEndDateObj);

            // Fetch material info
            MaterialInfo materialInfo = dao.getMaterialInfo(materialId);
            if (materialInfo == null) {
                request.setAttribute("errorMsg", "Material information not found.");
                request.getRequestDispatcher("/inventoryhistory.jsp").forward(request, response);
                return;
            }

            // Fetch total import/export quantities
            double totalImportQty = dao.getTotalImportQty(materialId);
            double totalExportQty = dao.getTotalExportQty(materialId);

            // Fetch history list with pagination
            List<InventoryHistory> historyList = dao.getFilteredInventoryHistories(
                materialId, subUnitId, startDateObj.toString(), endDateObj.toString(), 
                transactionType, sortBy, page, PAGE_SIZE
            );
            int totalRecords = dao.getTotalRecords(materialId, subUnitId, startDateObj.toString(), endDateObj.toString(), transactionType);
            int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

            // Fetch custom total import/export and latest quantities
            Map<String, Double> customTotals = dao.getTotalImportExportByPeriod(materialId, subUnitId, totalPeriod, totalStartDateObj.toString(), totalEndDateObj.toString());
            double customTotalImport = customTotals.get("totalImport");
            double customTotalExport = customTotals.get("totalExport");
            InventoryHistory latestHistory = dao.getLatestHistoryByDate(materialId, subUnitId, totalStartDateObj.toString(), totalEndDateObj.toString());
            double customLatestAvailable = (latestHistory != null) ? latestHistory.getAvailableQty() : 0.0;
            double customLatestNotAvailable = (latestHistory != null) ? latestHistory.getNotAvailableQty() : 0.0;

            System.out.println("History list size: " + historyList.size() + ", Total records: " + totalRecords + ", Total pages: " + totalPages);
            System.out.println("Custom Total Import: " + customTotalImport + ", Custom Total Export: " + customTotalExport);
            System.out.println("Custom Latest Available: " + customLatestAvailable + ", Custom Latest Not Available: " + customLatestNotAvailable);

            // Set attributes
            request.setAttribute("materialInfo", materialInfo);
            request.setAttribute("totalImportQty", totalImportQty);
            request.setAttribute("totalExportQty", totalExportQty);
            request.setAttribute("historyList", historyList);
            request.setAttribute("dateRange", dateRange);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.setAttribute("filteredStartDate", startDateObj.toString());
            request.setAttribute("filteredEndDate", endDateObj.toString());
            request.setAttribute("transactionType", transactionType);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("materialId", materialId);
            request.setAttribute("subUnitId", subUnitId);
            request.setAttribute("totalPeriod", totalPeriod);
            request.setAttribute("totalStartDate", totalStartDateObj.toString());
            request.setAttribute("totalEndDate", totalEndDateObj.toString());
            request.setAttribute("customTotalImport", customTotalImport);
            request.setAttribute("customTotalExport", customTotalExport);
            request.setAttribute("customLatestAvailable", customLatestAvailable);
            request.setAttribute("customLatestNotAvailable", customLatestNotAvailable);

            // Forward to JSP
            request.getRequestDispatcher("/inventoryhistory.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Servlet error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMsg", "An unexpected error occurred while fetching inventory history.");
            request.getRequestDispatcher("/inventoryhistory.jsp").forward(request, response);
        }
    }
}