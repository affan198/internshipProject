package com.uma.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uma.entities.Expense;
import com.uma.entities.User;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	List<Expense> findByUser(User user);
	
	@Query("SELECT COUNT(e) FROM Expense e WHERE e.user.id = :user_id") 
	long countExpenseForUser(@Param("user_id") long user_id);
	
	@Query("SELECT COUNT(e) FROM Expense e WHERE e.user.id = :user_id AND (MONTH(e.date) = MONTH(CURRENT_DATE) AND YEAR(e.date) = YEAR(CURRENT_DATE))")
	long currentMonthExpenseCountForUser(@Param("user_id") long user_id);
	
	@Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :user_id AND (MONTH(e.date) = MONTH(CURRENT_DATE) AND YEAR(e.date) = YEAR(CURRENT_DATE))")
	Long expenseAmountCurrentMonthThisUser(@Param("user_id") long user_id);
	
	@Query("SELECT e FROM Expense e WHERE e.user.id = :user_id AND (MONTH(e.date) = MONTH(CURRENT_DATE) AND YEAR(e.date) = YEAR(CURRENT_DATE))")
	List<Expense> expensesCurrentMonthThisUser(long user_id);
	
	@Modifying
	@Query("UPDATE Expense e SET e.date = :date, e.type = :type, e.location = :location, e.amount = :amount WHERE e.id = :expenseId")
	int updateExpenseDetails(@Param("expenseId") Long espenseId, @Param("date") Date date, @Param("type") String type,
			@Param("location") String location, @Param("amount") Double amount);

	@Query("SELECT SUM(e.amount) FROM Expense e WHERE MONTH(e.date) = MONTH(CURRENT_DATE) AND YEAR(e.date) = YEAR(CURRENT_DATE)")
	Long expenseAmountCurrentMonth();

	@Query("SELECT COUNT(e) FROM Expense e WHERE MONTH(e.date) = MONTH(CURRENT_DATE) AND YEAR(e.date) = YEAR(CURRENT_DATE)")
	long countExpenses();
	
	@Query("SELECT e FROM Expense e WHERE MONTH(e.date) = MONTH(CURRENT_DATE) AND YEAR(e.date) = YEAR(CURRENT_DATE)")
	List<Expense> expensesOfthisMonth();
	
	@Query("SELECT e FROM Expense e WHERE e.date BETWEEN :fromDate AND :toDate")
	List<Expense> monthExpenses(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query("SELECT e FROM Expense e WHERE e.user.id =:user_id AND (e.date BETWEEN :fromDate AND :toDate)")
	List<Expense> monthlyExpensesThisUser(@Param("user_id") long user_id ,@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
}
