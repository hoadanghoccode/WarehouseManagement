/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Permission;

/**
 *
 * @author PC
 */
public class AuthenDAO {

    private Connection conn;

    public AuthenDAO() {
        // Dùng DBContext để lấy connection
        conn = new DBContext().getConnection();
    }
    
    public List<Permission> getPermission() throws SQLException {
        List<Permission> list = new ArrayList<>();

        String sql =
            "SELECT " +
            "  rr.Resource_role_id      AS id, " +
            "  res.Name                AS resourceName, " +
            "  rl.Name                 AS roleName, " +
            "  rr.Can_add              AS canAdd, " +
            "  rr.Can_view             AS canRead, " +
            "  rr.Can_update           AS canUpdate, " +
            "  rr.Can_delete           AS canDelete, " +
            "  rr.Created_at           AS createdAt " +
            "FROM Resource_role rr " +
            "INNER JOIN Resource res ON rr.Resource_id = res.Resource_id " +
            "INNER JOIN Role     rl  ON rr.Role_id     = rl.Role_id;";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Permission p = new Permission();
                p.setId(rs.getInt("id"));
                p.setResourceName(rs.getString("resourceName"));
//                p.setRoleId(rs.getInt("roleName")); // nếu cần lưu roleId, bạn có thể thêm trường vào model
                p.setCanCreate(rs.getBoolean("canAdd"));    // map canAdd -> canCreate
                p.setCanRead  (rs.getBoolean("canRead"));   // map canView -> canRead
                p.setCanUpdate(rs.getBoolean("canUpdate")); // map canUpdate -> canUpdate
                p.setCanDelete(rs.getBoolean("canDelete")); // map canDelete -> canDelete
                // Nếu muốn lưu createdAt, thêm trường Date createdAt vào model Permission và map:
                // p.setCreatedAt(rs.getTimestamp("createdAt"));
                list.add(p);
            }
        }

        return list;
    }
    

}
