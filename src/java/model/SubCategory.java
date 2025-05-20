/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ADMIN
 */
public class SubCategory {
    private int subCategoryId;
    private Category category;
    private String name;

    public SubCategory() {
    }

    public SubCategory(int subCategoryId, Category category, String name) {
        this.subCategoryId = subCategoryId;
        this.category = category;
        this.name = name;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SubCategory{" + "subCategoryId=" + subCategoryId + ", category=" + category + ", name=" + name + '}';
    }

    
}
