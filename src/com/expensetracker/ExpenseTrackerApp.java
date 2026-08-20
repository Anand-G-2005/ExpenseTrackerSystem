package com.expensetracker;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ExpenseTrackerApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CategoryService categoryService = new CategoryService();
    private static final ExpenseService expenseService = new ExpenseService();

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("     EXPENSE TRACKER SYSTEM");
        System.out.println("=================================");

        boolean running = true;

        while (running) {
            showMainMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    expenseService.viewExpenses();
                    break;
                case 3:
                    manageCategories();
                    break;
                case 4:
                    filterExpenses();
                    break;
                case 5:
                    editExpense();
                    break;
                case 6:
                    deleteExpense();
                    break;
                case 7:
                    showReports();
                    break;
                case 8:
                    running = false;
                    System.out.println("Thank you for using Expense Tracker.");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1 to 8.");
            }
        }

        scanner.close();
    }

    private static void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Add Expense");
        System.out.println("2. View All Expenses");
        System.out.println("3. Manage Categories");
        System.out.println("4. Filter Expenses");
        System.out.println("5. Edit Expense");
        System.out.println("6. Delete Expense");
        System.out.println("7. View Reports");
        System.out.println("8. Exit");
    }

    private static void addExpense() {
        System.out.println("\n--- Add Expense ---");

        LocalDate date = readDate("Enter date (yyyy-MM-dd): ");
        double amount = readPositiveAmount("Enter amount: ");

        categoryService.viewCategories();
        String category = readText("Enter an existing category name: ");

        if (!categoryService.categoryExists(category)) {
            System.out.println("Category does not exist. Create it first.");
            return;
        }

        String description = readText("Enter description: ");

        expenseService.addExpense(date, amount, category, description);
    }

    private static void editExpense() {
        System.out.println("\n--- Edit Expense ---");
        expenseService.viewExpenses();

        int id = readInt("Enter expense ID to edit: ");
        LocalDate date = readDate("Enter new date (yyyy-MM-dd): ");
        double amount = readPositiveAmount("Enter new amount: ");

        categoryService.viewCategories();
        String category = readText("Enter new category name: ");

        if (!categoryService.categoryExists(category)) {
            System.out.println("Category does not exist. Expense was not updated.");
            return;
        }

        String description = readText("Enter new description: ");

        if (expenseService.updateExpense(id, date, amount, category, description)) {
            System.out.println("Expense updated successfully.");
        } else {
            System.out.println("Expense ID not found.");
        }
    }

    private static void deleteExpense() {
        System.out.println("\n--- Delete Expense ---");
        expenseService.viewExpenses();

        int id = readInt("Enter expense ID to delete: ");

        if (expenseService.deleteExpense(id)) {
            System.out.println("Expense deleted successfully.");
        } else {
            System.out.println("Expense ID not found.");
        }
    }

    private static void manageCategories() {
        boolean managingCategories = true;

        while (managingCategories) {
            System.out.println("\n--- Category Menu ---");
            System.out.println("1. Add Category");
            System.out.println("2. View Categories");
            System.out.println("3. Edit Category");
            System.out.println("4. Delete Category");
            System.out.println("5. Back to Main Menu");

            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    String name = readText("Enter category name: ");
                    categoryService.addCategory(name);
                    break;
                case 2:
                    categoryService.viewCategories();
                    break;
                case 3:
                    editCategory();
                    break;
                case 4:
                    deleteCategory();
                    break;
                case 5:
                    managingCategories = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1 to 5.");
            }
        }
    }

    private static void editCategory() {
        categoryService.viewCategories();

        int id = readInt("Enter category ID to edit: ");
        String oldName = categoryService.getCategoryNameById(id);

        if (oldName == null) {
            System.out.println("Category ID not found.");
            return;
        }

        String newName = readText("Enter new category name: ");

        if (categoryService.updateCategory(id, newName)) {
            expenseService.changeCategoryNameInExpenses(oldName, newName);
            System.out.println("Category updated successfully.");
        }
    }

    private static void deleteCategory() {
        categoryService.viewCategories();

        int id = readInt("Enter category ID to delete: ");
        String categoryName = categoryService.getCategoryNameById(id);

        if (categoryName == null) {
            System.out.println("Category ID not found.");
            return;
        }

        if (expenseService.hasExpensesForCategory(categoryName)) {
            System.out.println("This category has expenses. Delete or edit those expenses first.");
            return;
        }

        if (categoryService.deleteCategory(id)) {
            System.out.println("Category deleted successfully.");
        }
    }

    private static void filterExpenses() {
        System.out.println("\n--- Filter Expenses ---");
        System.out.println("1. Filter by Date Range");
        System.out.println("2. Filter by Category");
        System.out.println("3. Filter by Amount Range");

        int choice = readInt("Choose an option: ");

        switch (choice) {
            case 1:
                LocalDate startDate = readDate("Enter start date (yyyy-MM-dd): ");
                LocalDate endDate = readDate("Enter end date (yyyy-MM-dd): ");

                if (startDate.isAfter(endDate)) {
                    System.out.println("Start date cannot be after end date.");
                    return;
                }

                expenseService.filterByDateRange(startDate, endDate);
                break;

            case 2:
                String category = readText("Enter category name: ");
                expenseService.filterByCategory(category);
                break;

            case 3:
                double minimumAmount = readPositiveOrZeroAmount("Enter minimum amount: ");
                double maximumAmount = readPositiveOrZeroAmount("Enter maximum amount: ");

                if (minimumAmount > maximumAmount) {
                    System.out.println("Minimum amount cannot be greater than maximum amount.");
                    return;
                }

                expenseService.filterByAmountRange(minimumAmount, maximumAmount);
                break;

            default:
                System.out.println("Invalid option.");
        }
    }

    private static void showReports() {
        System.out.println("\n--- Reports ---");
        System.out.println("1. Monthly Expense Report");
        System.out.println("2. Category-wise Expense Report");

        int choice = readInt("Choose an option: ");

        switch (choice) {
            case 1:
                YearMonth month = readMonth("Enter month (yyyy-MM): ");
                expenseService.showMonthlyReport(month);
                break;
            case 2:
                expenseService.showCategoryWiseReport();
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readPositiveAmount(String message) {
        while (true) {
            double amount = readPositiveOrZeroAmount(message);

            if (amount > 0) {
                return amount;
            }

            System.out.println("Amount must be greater than zero.");
        }
    }

    private static double readPositiveOrZeroAmount(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();

            try {
                double amount = Double.parseDouble(input);

                if (amount >= 0) {
                    return amount;
                }

                System.out.println("Amount cannot be negative.");
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    private static LocalDate readDate(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();

            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException exception) {
                System.out.println("Invalid date. Use yyyy-MM-dd, for example 2026-08-20.");
            }
        }
    }

    private static YearMonth readMonth(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();

            try {
                return YearMonth.parse(input);
            } catch (DateTimeParseException exception) {
                System.out.println("Invalid month. Use yyyy-MM, for example 2026-08.");
            }
        }
    }

    private static String readText(String message) {
        while (true) {
            System.out.print(message);
            String text = scanner.nextLine().trim();

            if (text.isEmpty()) {
                System.out.println("This value cannot be empty.");
            } else if (text.contains("|")) {
                System.out.println("Do not use the | symbol.");
            } else {
                return text;
            }
        }
    }
}
