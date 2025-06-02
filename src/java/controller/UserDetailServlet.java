package controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dal.UserDAO;
import model.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import model.Departmentt;

@MultipartConfig
public class UserDetailServlet extends HttpServlet {
    private Cloudinary cloudinary;

    @Override
    public void init() throws ServletException {
        super.init();
        cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dnzc9p3jj",
            "api_key", "199637772186358",
            "api_secret", "JcmmcMjcJxr7z8cTo1OSO759wNk"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("getDepartmentsByRole".equals(action)) {
            doGetDepartmentsByRole(request, response);
        } else {
            String idRaw = request.getParameter("id");
            if (idRaw == null || idRaw.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user ID");
                return;
            }
            try {
                int userId = Integer.parseInt(idRaw);
                if (userId <= 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
                    return;
                }
                UserDAO dao = new UserDAO();
                Users user = dao.getUserById(userId);
                if (user == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                    return;
                }
                System.out.println("User ID: " + userId + ", Date of Birth: " + (user.getDateOfBirth() != null ? user.getDateOfBirth().toString() : "null"));
                request.setAttribute("user", user);
                request.setAttribute("departments", dao.getDepartmentsByRoleId(user.getRoleId()));
                request.setAttribute("roles", dao.getAllRoles());
                request.setAttribute("userDepartmentId", dao.getUserDepartmentId(userId));
                request.getRequestDispatcher("userdetail.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                System.err.println("Invalid user ID format: " + idRaw);
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
            } catch (Exception e) {
                System.err.println("Error fetching user details for ID " + idRaw + ": " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
            }
        }
    }

    protected void doGetDepartmentsByRole(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String roleIdRaw = request.getParameter("roleId");
        if (roleIdRaw == null || roleIdRaw.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing role ID");
            return;
        }
        try {
            int roleId = Integer.parseInt(roleIdRaw);
            UserDAO dao = new UserDAO();
            List<Departmentt> departments = dao.getDepartmentsByRoleId(roleId);
            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < departments.size(); i++) {
                Departmentt department = departments.get(i);
                json.append("{");
                json.append("\"departmentId\":").append(department.getDepartmentId()
).append(",");
                json.append("\"name\":\"").append(department.getName().replace("\"", "\\\"")).append("\"");
                json.append("}");
                if (i < departments.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");
            PrintWriter out = response.getWriter();
            out.print(json.toString());
            out.flush();
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid role ID format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            if (userId <= 0) {
                throw new IllegalArgumentException("Invalid user ID");
            }
            String email = request.getParameter("email");
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }
            UserDAO dao = new UserDAO();
            Users existingUser = dao.getUserById(userId);
            if (existingUser == null) {
                throw new IllegalArgumentException("User not found");
            }
            String departmentIdRaw = request.getParameter("departmentId");
            int departmentId = 0;
            if (departmentIdRaw != null && !departmentIdRaw.trim().isEmpty()) {
                departmentId = Integer.parseInt(departmentIdRaw);
            }
            String roleIdRaw = request.getParameter("roleId");
            if (roleIdRaw == null || roleIdRaw.trim().isEmpty()) {
                throw new IllegalArgumentException("Role is required");
            }
            int roleId = Integer.parseInt(roleIdRaw);
            String phoneNumber = request.getParameter("phoneNumber");
            if (phoneNumber == null) {
                phoneNumber = "";
            } else if (!phoneNumber.isEmpty() && !phoneNumber.matches("^0\\d{9}$")) {
                throw new IllegalArgumentException("Phone number must start with 0, followed by exactly 9 digits");
            }
            String address = request.getParameter("address");
            if (address == null) {
                address = "";
            }
            String image = request.getParameter("image");
            if (image == null) {
                image = "";
            }
            String statusRaw = request.getParameter("status");
            if (statusRaw == null || (!statusRaw.equals("true") && !statusRaw.equals("false"))) {
                throw new IllegalArgumentException("Invalid status value");
            }
            boolean status = Boolean.parseBoolean(statusRaw);
            String dateOfBirthStr = request.getParameter("dateOfBirth");
            java.util.Date dateOfBirth = null;
            if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
                dateOfBirth = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirthStr);
            }

            // Handle file upload
            Part filePart = request.getPart("imageFile");
if (filePart != null && filePart.getSize() > 0) {
    try (InputStream fileContent = filePart.getInputStream()) {

        // Chuyển InputStream thành byte[]
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        while ((nRead = fileContent.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();

        byte[] fileBytes = buffer.toByteArray();

        // Upload fileBytes lên Cloudinary
        Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
            "resource_type", "auto"
        ));

        image = (String) uploadResult.get("url");

    } catch (Exception e) {
        System.err.println("Error uploading image to Cloudinary: " + e.getMessage());
        e.printStackTrace();
        session.setAttribute("error", "Image upload failed: " + e.getMessage());
    }
}

            Users user = new Users(userId, roleId, existingUser.getFullName(), email, existingUser.getPassword(),
                    existingUser.isGender(), phoneNumber, address, dateOfBirth, image, null,
                    new java.sql.Timestamp(System.currentTimeMillis()), status, null, null);
            dao.updateUser(user, departmentId);
            session.setAttribute("success", "User updated successfully");
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error updating user: " + e.getMessage());
            session.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Update failed: " + e.getMessage());
        }
        response.sendRedirect("userlist");
    }

    @Override
    public String getServletInfo() {
        return "User Detail Servlet";
    }
}