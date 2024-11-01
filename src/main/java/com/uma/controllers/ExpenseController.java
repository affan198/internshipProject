package com.uma.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.uma.entities.Expense;
import com.uma.entities.User;
import com.uma.service.ExpenseService;
import com.uma.service.FileStorageService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/expense")
public class ExpenseController {
	@Autowired
	private ExpenseService expenseService;

	@Autowired
	private FileStorageService fileStorageService;

	// Show the form for adding a new expense
	@GetMapping("/add")
	public String showExpenseForm(Model model) {
		model.addAttribute("expense", new Expense());
		return "expenseForm";
	}

	@PostMapping("/add")
	public String addExpense(@ModelAttribute Expense expense, HttpSession session, @RequestParam MultipartFile file) {
	    User user = (User) session.getAttribute("loggedInUser");
	    if (user == null) {
	        return "redirect:/user/login"; // Redirect to login if user is not logged in
	    }
	    expense.setUser(user);

	    String filePath = fileStorageService.storeFile(file);
	    String originalFilename = file.getOriginalFilename();
	    System.err.println(originalFilename);
	    expense.setFileName(file.getOriginalFilename());
	    expense.setFilePath(filePath);
	   
	    expenseService.saveExpense(expense);

	    
	    return "redirect:/user/dashboard";
	}


	// Show the edit form with existing expense data
	@GetMapping("/edit/{id}")
	public String showEditExpenseForm(@PathVariable Long id, Model model, HttpSession session) {
		User user = (User) session.getAttribute("loggedInUser");
		if (user == null) {
			return "redirect:/user/login";
		}

		Expense expense = expenseService.getExpenseById(id);
		if (expense == null || !expense.getUser().getId().equals(user.getId())) {
			return "redirect:/user/dashboard"; // Redirect if expense doesn't belong to the logged-in user
		}

		model.addAttribute("expense", expense); // Ensure this line is correctly populating the expense
		return "expenseForm"; // Reuse the same form for adding and editing
	}

	// Update the expense with new data
	@PostMapping("/edit/{id}")
	public String updateExpense(@PathVariable Long id, @ModelAttribute Expense expense, HttpSession session, @RequestParam MultipartFile file) {
		User user = (User) session.getAttribute("loggedInUser");
		if (user == null) {
			return "redirect:/user/login";
		}

		expense.setUser(user); // Set the user to ensure correct ownership
		

	    String filePath = fileStorageService.storeFile(file);
	    String originalFilename = file.getOriginalFilename();
	    System.err.println(originalFilename);
	    expense.setFileName(file.getOriginalFilename());
	    expense.setFilePath(filePath);
	   
		
		expenseService.saveExpense(expense); // Save the updated expense
		return "redirect:/user/dashboard";
	}

	// Delete an expense by ID
	@GetMapping("/delete/{id}") 
	public String deleteExpense(@PathVariable Long id, HttpSession session) {
		User user = (User) session.getAttribute("loggedInUser");
		if (user == null) {
			return "redirect:/user/login";
		}

		Expense expense = expenseService.getExpenseById(id);
		if (expense != null && expense.getUser().getId().equals(user.getId())) {
			expenseService.deleteExpense(id); // Only delete if the user owns the expense
		}
		return "redirect:/user/dashboard";
	}
}
