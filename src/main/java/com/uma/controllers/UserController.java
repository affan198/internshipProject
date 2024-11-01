package com.uma.controllers;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uma.entities.Expense;
import com.uma.entities.User;
import com.uma.service.ExpenseService;
import com.uma.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private ExpenseService expenseService;

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute User user, Model model) {
		String email = user.getEmail();
		boolean isEmpty = userService.isUserEmpty(email);
		if (isEmpty) {
			userService.registerUser(user);
			model.addAttribute("success", "User registered successfuly. please LogIn!");
			return "login";
		} else {
			model.addAttribute("error", "this email Id is already registered!");
			return "register";
		}

	}

	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}

	@PostMapping("/login")
	public String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session,
			Model model) {
		Optional<User> user = userService.loginUser(email, password);
		if (user.isPresent()) {
			session.setAttribute("loggedInUser", user.get());
			return "redirect:/user/dashboard";
		}
		model.addAttribute("error", "Invalid email or password");
		return "login";
	}

	@GetMapping("/dashboard")
	public String userDashboard(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "redirect:/user/login";
		}
		model.addAttribute("expenses", expenseService.getUserExpenses(loggedInUser));
		model.addAttribute("loggedInUser", loggedInUser);
		return "dashboard";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // Clears the session
		return "redirect:/user/login";
	}

	@GetMapping("/view")
	public String viewProfile(HttpSession session, Model model) {
		User user = (User) session.getAttribute("loggedInUser");
		if (user == null) {
			return "redirect:/user/login";
		}
		model.addAttribute("user", user);
		return "viewProfile";
	}

	@GetMapping("/edit")
	public String editProfileForm(HttpSession session, Model model) {
		User user = (User) session.getAttribute("loggedInUser");
		if (user == null) {
			return "redirect:/user/login";
		}
		model.addAttribute("user", user);
		return "editProfile";
	}

	@PostMapping("/edit")
	public String editProfile(@ModelAttribute User updatedUser, HttpSession session) {
		User user = (User) session.getAttribute("loggedInUser");
		if (user == null) {
			return "redirect:/user/login";
		}
		userService.updateUser(updatedUser);
		session.setAttribute("loggedInUser", updatedUser); // Update session info
		return "redirect:/user/dashboard";
	}

	@GetMapping("/reportingPage")
	public String gotoReportingPage(HttpSession session, Model model) {

		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "redirect:/user/login";
		}
		
		model.addAttribute("loggedInUser", loggedInUser);
		long user_id = loggedInUser.getId();
		
		long totalExpenseForUser = expenseService.countExpenseForUser(user_id);
		model.addAttribute("totalExpenseForUser", totalExpenseForUser);
		
		long totalExpenseCountCurrentMonth = expenseService.totalExpenseCountCurrentMonthThisUser(user_id);
		model.addAttribute("totalExpenseCountCurrentMonth", totalExpenseCountCurrentMonth);
		
		long monthExpenseAmountThisUser = expenseService.currentMonthExpenseAmountThisUser(user_id);
		model.addAttribute("monthExpenseAmountThisUser", monthExpenseAmountThisUser);
		
		return "userReportPage";
	}
	
	@GetMapping("/currentMonthExpenses")
	public String currentMonthExpenses(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "redirect:/user/login";
		}
		
		model.addAttribute("loggedInUser", loggedInUser);
		long user_id = loggedInUser.getId();
		List<Expense> monthExpensesThisUser = expenseService.currentMonthExpensesThisUser(user_id);
		
		model.addAttribute("monthExpensesThisUser", monthExpensesThisUser);
		
		return "userMonthlyExpenses";
	}
	
	@GetMapping("/monthlyExpenses")
	public String monthlyExpenses(@RequestParam Date fromDate, @RequestParam Date toDate,HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "redirect:/user/login";
		}
		
		model.addAttribute("loggedInUser", loggedInUser);
		long user_id = loggedInUser.getId();
		
		List<Expense> monthExpensesThisUser = expenseService.monthlyExpensesThisUser(user_id, fromDate, toDate);
		model.addAttribute("monthExpensesThisUser", monthExpensesThisUser);
		
		return "userMonthlyExpenses";
	}

	@GetMapping("/delete")
	public String deleteProfile(HttpSession session) {
		User user = (User) session.getAttribute("loggedInUser");
		if (user != null) {
			userService.deleteUser(user.getId());
			session.invalidate();
		}
		return "redirect:/user/login";
	}

}
