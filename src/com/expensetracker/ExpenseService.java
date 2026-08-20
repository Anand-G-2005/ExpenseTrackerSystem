package com.expensetracker;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExpenseService {
    private final List<Expense> expenses;

    public ExpenseService() {
        expenses = new ArrayList<>();
        loadExpenses();
    }

    public void addExpense(LocalDate date, double amount,
                           String categoryName, String description) {
        Expense expense = new Expense(
                getNextId(),
                date,
                amount,
                categoryName,
                description
        );

        expenses.add(expense);
        saveExpenses();

        System.out.println("Expense added successfully.");
    }

    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        System.out.println("\n--- All Expenses ---");
        displayExpenses(expenses);
    }

    public boolean updateExpense(int id, LocalDate date, double amount,
                                 String categoryName, String description) {
        Expense expense = findExpenseById(id);

        if (expense == null) {
            return false;
        }

        expense.setDate(date);
        expense.setAmount(amount);
        expense.setCategoryName(categoryName);
        expense.setDescription(description);
        saveExpenses();

        return true;
    }

    public boolean deleteExpense(int id) {
        Expense expense = findExpenseById(id);

        if (expense == null) {
            return false;
        }

        expenses.remove(expense);
        saveExpenses();

        return true;
    }

    public void filterByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Expense> filteredExpenses = new ArrayList<>();

        for (Expense expense : expenses) {
            boolean isAfterOrEqualStart = !expense.getDate().isBefore(startDate);
            boolean isBeforeOrEqualEnd = !expense.getDate().isAfter(endDate);

            if (isAfterOrEqualStart && isBeforeOrEqualEnd) {
                filteredExpenses.add(expense);
            }
        }

        System.out.println("\n--- Expenses in Selected Date Range ---");
        displayExpenses(filteredExpenses);
    }

    public void filterByCategory(String categoryName) {
        List<Expense> filteredExpenses = new ArrayList<>();

        for (Expense expense : expenses) {
            if (expense.getCategoryName().equalsIgnoreCase(categoryName)) {
                filteredExpenses.add(expense);
            }
        }

        System.out.println("\n--- Expenses for Category: " + categoryName + " ---");
        displayExpenses(filteredExpenses);
    }

    public void filterByAmountRange(double minimumAmount, double maximumAmount) {
        List<Expense> filteredExpenses = new ArrayList<>();

        for (Expense expense : expenses) {
            if (expense.getAmount() >= minimumAmount
                    && expense.getAmount() <= maximumAmount) {
                filteredExpenses.add(expense);
            }
        }

        System.out.println("\n--- Expenses in Selected Amount Range ---");
        displayExpenses(filteredExpenses);
    }

    public boolean hasExpensesForCategory(String categoryName) {
        for (Expense expense : expenses) {
            if (expense.getCategoryName().equalsIgnoreCase(categoryName)) {
                return true;
            }
        }

        return false;
    }

    public void changeCategoryNameInExpenses(String oldName, String newName) {
        for (Expense expense : expenses) {
            if (expense.getCategoryName().equalsIgnoreCase(oldName)) {
                expense.setCategoryName(newName);
            }
        }

        saveExpenses();
    }

    public void showMonthlyReport(YearMonth month) {
        double totalAmount = 0;
        List<Expense> monthlyExpenses = new ArrayList<>();

        for (Expense expense : expenses) {
            if (YearMonth.from(expense.getDate()).equals(month)) {
                monthlyExpenses.add(expense);
                totalAmount += expense.getAmount();
            }
        }

        System.out.println("\n--- Monthly Report: " + month + " ---");
        displayExpenses(monthlyExpenses);
        System.out.printf("Total monthly expense: %.2f%n", totalAmount);
    }

    public void showCategoryWiseReport() {
        Map<String, Double> categoryTotals = new LinkedHashMap<>();

        for (Expense expense : expenses) {
            String category = expense.getCategoryName();
            double currentTotal = categoryTotals.getOrDefault(category, 0.0);
            categoryTotals.put(category, currentTotal + expense.getAmount());
        }

        if (categoryTotals.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        System.out.println("\n--- Category-wise Expense Report ---");

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            System.out.printf("%s: %.2f%n", entry.getKey(), entry.getValue());
        }
    }

    private void displayExpenses(List<Expense> expenseList) {
        if (expenseList.isEmpty()) {
            System.out.println("No matching expenses found.");
            return;
        }

        double totalAmount = 0;

        for (Expense expense : expenseList) {
            System.out.println(expense);
            totalAmount += expense.getAmount();
        }

        System.out.printf("Total: %.2f%n", totalAmount);
    }

    private Expense findExpenseById(int id) {
        for (Expense expense : expenses) {
            if (expense.getId() == id) {
                return expense;
            }
        }

        return null;
    }

    private int getNextId() {
        int highestId = 0;

        for (Expense expense : expenses) {
            if (expense.getId() > highestId) {
                highestId = expense.getId();
            }
        }

        return highestId + 1;
    }

    private void loadExpenses() {
        List<String> lines = FileManager.readLines("expenses.txt");

        for (String line : lines) {
            Expense expense = Expense.fromFileString(line);

            if (expense != null) {
                expenses.add(expense);
            }
        }
    }

    private void saveExpenses() {
        List<String> lines = new ArrayList<>();

        for (Expense expense : expenses) {
            lines.add(expense.toFileString());
        }

        FileManager.writeLines("expenses.txt", lines);
    }
}
