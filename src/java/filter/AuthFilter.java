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

        // Write code here to process the request and/or response before
        // the rest of the filter chain is invoked.
        // For example, a logging filter might log items on the request object,
        // such as the parameters.
        /*
	for (Enumeration en = request.getParameterNames(); en.hasMoreElements(); ) {
	    String name = (String)en.nextElement();
	    String values[] = request.getParameterValues(name);
	    int n = values.length;
	    StringBuffer buf = new StringBuffer();
	    buf.append(name);
	    buf.append("=");
	    for(int i=0; i < n; i++) {
	        buf.append(values[i]);
	        if (i < n-1)
	            buf.append(",");
	    }
	    log(buf.toString());
	}
         */
    }    
    
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("AuthFilter:DoAfterProcessing");
        }

        // Write code here to process the request and/or response after
        // the rest of the filter chain is invoked.
        // For example, a logging filter might log the attributes on the
        // request object after the request has been processed. 
        /*
	for (Enumeration en = request.getAttributeNames(); en.hasMoreElements(); ) {
	    String name = (String)en.nextElement();
	    Object value = request.getAttribute(name);
	    log("attribute: " + name + "=" + value.toString());

	}
         */
        // For example, a filter might append something to the response.
        /*
	PrintWriter respOut = new PrintWriter(response.getWriter());
	respOut.println("<P><B>This has been appended by an intrusive filter.</B>");
         */
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        this.dao = new AuthenDAO();
    }
    
    
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
      // Ở đây bạn ép kiểu nếu muốn dùng HttpServletRequest/Response
        System.out.println("=== Filter is running ===");
        HttpServletRequest  req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath(); // VD: "/WarehouseManagement"
        System.out.println("Current URI: " + uri);
        System.out.println("Context Path: " + contextPath);

        // 1. Cho phép truy cập trang login (login.jsp, LoginController) mà không cần session
        boolean isLoginPage = uri.equals(contextPath + "/login.jsp")
                           || uri.equals(contextPath + "/LoginController")
                           || uri.equals(contextPath + "/login")
                           || uri.equals(contextPath + "/login-google");
        System.out.println("Is Login Page: " + isLoginPage);

        // 2. Cho phép truy cập file tĩnh (CSS, JS, ảnh, v.v.) để giao diện login cũng hiển thị đúng.
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

        HttpSession session = req.getSession(false);
        System.out.println("Session exists: " + (session != null));
        Users user = (session == null) ? null : (Users) session.getAttribute("USER");
        System.out.println("User in session: " + (user != null));

        // 3. Nếu chưa login và không phải trang login, không phải file tĩnh => bắt redirect về login
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
                    
                    // Thêm log để xem permissions
                    System.out.println("=== User Permissions ===");
                    System.out.println("User ID: " + user.getUserId());
                    System.out.println("Role ID: " + roleId);
                    for (Map.Entry<String, Boolean> entry : perms.entrySet()) {
                        System.out.println(entry.getKey() + " = " + entry.getValue());
                    }
                    System.out.println("=====================");
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // Nếu cần, chuyển sang trang lỗi hoặc ghi log
                }
            }
        }

        // 5. Cho phép request tiếp tục được xử lý bởi servlet/JSP kế tiếp
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
