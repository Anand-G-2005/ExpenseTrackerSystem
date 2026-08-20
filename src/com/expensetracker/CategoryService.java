package com.expensetracker;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private final List<Category> categories;

    public CategoryService() {
        categories = new ArrayList<>();
        loadCategories();
    }

    public void addCategory(String name) {
        if (findCategoryByName(name) != null) {
            System.out.println("Category already exists.");
            return;
        }

        categories.add(new Category(getNextId(), name.trim()));
        saveCategories();
        System.out.println("Category added successfully.");
    }

    public void viewCategories() {
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
            return;
        }

        System.out.println("\n--- Categories ---");

        for (Category category : categories) {
            System.out.println(category);
        }
    }

    public boolean updateCategory(int id, String newName) {
        Category category = findCategoryById(id);

        if (category == null) {
            return false;
        }

        Category existingCategory = findCategoryByName(newName);

        if (existingCategory != null && existingCategory.getId() != id) {
            System.out.println("Another category already uses this name.");
            return false;
        }

        category.setName(newName.trim());
        saveCategories();
        return true;
    }

    public boolean deleteCategory(int id) {
        Category category = findCategoryById(id);

        if (category == null) {
            return false;
        }

        categories.remove(category);
        saveCategories();
        return true;
    }

    public boolean categoryExists(String name) {
        return findCategoryByName(name) != null;
    }

    public String getCategoryNameById(int id) {
        Category category = findCategoryById(id);

        if (category == null) {
            return null;
        }

        return category.getName();
    }

    private Category findCategoryById(int id) {
        for (Category category : categories) {
            if (category.getId() == id) {
                return category;
            }
        }

        return null;
    }

    private Category findCategoryByName(String name) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(name.trim())) {
                return category;
            }
        }

        return null;
    }

    private int getNextId() {
        int highestId = 0;

        for (Category category : categories) {
            if (category.getId() > highestId) {
                highestId = category.getId();
            }
        }

        return highestId + 1;
    }

    private void loadCategories() {
        List<String> lines = FileManager.readLines("categories.txt");

        for (String line : lines) {
            Category category = Category.fromFileString(line);

            if (category != null) {
                categories.add(category);
            }
        }
    }

    private void saveCategories() {
        List<String> lines = new ArrayList<>();

        for (Category category : categories) {
            lines.add(category.toFileString());
        }

        FileManager.writeLines("categories.txt", lines);
    }
}
