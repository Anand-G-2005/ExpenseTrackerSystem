package com.expensetracker;

import java.time.LocalDate;

public class Expense {
    private int id;
    private LocalDate date;
    private double amount;
    private String categoryName;
    private String description;

    public Expense(int id, LocalDate date, double amount,
                   String categoryName, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.categoryName = categoryName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toFileString() {
        return id + "|" + date + "|" + amount + "|"
                + categoryName + "|" + description;
    }

    public static Expense fromFileString(String line) {
        String[] parts = line.split("\\|", -1);

        if (parts.length != 5) {
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0]);
            LocalDate date = LocalDate.parse(parts[1]);
            double amount = Double.parseDouble(parts[2]);

            return new Expense(id, date, amount, parts[3], parts[4]);
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d | Date: %s | Amount: %.2f | Category: %s | Description: %s",
                id, date, amount, categoryName, description);
    }
}
