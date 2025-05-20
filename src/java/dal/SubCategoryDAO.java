/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import java.util.List;
import model.SubCategory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Category;

/**
 *
 * @author ADMIN
 */
public class SubCategoryDAO extends DBContext {

    public List<SubCategory> getAllSubCategory() {
        List<SubCategory> list = new ArrayList<>();
        String query = "SELECT * FROM Sub_Category";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategoryDAO cDAO = new CategoryDAO();
                SubCategory sc = new SubCategory();
                sc.setSubCategoryId(rs.getInt("Sub_category_id"));
                sc.setName(rs.getString("Name"));
                sc.setCategory(cDAO.getCategoryById(rs.getInt("Category_id")));
                list.add(sc);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<SubCategory> getSubCategoryByCategoryId(int cid) {
        List<SubCategory> list = new ArrayList<>();
        String query = "SELECT * FROM Sub_Category sc WHERE sc.Category_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, cid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategoryDAO cDAO = new CategoryDAO();
                SubCategory sc = new SubCategory();
                sc.setSubCategoryId(rs.getInt("Sub_Category_id"));
                sc.setName(rs.getString("Name"));
                sc.setCategory(cDAO.getCategoryById(rs.getInt("Category_id")));
                list.add(sc);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;

    }

    public int countSubCategoriesByCategoryId(int categoryId) {
        String query = "SELECT COUNT(*) FROM Sub_Category WHERE Category_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error counting subcategories: " + e.getMessage());
        }
        return 0;
    }

    public static void main(String[] args) {
        SubCategoryDAO scDAO = new SubCategoryDAO();
        List<SubCategory> list = scDAO.getSubCategoryByCategoryId(1);
        System.out.println(list);

    }
}
