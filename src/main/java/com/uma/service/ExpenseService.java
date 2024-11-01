package com.uma.service;

import java.sql.Date;
import java.util.List;

import com.uma.entities.Expense;
import com.uma.entities.User;

public interface ExpenseService {

	public Expense saveExpense(Expense expense);

	public List<Expense> getUserExpenses(User user);

	public List<Expense> getAllExpenses();

	public Expense getExpenseById(Long id);

	public void deleteExpense(Long id);

	public int updateExpense(Long expenseId, Date date, String type, String location, Double amount);

	public double currentMonthExpenseAmount();

	public long totalExpenseCurrentMonth();

	public List<Expense> thisMonthExpenses();

	public List<Expense> findMonthlyExpenses(Date fromDate, Date toDate);

	public long countExpenseForUser(long user_id);

	public long totalExpenseCountCurrentMonthThisUser(long user_id);

	public long currentMonthExpenseAmountThisUser(long user_id);

	public List<Expense> currentMonthExpensesThisUser(long user_id);
	
	public List<Expense> monthlyExpensesThisUser(long user_id, Date fromDate, Date toDate);

}
