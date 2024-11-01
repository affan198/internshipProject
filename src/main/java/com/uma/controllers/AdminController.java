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

import com.uma.entities.Admin;
import com.uma.entities.Expense;
import com.uma.service.AdminService;
import com.uma.service.ExpenseService;
import com.uma.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("admin", new Admin());
		return "adminRegister";
	}

	@PostMapping("/register")
	public String registerAdmin(@ModelAttribute Admin admin, Model model) {
		String email = admin.getEmail();
		boolean adminEmpty = adminService.isAdminEmpty(email);

		if (adminEmpty) {
			adminService.registerAdmin(admin);
			model.addAttribute("success", "Admin registered successfuly. please LogIn!");
			return "adminLogin";
		} else {
			model.addAttribute("error", "this email Id is already registered!");
			return "adminRegister";
		}

	}

	@GetMapping("/login")
	public String showLoginForm() {
		return "adminLogin";
	}

	@PostMapping("/login")
	public String loginAdmin(@RequestParam String email, @RequestParam String password, HttpSession session,
			Model model) {

		Optional<Admin> admin = adminService.adminLoginCredential(email, password);
		if (admin.isPresent()) {
			session.setAttribute("logedInAdmin", admin.get());
			return "adminDashboard";
		}
		model.addAttribute("error", "Invalid email or password");
		return "adminLogin";
	}

	@GetMapping("/dashboard")
	public String adminDashboard(HttpSession session, Model model) {
		Admin logedInAdmin = (Admin) session.getAttribute("logedInAdmin");
		if (logedInAdmin == null) {
			return "redirect:/admin/login";
		}
		model.addAttribute("logedInAdmin", logedInAdmin);
		return "adminDashboard";
	}

	@GetMapping("/allExpense")
	public String allUserExpenses(HttpSession session, Model model) {
		Admin logedInAdmin = (Admin) session.getAttribute("logedInAdmin");
		if (logedInAdmin == null) {
			return "redirect:/admin/login";
		}
		model.addAttribute("allExpenses", expenseService.getAllExpenses());
		model.addAttribute("logedInAdmin", logedInAdmin);
		return "allExpenseChart";
	}

	@GetMapping("/allUsers")
	public String allUser(HttpSession session, Model model) {
		Admin logedInAdmin = (Admin) session.getAttribute("logedInAdmin");
		if (logedInAdmin == null) {
			return "redirect:/admin/login";
		}
		model.addAttribute("users", userService.getAllUsers());
		model.addAttribute("logedInAdmin", logedInAdmin);
		return "allUserChart";
	}

	@GetMapping("/reportingPage")
	public String gotoReportingPage(HttpSession session, Model model) {
		Admin logedInAdmin = (Admin) session.getAttribute("logedInAdmin");
		if (logedInAdmin == null) {
			return "redirect:/admin/login";
		}
		
		model.addAttribute("logedInAdmin",logedInAdmin);
		
		long totalUsers = userService.countNoOfUsers();
		double monthExpenseAmount = expenseService.currentMonthExpenseAmount();
		long totalExpenseCurrentMonth = expenseService.totalExpenseCurrentMonth();
		model.addAttribute("totalUsers",totalUsers);
		model.addAttribute("monthExpenseAmount",monthExpenseAmount);
		
		double averageExpense = totalExpenseCurrentMonth/totalUsers;
		
		model.addAttribute("averageExpense", averageExpense);
		model.addAttribute("totalExpenseCurrentMonth",totalExpenseCurrentMonth);
		return "reportPage";
	}

	@GetMapping("/currentMonthExpenses")
	public String currentMonthExpenses(HttpSession session, Model model) {
		
		Admin logedInAdmin = (Admin) session.getAttribute("logedInAdmin");
		if (logedInAdmin == null) {
			return "redirect:/admin/login";
		}
		
		model.addAttribute("logedInAdmin",logedInAdmin);
		
		List<Expense> monthlyExpenses = expenseService.thisMonthExpenses();
		
		model.addAttribute("monthlyExpenses",monthlyExpenses);
		return "monthlyExpenseChart";
	}
	
	@GetMapping("/monthlyExpenses")
	public String getMonthlyExpense(@RequestParam Date fromDate, @RequestParam Date toDate, HttpSession session, Model model) {
		
		Admin logedInAdmin = (Admin) session.getAttribute("logedInAdmin");
		if (logedInAdmin == null) {
			return "redirect:/admin/login";
		}
		
		model.addAttribute("logedInAdmin",logedInAdmin);
		
		List<Expense> monthlyExpenses = expenseService.findMonthlyExpenses(fromDate, toDate);
		model.addAttribute("monthlyExpenses",monthlyExpenses);
		return "monthlyExpenseChart";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/admin/login";
	}

}
