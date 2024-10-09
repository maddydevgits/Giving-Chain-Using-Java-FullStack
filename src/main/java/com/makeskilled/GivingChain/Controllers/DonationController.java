package com.makeskilled.GivingChain.Controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.makeskilled.GivingChain.Models.AdminModel;
import com.makeskilled.GivingChain.Models.DonationModel;
import com.makeskilled.GivingChain.Models.MappingModel;
import com.makeskilled.GivingChain.Models.RegistrationModel;
import com.makeskilled.GivingChain.Models.RequestModel;
import com.makeskilled.GivingChain.Repositories.AdminRepository;
import com.makeskilled.GivingChain.Repositories.DonationRepository;
import com.makeskilled.GivingChain.Repositories.MappingRepository;
import com.makeskilled.GivingChain.Repositories.RegisterRepository;
import com.makeskilled.GivingChain.Repositories.RequestRepository;
import com.makeskilled.GivingChain.Services.EmailService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/donations")
public class DonationController {
    
    @Autowired
    DonationRepository donationRepo;

    @Autowired
    MappingRepository mapRepo;

    @Autowired
    RegisterRepository registerRepo;

    @Autowired
    RequestRepository repo;

    @Autowired
    EmailService emailService;

    @Autowired
    AdminRepository adminRepo;

    @PostMapping("/donate")
    public String createDonation(HttpSession session, DonationModel model,Model form,RedirectAttributes redirectAttributes) {

        String username = (String) session.getAttribute("username");
        
        System.out.println("username: " + username);
        System.out.println("Name: " + model.getName());
        System.out.println("Phone: " + model.getPhone());
        System.out.println("Donation Type: "+ model.getDonationType());
        System.out.println("Address: "+model.getAddress());
        System.out.println("Text: "+model.getText());
        
        model.setUsername(username);
        model.setDistributed(false);
        model.setDonatedDate(new Date());

        donationRepo.save(model);

        RegistrationModel m=registerRepo.findByUsername(username);
        String email=m.getEmail();

        AdminModel admin=adminRepo.findByUsername("admin");
        String adminemail=admin.getEmail();


        String subject = "Donation to GivingChain";
        String body = "<div>"
                + "<h3>Dear " + username + ",</h3>"
                + "<p>Thank you for your generous donation to <b>GivingChain</b>.</p>"
                + "<p><strong>Donation Details:</strong></p>"
                + "<ul>"
                + "<li><strong>Username:</strong> " + username +"</li>"
                + "<li><strong>Name:</strong> " + model.getName() + "</li>"
                + "<li><strong>Phone:</strong> " + model.getPhone() + "</li>"
                + "<li><strong>Donation Type:</strong> " + model.getDonationType() + "</li>"
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

        form.addAttribute("message", "Donation request successfully created.");
        redirectAttributes.addFlashAttribute("message", "Donation request successfully created.");
        redirectAttributes.addFlashAttribute("username", username);
        return "redirect:/dashboard#contact";  
    }

    @GetMapping("/list")
    public String getPublicDonations(HttpSession session,Model form) {

        List <DonationModel> donations=donationRepo.findAll();
        form.addAttribute("data", donations);
        return "userdonations";
    }

    @GetMapping("/mylist")
    public String getPrivateDonations(HttpSession session,Model form) {

        String username=(String)session.getAttribute("username");

        List <DonationModel> donations=donationRepo.findByUsername(username);

        form.addAttribute("data", donations);
        return "mydonations";

    }

    @GetMapping("/history")
    public String getHistory(HttpSession session, Model form) {
        String username=(String) session.getAttribute("username");
        List<MappingModel> myMappings = mapRepo.findByDonation_Username(username);
        form.addAttribute("mappings", myMappings);
        
        return "userhistory";
    }


    @GetMapping("/mapping")
    public String getUnassignedDonationsAndRequests(Model model) {
        // Fetch unassigned donations
        List<DonationModel> unassignedDonations = donationRepo.findByIsDistributed(false);

        // Fetch requests that are not accepted yet
        List<RequestModel> unacceptedRequests = repo.findByAccepted(false);

        model.addAttribute("donations", unassignedDonations);
        model.addAttribute("requests", unacceptedRequests);

        return "admindonations";  // This is the Thymeleaf page where admin can map donations to requests
    }

