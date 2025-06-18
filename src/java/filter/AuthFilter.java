/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package filter;

import dal.AuthenDAO;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.Map;
import model.Users;

/**
 *
 * @author PC
 */
public class AuthFilter implements Filter {

    private static final boolean debug = true;
    private AuthenDAO dao;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public AuthFilter() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("AuthFilter:DoBeforeProcessing");
        }
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("AuthFilter:DoAfterProcessing");
        }

    }

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.dao = new AuthenDAO();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        // Ép kiểu sang HttpServletRequest/HttpServletResponse
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        System.out.println("=== Filter is running ===");

        // Lấy URI và contextPath
        String uri = req.getRequestURI();                 // Ví dụ: /YourApp/warehouses
        String contextPath = req.getContextPath();        // Ví dụ: /YourApp
        System.out.println("Current URI: " + uri);
        System.out.println("Context Path: " + contextPath);

        // 1. Cho phép truy cập trang login (login.jsp, LoginController, endpoint login)
        boolean isLoginPage = uri.equals(contextPath + "/login.jsp")
                || uri.equals(contextPath + "/LoginController")
                || uri.equals(contextPath + "/login")
                || uri.equals(contextPath + "/indexInventory.jsp")
                || uri.equals(contextPath + "/403.jsp")                                
                || uri.equals(contextPath + "/adminresetlist")
                || uri.equals(contextPath + "/resetpassword")
                || uri.equals(contextPath + "/changepassword")
                || uri.equals(contextPath + "/login-google");
        System.out.println("Is Login Page: " + isLoginPage);

        // 2. Cho phép truy cập file tĩnh (CSS, JS, ảnh, font, v.v.)
        boolean isStaticResource = uri.startsWith(contextPath + "/css/")
                || uri.startsWith(contextPath + "/js/")
                || uri.startsWith(contextPath + "/images/")
                || uri.endsWith(".css")
                || uri.endsWith(".js")
                || uri.endsWith(".png")
                || uri.endsWith(".jpg")
                || uri.endsWith(".jpeg")
                || uri.endsWith(".gif")
                || uri.endsWith(".woff")
                || uri.endsWith(".ttf");
        System.out.println("Is Static Resource: " + isStaticResource);

        // Lấy session hiện tại (nếu có)
        HttpSession session = req.getSession(false);
        System.out.println("Session exists: " + (session != null));

        // Lấy đối tượng user từ session
        Users user = (session == null) ? null : (Users) session.getAttribute("USER");
        System.out.println("User in session: " + (user != null));

        // 3. Nếu chưa login và không phải trang login, không phải file tĩnh → redirect về login
        if (user == null && !isLoginPage && !isStaticResource) {
            System.out.println("Redirecting to login page");
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        // 4. Nếu đã login, load permissions vào session (chỉ load 1 lần)
        if (user != null) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
            if (perms == null) {
                try {
                    int roleId = user.getRoleId();
                    perms = dao.getPermissionsByRoleId(roleId);
                    session.setAttribute("PERMISSIONS", perms);

                    // Log ra bộ quyền để debug
                    System.out.println("=== User Permissions ===");
                    System.out.println("User ID: " + user.getUserId());
                    System.out.println("Role ID: " + roleId);
                    for (Map.Entry<String, Boolean> entry : perms.entrySet()) {
                        System.out.println(entry.getKey() + " = " + entry.getValue());
                    }
                    System.out.println("========================");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // Nếu lỗi DB, bạn có thể redirect sang trang lỗi chung hoặc 500
                    resp.sendRedirect(contextPath + "/error.jsp");
                    return;
                }
            }
        }

        //check department
        if (user != null && uri.startsWith(contextPath + "/department")) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
            Boolean canViewDepartment = (perms != null) ? perms.get("Department_VIEW") : null;

            System.out.println("Checking INVENTORY_VIEW permission: " + canViewDepartment);
            if (canViewDepartment == null || !canViewDepartment) {
                session.invalidate();
                resp.sendRedirect(contextPath + "/403.jsp");
                return;
            }
            // Nếu có quyền VIEW, cho đi tiếp vào Servlet / JSP xử lý
        }

        //Check user
        if (user != null && uri.startsWith(contextPath + "/userlist")) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
            Boolean canViewCustomer = (perms != null) ? perms.get("Customer_VIEW") : null;

            if (canViewCustomer == null || !canViewCustomer) {
                session.invalidate();
                resp.sendRedirect(contextPath + "/403.jsp");
                return;
            }
            // Nếu có quyền VIEW, cho đi tiếp vào Servlet / JSP xử lý
        }

        //check permission
        if (user != null && uri.startsWith(contextPath + "/resource")) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
            Boolean canViewPermission = (perms != null) ? perms.get("Permission_VIEW") : null;

            System.out.println("Checking INVENTORY_VIEW permission: " + canViewPermission);
            if (canViewPermission == null || !canViewPermission) {
                session.invalidate();
                resp.sendRedirect(contextPath + "/403.jsp");
                return;
            }
            // Nếu có quyền VIEW, cho đi tiếp vào Servlet / JSP xử lý
        }

        //check role
        if (user != null && uri.startsWith(contextPath + "/permissionrole")) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
            Boolean canViewRole = (perms != null) ? perms.get("Role_VIEW") : null;

            System.out.println("Checking INVENTORY_VIEW permission: " + canViewRole);
            if (canViewRole == null || !canViewRole) {
                session.invalidate();
                resp.sendRedirect(contextPath + "/403.jsp");
                return;
            }
            // Nếu có quyền VIEW, cho đi tiếp vào Servlet / JSP xử lý
        }

        //check view material
        if (user != null && uri.startsWith(contextPath + "/list-material")) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
            Boolean canViewMaterial = (perms != null) ? perms.get("Material_VIEW") : null;

            if (canViewMaterial == null || !canViewMaterial) {
                session.invalidate();
                resp.sendRedirect(contextPath + "/403.jsp");
                return;
            }
            // Nếu có quyền VIEW, cho đi tiếp vào Servlet / JSP xử lý
        }

        //check add material
        if (user != null && uri.startsWith(contextPath + "/add-material")) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
            Boolean canAddMaterial = (perms != null) ? perms.get("Material_ADD") : null;

            if (canAddMaterial == null || !canAddMaterial) {
                session.invalidate();
                resp.sendRedirect(contextPath + "/403.jsp");
                return;
            }
            // Nếu có quyền VIEW, cho đi tiếp vào Servlet / JSP xử lý
        }
        
        //check update material
        if (user != null && uri.startsWith(contextPath + "/update-material")) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
            Boolean canUpdateMaterial = (perms != null) ? perms.get("Material_UPDATE") : null;

            if (canUpdateMaterial == null || !canUpdateMaterial) {
                session.invalidate();
                resp.sendRedirect(contextPath + "/403.jsp");
                return;
            }
            // Nếu có quyền VIEW, cho đi tiếp vào Servlet / JSP xử lý
        }
        
        
         //check warehouse
        if (user != null && uri.startsWith(contextPath + "/inventory")) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
            Boolean canViewWarehouse = (perms != null) ? perms.get("Warehouse_VIEW") : null;

            if (canViewWarehouse == null || !canViewWarehouse) {
                session.invalidate();
                resp.sendRedirect(contextPath + "/403.jsp");
                return;
            }
            // Nếu có quyền VIEW, cho đi tiếp vào Servlet / JSP xử lý
        }

        //check category
        if (user != null && uri.startsWith(contextPath + "/categorylist")) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
            Boolean canViewCategory = (perms != null) ? perms.get("Category_VIEW") : null;

            if (canViewCategory == null || !canViewCategory) {
                session.invalidate();
                resp.sendRedirect(contextPath + "/403.jsp");
                return;
            }
            // Nếu có quyền VIEW, cho đi tiếp vào Servlet / JSP xử lý
        }
        //check update material
        if (user != null && uri.startsWith(contextPath + "/update-material")) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
            Boolean canUpdateMaterial = (perms != null) ? perms.get("Material_UPDATE") : null;

            if (canUpdateMaterial == null || !canUpdateMaterial) {
                session.invalidate();
                resp.sendRedirect(contextPath + "/403.jsp");
                return;
            }
            // Nếu có quyền VIEW, cho đi tiếp vào Servlet / JSP xử lý
        }
        
        //check update material
        if (user != null && uri.startsWith(contextPath + "/adminresetlist")) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> perms = (Map<String, Boolean>) session.getAttribute("PERMISSIONS");
            Boolean canViewAdminReset = (perms != null) ? perms.get("Password_VIEW") : null;

            if (canViewAdminReset == null || !canViewAdminReset) {
                session.invalidate();
                resp.sendRedirect(contextPath + "/403.jsp");
                return;
            }
            // Nếu có quyền VIEW, cho đi tiếp vào Servlet / JSP xử lý
        }
        chain.doFilter(request, response);
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AuthFilter()");
        }
        StringBuffer sb = new StringBuffer("AuthFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
