package com.uma.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uma.entities.Expense;
import com.uma.entities.User;
import com.uma.repositories.ExpenseRepository;

import jakarta.transaction.Transactional;

@Service
public class IExpenseService implements ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getUserExpenses(User user) {
        return expenseRepository.findByUser(user);
    }
    
    @Override
    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
    
    @Transactional
    public int updateExpense(Long expenseId, Date date, String type, String location, Double amount) {
        return expenseRepository.updateExpenseDetails(expenseId, date, type, location, amount);
    }

	@Override
	public List<Expense> getAllExpenses() {
		
		return expenseRepository.findAll();
	}

	@Override
	public double currentMonthExpenseAmount() {
		Long expenseAmountCurrentMonth = expenseRepository.expenseAmountCurrentMonth();
		if(expenseAmountCurrentMonth == null) {
			return 0;
		}
		return expenseRepository.expenseAmountCurrentMonth();
	}

	@Override
	public long totalExpenseCurrentMonth() {

		return expenseRepository.countExpenses();
	}

	@Override
	public List<Expense> thisMonthExpenses() {
		List<Expense> expensesOfthisMonth = expenseRepository.expensesOfthisMonth();
		return expensesOfthisMonth;
	}

	@Override
	public List<Expense> findMonthlyExpenses(Date fromDate, Date toDate) {
		
		return expenseRepository.monthExpenses(fromDate, toDate);
	}

	@Override
	public long countExpenseForUser(long user_id) {
		return expenseRepository.countExpenseForUser(user_id);
	}

	@Override
	public long totalExpenseCountCurrentMonthThisUser(long user_id) {
		
		return expenseRepository.currentMonthExpenseCountForUser(user_id);
	}

	@Override
	public long currentMonthExpenseAmountThisUser(long user_id) {

		Long expenseAmountCurrentMonthThisUser = expenseRepository.expenseAmountCurrentMonthThisUser(user_id);
		if(expenseAmountCurrentMonthThisUser==null) {
			return 0;
		}
		
		return expenseRepository.expenseAmountCurrentMonthThisUser(user_id);
	}

	@Override
	public List<Expense> currentMonthExpensesThisUser(long user_id) {

		return expenseRepository.expensesCurrentMonthThisUser(user_id);
	}

	@Override
	public List<Expense> monthlyExpensesThisUser(long user_id, Date fromDate, Date toDate) {

		return expenseRepository.monthlyExpensesThisUser(user_id, fromDate, toDate);
	}

	
}