    @PostMapping("/map")
    public String mapDonationToRequest(@RequestParam("donationId") Long donationId, @RequestParam("requestId") Long requestId, RedirectAttributes redirectAttributes) {
        DonationModel donation = donationRepo.findById(donationId).orElseThrow(() -> new IllegalArgumentException("Invalid donation ID"));
        RequestModel request = repo.findById(requestId).orElseThrow(() -> new IllegalArgumentException("Invalid request ID"));

        MappingModel mapping = new MappingModel();
        mapping.setDonation(donation);
        mapping.setRequest(request);

        mapRepo.save(mapping);

        // Mark donation as distributed
        donation.setDistributed(true);
        donationRepo.save(donation);

        // Mark request as accepted
        request.setAccepted(true);
        repo.save(request);

        // Prepare email details for donor and requester
        String donorUsername = donation.getUsername(); // Assuming username is the email, otherwise use the correct field
        String requesterUsername = request.getUsername(); // Assuming username is the email, otherwise use the correct field

        // Fetch the email addresses from the repository using username
        RegistrationModel donor = registerRepo.findByUsername(donorUsername);
        RegistrationModel requester = registerRepo.findByUsername(requesterUsername);

        String donorEmail=donor.getEmail();
        String requesterEmail=requester.getEmail();

        
        // Email subjects
        String subjectDonor = "Your Donation Has Been Mapped to a Request";
        String subjectRequester = "A Donation Has Been Mapped to Your Request";
        
        // Email bodies
        String bodyDonor = "<div>"
            + "<h3>Dear " + donation.getName() + ",</h3>"
            + "<p>Your donation has been successfully mapped to a request.</p>"
            + "<p><strong>Donation Details:</strong></p>"
            + "<ul>"
            + "<li><strong>Donation Type:</strong> " + donation.getDonationType() + "</li>"
            + "<li><strong>Donation Description:</strong> " + donation.getText() + "</li>"
            + "<li><strong>Donation Contact:</strong> " + donation.getPhone() + "</li>"
            + "</ul>"
            + "<p><strong>Mapped to Requester:</strong> " + request.getName() + "</p>"
            + "<ul>"
            + "<li><strong>Request Type:</strong> " + request.getRequestType() + "</li>"
            + "<li><strong>Request Description:</strong> " + request.getText() + "</li>"
            + "<li><strong>Requester Contact:</strong> " + request.getPhone() + "</li>"
            + "</ul>"
            + "<p>Thank you for your generous support!</p>"
            + "<p>Sincerely,<br><b>The GivingChain Team</b></p>"
            + "</div>";

        String bodyRequester = "<div>"
            + "<h3>Dear " + request.getName() + ",</h3>"
            + "<p>A donation has been mapped to your request.</p>"
            + "<p><strong>Request Details:</strong></p>"
            + "<ul>"
            + "<li><strong>Request Type:</strong> " + request.getRequestType() + "</li>"
            + "<li><strong>Request Description:</strong> " + request.getText() + "</li>"
            + "<li><strong>Requester Contact:</strong> " + request.getPhone() + "</li>"
            + "</ul>"
            + "<p><strong>Mapped to Donor:</strong> " + donation.getName() + "</p>"
            + "<ul>"
            + "<li><strong>Donation Type:</strong> " + donation.getDonationType() + "</li>"
            + "<li><strong>Donation Description:</strong> " + donation.getText() + "</li>"
            + "<li><strong>Donor Contact:</strong> " + donation.getPhone() + "</li>"
            + "</ul>"
            + "<p>We hope this donation fulfills your needs!</p>"
            + "<p>Sincerely,<br><b>The GivingChain Team</b></p>"
            + "</div>";

        // Send emails to the donor and requester
        String response=emailService.sendEmail(donorEmail, donation.getName(), subjectDonor, bodyDonor);
        String response1=emailService.sendEmail(requesterEmail, request.getName(), subjectRequester, bodyRequester);

        System.out.println(response);
        System.out.println(response1);

        // Redirect with success message
        redirectAttributes.addFlashAttribute("message", "Successfully mapped donation to request and notified both donor and requester.");
        return "redirect:/donations/mapping";
    }

    @GetMapping("/mappings")
    public String viewMappings(Model model) {
        List<MappingModel> mappings = mapRepo.findAll();
        model.addAttribute("mappings", mappings);
        return "adminmappings";
    }

    @GetMapping("/mappings1")
    public String viewMappings1(Model model) {
        List<MappingModel> mappings = mapRepo.findAll();
        model.addAttribute("mappings", mappings);
        return "donations";
    }
    
}
