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
    private Category parentId;
    private int subCategoryCount;
    private List<Category> subCategories;
    private int materialCount;

    public Category() {
    }
    
     public Category(int categoryId, String name, Category parentId) {
        this.categoryId = categoryId;
        this.name = name;
        this.parentId = parentId;
    }

    public Category(int categoryId, String name, Category parentId, int subCategoryCount, List<Category> subCategories) {
        this.categoryId = categoryId;
        this.name = name;
        this.parentId = parentId;
        this.subCategoryCount = subCategoryCount;
        this.subCategories = subCategories;
    }

    public Category(int categoryId, String name, Category parentId, int subCategoryCount, List<Category> subCategories, int materialCount) {
        this.categoryId = categoryId;
        this.name = name;
        this.parentId = parentId;
        this.subCategoryCount = subCategoryCount;
        this.subCategories = subCategories;
        this.materialCount = materialCount;
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

    public Category getParentId() {
        return parentId;
    }

    public void setParentId(Category parentId) {
        this.parentId = parentId;
    }

    public int getSubCategoryCount() {
        return subCategoryCount;
    }

    public void setSubCategoryCount(int subCategoryCount) {
        this.subCategoryCount = subCategoryCount;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public int getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(int materialCount) {
        this.materialCount = materialCount;
    }
    
    

    @Override
    public String toString() {
        return "Category{" + "categoryId=" + categoryId + ", name=" + name + ", parentId=" + parentId + ", subCategoryCount=" + subCategoryCount + ", subCategories=" + subCategories + '}';
    }


}
