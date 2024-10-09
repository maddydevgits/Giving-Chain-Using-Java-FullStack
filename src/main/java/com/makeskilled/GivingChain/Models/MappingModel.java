package com.makeskilled.GivingChain.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "donation_request_mapping")
public class MappingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "donation_id")
    private DonationModel donation;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private RequestModel request;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DonationModel getDonation() {
        return donation;
    }

    public void setDonation(DonationModel donation) {
        this.donation = donation;
    }

    public RequestModel getRequest() {
        return request;
    }

    public void setRequest(RequestModel request) {
        this.request = request;
    }
}