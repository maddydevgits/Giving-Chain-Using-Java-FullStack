package com.makeskilled.GivingChain.Controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.makeskilled.GivingChain.Models.AdminModel;
import com.makeskilled.GivingChain.Models.DonationModel;
import com.makeskilled.GivingChain.Models.MappingModel;
import com.makeskilled.GivingChain.Models.RegistrationModel;
import com.makeskilled.GivingChain.Models.RequestModel;
import com.makeskilled.GivingChain.Repositories.AdminRepository;
import com.makeskilled.GivingChain.Repositories.MappingRepository;
import com.makeskilled.GivingChain.Repositories.RegisterRepository;
import com.makeskilled.GivingChain.Repositories.RequestRepository;
import com.makeskilled.GivingChain.Services.EmailService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/requests")
public class RequestController {
    
    @Autowired
    RequestRepository repo;

    @Autowired
    MappingRepository mapRepo;

    @Autowired
    RegisterRepository userRepo;

    @Autowired
    AdminRepository adminRepo;

    @Autowired
    EmailService emailService;

    @PostMapping("/create")
    public String createRequest(HttpSession session, RequestModel model,Model form,RedirectAttributes redirectAttributes) {

        String username = (String) session.getAttribute("username");
        
        System.out.println("username: " + username);
        System.out.println("Name: " + model.getName());
        System.out.println("Phone: " + model.getPhone());
        System.out.println("Donation Type: "+ model.getRequestType());
        System.out.println("Address: "+model.getAddress());
        System.out.println("Text: "+model.getText());
        
        model.setUsername(username);
        model.setAccepted(false);
        model.setRequestedDate(new Date());

        repo.save(model);

        RegistrationModel m=userRepo.findByUsername(username);
        String email=m.getEmail();

        AdminModel admin=adminRepo.findByUsername("admin");
        String adminemail=admin.getEmail();


        String subject = "Request to GivingChain";
        String body = "<div>"
                + "<h3>Dear " + username + ",</h3>"
                + "<p>Thank you for your request to <b>GivingChain</b>.</p>"
                + "<p><strong>Request Details:</strong></p>"
                + "<ul>"
                + "<li><strong>Username:</strong> " + username +"</li>"
                + "<li><strong>Name:</strong> " + model.getName() + "</li>"
                + "<li><strong>Phone:</strong> " + model.getPhone() + "</li>"
                + "<li><strong>Request Type:</strong> " + model.getRequestType() + "</li>"
                + "<li><strong>Address:</strong> " + model.getAddress() + "</li>"
                + "<li><strong>Message:</strong> " + model.getText() + "</li>"
                + "</ul>"
                + "<p>We deeply appreciate your support in helping those in need.</p>"
                + "<p>Sincerely,<br><b>The GivingChain Team</b></p>"
                + "</div>";

        String response = emailService.sendEmail(email, username, subject, body);
        System.out.println(response);

        String adminResponse = emailService.sendEmail(adminemail, username, subject, body);
        System.out.println(adminResponse);

        form.addAttribute("message1", "Donation request successfully created.");
        redirectAttributes.addFlashAttribute("message1", "Donation request successfully created.");
        redirectAttributes.addFlashAttribute("username", username);
        return "redirect:/ngodashboard#contact1";  

    }

    @GetMapping("/mylist")
    public String getPublicDonations(HttpSession session,Model form) {

        String username=(String) session.getAttribute("username");
        // List<MappingModel> myRequestMappings = mapRepo.findByRequest_Username(username);
    
        // form.addAttribute("mappings", myRequestMappings);

        List <RequestModel> requests=repo.findByUsername(username);
        form.addAttribute("data", requests);
        return "myrequests";
    }

    @GetMapping("/history")
    public String getPublicDonationsHistory(HttpSession session,Model form) {

        String username=(String) session.getAttribute("username");
        List<MappingModel> myRequestMappings = mapRepo.findByRequest_Username(username);
    
        form.addAttribute("mappings", myRequestMappings);

        return "requestshistory";
    }
    
}
