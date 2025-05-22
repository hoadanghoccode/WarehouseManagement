/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import java.util.List;
import model.Category;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public class CategoryDAO extends DBContext {

    //GET ALL CATEGORY
//    public List<Category> getAllCategory() {
//        List<Category> list = new ArrayList<>();
//        String query = "SELECT * FROM Category ORDER BY Name";
//        try {
//            PreparedStatement ps = connection.prepareStatement(query);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Category cat = new Category();
//                cat.setCategoryId(rs.getInt("Category_id"));
//                cat.setName(rs.getString("Name"));
//                list.add(cat);
//            }
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//        return list;
//    }
//    public List<Category> getAllCategory() {
//        List<Category> list = new ArrayList<>();
//        String query = "SELECT * FROM Category ORDER BY Name";
//        try {
//            PreparedStatement ps = connection.prepareStatement(query);
//            ResultSet rs = ps.executeQuery();
//            Map<Integer, Category> categoryMap = new HashMap<>();
//
//            // Đọc tất cả vào map
//            while (rs.next()) {
//                int id = rs.getInt("Category_id");
//                String name = rs.getString("Name");
//                Integer parentId = rs.getObject("Parent_id") != null ? rs.getInt("Parent_id") : null;
//
//                Category cat = new Category();
//                cat.setCategoryId(id);
//                cat.setName(name);
//                cat.setParentId(null); // Gán sau
//                cat.setSubCategories(new ArrayList<>());
//
//                categoryMap.put(id, cat);
//            }
//
//            // Gán quan hệ cha–con
//            for (Category cat : categoryMap.values()) {
//                Integer parentId = getParentIdFromDB(cat.getCategoryId());
//                if (parentId != null && categoryMap.containsKey(parentId)) {
//                    Category parent = categoryMap.get(parentId);
//                    cat.setParentId(parent);
//                    parent.getSubCategories().add(cat);
//                } else {
//                    list.add(cat); // Nếu không có cha thì là gốc
//                }
//            }
//
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//        return list;
//    }
    
    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM Category ORDER BY Name";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            Map<Integer, Category> categoryMap = new HashMap<>();
            Map<Integer, Integer> parentIdMap = new HashMap<>(); // Lưu parentId tạm

            // Đọc tất cả category vào map
            while (rs.next()) {
                int id = rs.getInt("Category_id");
                String name = rs.getString("Name");
                Integer parentId = rs.getObject("Parent_id") != null ? rs.getInt("Parent_id") : null;

                Category cat = new Category();
                cat.setCategoryId(id);
                cat.setName(name);
                cat.setSubCategories(new ArrayList<>()); // chuẩn bị list con
                cat.setParentId(null); // tạm thời null, gán sau

                categoryMap.put(id, cat);
                parentIdMap.put(id, parentId);
            }

            // Gán quan hệ cha - con dựa vào parentIdMap
            for (Map.Entry<Integer, Category> entry : categoryMap.entrySet()) {
                int id = entry.getKey();
                Category cat = entry.getValue();
                Integer parentId = parentIdMap.get(id);

                if (parentId != null && categoryMap.containsKey(parentId)) {
                    Category parent = categoryMap.get(parentId);
                    cat.setParentId(parent);
                    parent.getSubCategories().add(cat);
                } else {
                    // Nếu không có cha, thêm vào list gốc
                    list.add(cat);
                }
            }

        } catch (SQLException e) {
            System.out.println("getAllCategory error: " + e.getMessage());
        }
        return list;
    }

    private Integer getParentIdFromDB(int categoryId) {
        String query = "SELECT Parent_id FROM Category WHERE Category_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getObject("Parent_id") != null ? rs.getInt("Parent_id") : null;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Category> getParentCategories() {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM Category WHERE Parent_id IS NULL ORDER BY Name";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category cat = new Category();
                cat.setCategoryId(rs.getInt("Category_id"));
                cat.setName(rs.getString("Name"));
                cat.setParentId(null); // là cha thì không có cha
                list.add(cat);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Category> getSubCategories() {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM Category WHERE Parent_id IS NOT NULL ORDER BY Name";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Category_id");
                String name = rs.getString("Name");
                int parentIdVal = rs.getInt("Parent_id");
                Category parent = getCategoryById(parentIdVal);

                Category cat = new Category(id, name, parent);
                list.add(cat);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

//    public Category getCategoryById(int id) {
//        String query = "SELECT * FROM Category c WHERE c.Category_id = ?";
//        try {
//            PreparedStatement ps = connection.prepareStatement(query);
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Category cat = new Category();
//                cat.setCategoryId(rs.getInt("Category_id"));
//                cat.setName(rs.getString("Name"));
//                return cat;
//            }
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//        return null;
//    }
    public Category getCategoryById(int id) {
        String query = "SELECT * FROM Category WHERE Category_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int categoryId = rs.getInt("Category_id");
                String name = rs.getString("Name");
                Integer parentIdVal = rs.getObject("Parent_id") != null ? rs.getInt("Parent_id") : null;

                Category parentCategory = parentIdVal != null ? getCategoryById(parentIdVal) : null;
                return new Category(categoryId, name, parentCategory);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public void insertCategory(String name, Integer parentId) {
        String query = "INSERT INTO Category (Name, Parent_id) VALUES (?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            if (parentId != null) {
                ps.setInt(2, parentId);
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

//    public void insertCategory(String name) {
//        String query = "INSERT INTO Category (Name) VALUES (?)";
//        try {
//            PreparedStatement ps = connection.prepareStatement(query);
//            ps.setString(1, name);
//            ps.executeUpdate();
//
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//    }
//    public void deleteCategory(int id) {
//        String query = "DELETE FROM Category WHERE Category_id = ?";
//        try {
//            PreparedStatement ps = connection.prepareStatement(query);
//            ps.setInt(1, id);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//    }
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

//    public List<Category> searchCategoryByName(String name) {
//        List<Category> list = new ArrayList<>();
//        String query = "SELECT * FROM Category WHERE Name LIKE ?";
//        try {
//            PreparedStatement ps = connection.prepareStatement(query);
//            ps.setString(1, "%" + name + "%");
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                int id = rs.getInt("Category_id");
//                String categoryName = rs.getString("Name");
//                Category category = new Category(id, categoryName);
//                list.add(category);
//            }
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//        return list;
//    }
    public List<Category> searchCategoryByName(String name) {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM Category WHERE Name LIKE ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Category_id");
                String categoryName = rs.getString("Name");
                Integer parentIdVal = rs.getObject("Parent_id") != null ? rs.getInt("Parent_id") : null;
                Category parent = parentIdVal != null ? getCategoryById(parentIdVal) : null;
                list.add(new Category(id, categoryName, parent));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public void updateCategory(Category category) {
        String query = "UPDATE Category SET Name = ?, Parent_id = ? WHERE Category_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, category.getName());

            // Nếu là category cha (không có parent) thì setNull
            if (category.getParentId() == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, category.getParentId().getCategoryId());
            }

            ps.setInt(3, category.getCategoryId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update category error: " + e.getMessage());
        }
    }

    public List<Category> getListByPage(ArrayList<Category> list, int start, int end) {
        ArrayList<Category> arr = new ArrayList<>();
        for (int i = start; i < end; i++) {
            arr.add(list.get(i));
        }
        return arr;
    }

    public static void main(String[] args) {
//        List<Category> list = new CategoryDAO().getAllCategory();
//        Category list = new CategoryDAO().insertCategory('Khoan');
        CategoryDAO cd = new CategoryDAO();
        List<Category> list = cd.getAllCategory();
        System.out.println(list);

//        cd.deleteCategory(3);
//        list = cd.getAllCategory();
//        System.out.println(list);
    }
}
