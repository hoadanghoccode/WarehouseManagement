/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

/**
 *
 * @author ADMIN
 */
public class Category {
    private int categoryId;
    private String name;
    private int subCategoryCount;
    private List<SubCategory> subCategories;

    public Category() {
    }

    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public Category(int categoryId, String name, int subCategoryCount, List<SubCategory> subCategories) {
        this.categoryId = categoryId;
        this.name = name;
        this.subCategoryCount = subCategoryCount;
        this.subCategories = subCategories;
    }
    
    
    public Category(int categoryId, String name, int subCategoryCount) {
        this.categoryId = categoryId;
        this.name = name;
        this.subCategoryCount = subCategoryCount;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    
    
    public int getSubCategoryCount() {
        return subCategoryCount;
    }

    public void setSubCategoryCount(int subCategoryCount) {
        this.subCategoryCount = subCategoryCount;
    }
    
    

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" + "categoryId=" + categoryId + ", name=" + name + ", subCategoryCount=" + subCategoryCount + ", subCategories=" + subCategories + '}';
    }

  

  
    
    
}
