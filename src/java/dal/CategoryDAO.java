package dal;

import java.util.ArrayList;
import java.util.List;
import model.Category;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDAO extends DBContext {

    public Category getCategoryById(int id) {
        String query = "SELECT * FROM Category WHERE Category_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category cate = new Category();
                cate.setCategoryId(rs.getInt("Category_id"));
                cate.setName(rs.getString("Name"));
                cate.setParentId(getCategoryById(rs.getInt("Parent_id")));
                cate.setStatus(rs.getString("Status"));
                return cate;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Category> getSubCategoryByParentId(int pid) {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM Category WHERE Parent_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, pid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category cate = new Category();
                cate.setCategoryId(rs.getInt("Category_id"));
                cate.setName(rs.getString("Name"));
                cate.setParentId(getCategoryById(rs.getInt("Parent_id")));
                cate.setStatus(rs.getString("Status"));
                list.add(cate);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Category> getAllParentCategory(String status) {
        List<Category> list = new ArrayList<>();
        String query = "SELECT c.Category_id AS Parent_id, c.Name AS Parent_Name, c.Status, "
                + "COUNT(sc.Category_id) AS SubCateNum "
                + "FROM Category c "
                + "LEFT JOIN Category sc ON sc.Parent_id = c.Category_id "
                + "WHERE c.Parent_id IS NULL ";

        // Nếu có truyền filter status thì thêm điều kiện
        if (status != null && !status.isEmpty()) {
            query += " AND c.Status = ? ";
        }

        query += "GROUP BY c.Category_id, c.Name, c.Status";

        try {
            PreparedStatement ps = connection.prepareStatement(query);

            if (status != null && !status.isEmpty()) {
                ps.setString(1, status);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category cate = new Category();
                cate.setCategoryId(rs.getInt("Parent_id"));
                cate.setName(rs.getString("Parent_Name"));
                cate.setParentId(null);
                cate.setSubCategories(getSubCategoryByParentId(rs.getInt("Parent_id")));
                cate.setSubCategoryCount(rs.getInt("SubCateNum"));
                cate.setStatus(rs.getString("Status"));
                list.add(cate);
            }
        } catch (SQLException e) {
            System.out.println("getAllParentCategory error: " + e.getMessage());
        }
        return list;
    }

    public Category getParentCategoryById(int categoryId) {
        String query = "SELECT c.Category_id AS Parent_id, c.Name AS Parent_Name, c.Status,"
                + " COUNT(sc.Category_id) AS SubCateNum "
                + "FROM Category c "
                + "LEFT JOIN Category sc ON sc.Parent_id = c.Category_id "
                + "WHERE c.Parent_id IS NULL AND c.Category_id = ? "
                + "GROUP BY c.Category_id, c.Name, c.Status";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Category cate = new Category();
                cate.setCategoryId(rs.getInt("Parent_id"));
                cate.setName(rs.getString("Parent_Name"));
                cate.setParentId(null);
                cate.setSubCategories(getSubCategoryByParentId(rs.getInt("Parent_id")));
                cate.setSubCategoryCount(rs.getInt("SubCateNum"));
                cate.setStatus(rs.getString("Status"));
                return cate;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Category> getAllSubCategory() {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM Category Where Parent_id IS NOT NULL";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category cate = new Category();
                cate.setCategoryId(rs.getInt("Category_id"));
                cate.setName(rs.getString("Name"));
                cate.setParentId(getCategoryById(rs.getInt("Parent_id")));
                cate.setStatus(rs.getString("Status"));
                list.add(cate);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return list;
    }

    public boolean insertCategory(String name, Integer parentId) {
        String query = "INSERT INTO Category (Name, Parent_id) VALUES (?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            if (parentId != null) {
                ps.setInt(2, parentId);
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất 1 dòng được thêm
        } catch (SQLException e) {
            System.out.println("Error inserting category: " + e.getMessage());
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

    public void deleteCategory(int id) {
        String query = "DELETE FROM Category WHERE Category_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public boolean updateCategory(Category category) {
        String query = "UPDATE Category SET Name = ?, Parent_id = ?, Status = ? WHERE Category_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, category.getName());

            if (category.getParentId() == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, category.getParentId().getCategoryId());
            }

            ps.setString(3, category.getStatus());
            ps.setInt(4, category.getCategoryId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất 1 dòng được cập nhật
        } catch (SQLException e) {
            System.out.println("Update category error: " + e.getMessage());
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

    public List<Category> searchCategoryByName(ArrayList<Category> parentList, String name) {
        List<Category> filteredParents = new ArrayList<>();

        for (Category cat : parentList) {
            if (cat.getName().toLowerCase().contains(name.toLowerCase())) {
                filteredParents.add(cat);
            }
        }

        return filteredParents;
    }

    public List<Category> getListByPage(ArrayList<Category> list, int start, int end) {
        ArrayList<Category> arr = new ArrayList<>();
        for (int i = start; i < end; i++) {
            if (i < list.size()) {
                arr.add(list.get(i));
            }
        }
        return arr;
    }

    public boolean isCircularParent(int categoryId, int newParentId) {
        String sql = """
        WITH RECURSIVE sub_tree AS (
            SELECT Category_id FROM Category WHERE Category_id = ?
            UNION ALL
            SELECT c.Category_id
            FROM Category c
            INNER JOIN sub_tree st ON c.Parent_id = st.Category_id
        )
        SELECT * FROM sub_tree WHERE Category_id = ?;
    """;

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, categoryId);     // category đang sửa
            st.setInt(2, newParentId);    // parent mới muốn gán

            ResultSet rs = st.executeQuery();
            return rs.next(); // Nếu có dòng nào → là vòng lặp
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Category";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category cate = new Category();
                cate.setCategoryId(rs.getInt("Category_id"));
                cate.setName(rs.getString("Name"));
                cate.setStatus(rs.getString("Status"));
                int parentId = rs.getInt("Parent_id");
                if (!rs.wasNull()) {
                    cate.setParentId(getCategoryById(parentId));
                } else {
                    cate.setParentId(null);
                }
                list.add(cate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Category> getIndentedCategories() {
        List<Category> all = getAllCategories();

        // Lọc chỉ những category active
//        List<Category> activeOnly = new ArrayList<>();
//        for (Category c : all) {
//            if ("active".equalsIgnoreCase(c.getStatus())) {
//                activeOnly.add(c);
//            }
//        }
        List<Category> result = new ArrayList<>();
        buildHierarchy(null, all, result);
        return result;
    }

    private void buildHierarchy(Category parent, List<Category> all, List<Category> result) {
        for (Category cat : all) {
            boolean isTop = (parent == null && cat.getParentId() == null);
            boolean isChild = (parent != null && cat.getParentId() != null /*Loại ra category cha*/
                    && cat.getParentId().getCategoryId() == parent.getCategoryId());

            if (isTop || isChild) {
                result.add(cat); // giữ nguyên tên gốc

                buildHierarchy(cat, all, result); // đệ quy con
            }
        }
    }

    //Bạn Giang tạo 2 hàm này để lấy Parent Category với list subCategory status Active của chúng. Đỡ phải sửa code trên bạn Minh <3
    public List<Category> getActiveSubCategoryByParentId(int parentId) {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM Category WHERE Parent_id = ? AND Status = 'active'";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, parentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category cate = new Category();
                cate.setCategoryId(rs.getInt("Category_id"));
                cate.setName(rs.getString("Name"));
                cate.setParentId(getCategoryById(rs.getInt("Parent_id")));
                cate.setStatus(rs.getString("Status"));
                list.add(cate);
            }
        } catch (SQLException e) {
            System.out.println("getActiveSubCategoryByParentId error: " + e.getMessage());
        }
        return list;
    }

    public List<Category> getAllParentCategoryWithActiveSubs(String status) {
        List<Category> list = new ArrayList<>();
        String query = "SELECT c.Category_id AS Parent_id, c.Name AS Parent_Name, c.Status, "
                + "COUNT(sc.Category_id) AS SubCateNum "
                + "FROM Category c "
                + "LEFT JOIN Category sc ON sc.Parent_id = c.Category_id "
                + "WHERE c.Parent_id IS NULL";

        if (status != null && !status.isEmpty()) {
            query += " AND c.Status = ?";
        }

        query += " GROUP BY c.Category_id, c.Name, c.Status";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            if (status != null && !status.isEmpty()) {
                ps.setString(1, status);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category cate = new Category();
                cate.setCategoryId(rs.getInt("Parent_id"));
                cate.setName(rs.getString("Parent_Name"));
                cate.setParentId(null);
                cate.setSubCategories(getActiveSubCategoryByParentId(rs.getInt("Parent_id"))); // <-- dùng hàm mới
                cate.setSubCategoryCount(rs.getInt("SubCateNum"));
                cate.setStatus(rs.getString("Status"));
                list.add(cate);
            }
        } catch (SQLException e) {
            System.out.println("getAllParentCategoryWithActiveSubs error: " + e.getMessage());
        }
        return list;
    }

    public static void main(String[] args) {

//        List<Category> list = new CategoryDAO().getAllCategory();
//        Category list = new CategoryDAO().insertCategory('Khoan');
        CategoryDAO dao = new CategoryDAO();
        System.out.println(dao.getAllParentCategory("active"));

    }
}
