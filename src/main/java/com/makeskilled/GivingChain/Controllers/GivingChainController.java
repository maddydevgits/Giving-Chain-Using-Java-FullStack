package com.makeskilled.GivingChain.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.makeskilled.GivingChain.Models.AdminModel;
import com.makeskilled.GivingChain.Models.RegistrationModel;
import com.makeskilled.GivingChain.Repositories.AdminRepository;
import com.makeskilled.GivingChain.Repositories.RegisterRepository;
import com.makeskilled.GivingChain.Services.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class GivingChainController {

    @Autowired
    RegisterRepository repo;

    @Autowired
    AdminRepository adminRepo;

    @Autowired
    EmailService emailService;
    
    @GetMapping("/")
    public String homePage() {
        return "land";
    }

    @GetMapping("/donations")
    public String donationsPage() {
        return "donations";
    }

    @GetMapping("/log")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/reg")
    public String signupPage() {
        return "register";
    }
    
    @PostMapping("/register")
    public String registerForm(RegistrationModel model,Model form) {
        
        System.out.println("Username: " + model.getUsername());
        System.out.println("Password: " + model.getPassword());
        System.out.println("Email: " + model.getEmail());
        System.out.print("Role: "+ model.getRole());
        
        if (repo.existsByUsername(model.getUsername())) {
            form.addAttribute("status", "Username already exists. Please choose another username.");
            return "register"; // Return to the registration page with the message
        }
        
        if (repo.existsByEmail(model.getEmail())) {
            form.addAttribute("status", "Email already exists. Please use another email.");
            return "register"; // Return to the registration page with the message
        }

        repo.save(model);
        String subject = "Welcome to GivingChain";
        String body = "<div><b>Thank you for registering with GivingChain.</b></div>";
        String response = emailService.sendEmail(model.getEmail(), model.getUsername(), subject, body);
        System.out.println(response);
        form.addAttribute("stat", "Registration successful! You can now log in.");
        return "redirect:/log";
    }

    @PostMapping("/login")
    public String loginForm(RegistrationModel model,Model form,HttpSession session) {
        
        System.out.println("Username: " + model.getUsername());
        System.out.println("Password: " + model.getPassword());

        if(repo.existsByUsername(model.getUsername())==false){
            form.addAttribute("status", "Username doesn't exist, register first");
            return "register";
        }

        RegistrationModel user = repo.findByUsername(model.getUsername());
        if (user != null && user.getPassword().equals(model.getPassword())) {
            // Store username in the session
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            
            form.addAttribute("status", "Login successful! Welcome " + user.getUsername());
            if (user.getRole().equals("USER")){
                return "redirect:/dashboard";
            } else {
                return "redirect:/ngodashboard";
            }
        } else {
            form.addAttribute("status", "Invalid username or password. Please try again.");
            return "login";  // Return to login page with an error message
        }
    }
    
    @GetMapping("/admin")
    public String adminPage() {
        return "adminlogin";
    }

    @PostMapping("/adlog")
    public String adloginForm(AdminModel model,Model form,HttpSession session) {
        
        System.out.println("Username: " + model.getUsername());
        System.out.println("Password: " + model.getPassword());


        AdminModel user = adminRepo.findByUsername(model.getUsername());
        if (user != null && user.getPassword().equals(model.getPassword())) {
            // Store username in the session
            session.setAttribute("username", user.getUsername());
            
            form.addAttribute("status", "Login successful! Welcome " + user.getUsername());
                return "redirect:/adminindex";
    
        } else {
            form.addAttribute("status", "Invalid username or password. Please try again.");
            return "adminlogin";  // Return to login page with an error message
        }
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "index";
    }

    @GetMapping("/ngodashboard")
    public String ngoDashboardPage(){
        return "ngoindex";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Invalidate the session to log out the user
        return "redirect:/log";  // Redirect to the login page
    }

    @GetMapping("/adminindex")
    public String adminIndexPage(){
        return "adminindex";
    }

    @GetMapping("/admindonations")
    public String adminDonationsPage(){
        return "admindonations";
    }

    @GetMapping("/donationqueue")
    public String donationQueuePage(){
        return "donationqueue";
    }
}
