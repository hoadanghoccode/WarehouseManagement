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

/**
 *
 * @author ADMIN
 */
public class CategoryDAO extends DBContext {

    //GET ALL CATEGORY
    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM Category ORDER BY Name";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category cat = new Category();
                cat.setCategoryId(rs.getInt("Category_id"));
                cat.setName(rs.getString("Name"));
                list.add(cat);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public Category getCategoryById(int id) {
        String query = "SELECT * FROM Category c WHERE c.Category_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category cat = new Category();
                cat.setCategoryId(rs.getInt("Category_id"));
                cat.setName(rs.getString("Name"));
                return cat;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public void insertCategory(String name) {
        String query = "INSERT INTO Category (Name) VALUES (?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
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
                Category category = new Category(id, categoryName);
                list.add(category);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
    
    public List<Category> getListByPage(ArrayList<Category> list, int start, int end){
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
        List<Category> list = cd.searchCategoryByName("");
        System.out.println(list);

//        cd.deleteCategory(3);
//        list = cd.getAllCategory();
//        System.out.println(list);
    }
}
