package com.makeskilled.GivingChain.Models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="donations")
public class DonationModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String donationType;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    private Date donatedDate;

    @Column(nullable = false)
    private boolean isDistributed;

    
    public DonationModel(Long id, String username, String name, String phone, String donationType, String address,
            String text, Date donatedDate, boolean isDistributed) {
        Id = id;
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.donationType = donationType;
        this.address = address;
        this.text = text;
        this.donatedDate = donatedDate;
        this.isDistributed = isDistributed;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDonationType() {
        return donationType;
    }

    public void setDonationType(String donationType) {
        this.donationType = donationType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDonatedDate() {
        return donatedDate;
    }

    public void setDonatedDate(Date donatedDate) {
        this.donatedDate = donatedDate;
    }

    public DonationModel(){

    }

    public boolean isDistributed() {
        return isDistributed;
    }

    public void setDistributed(boolean isDistributed) {
        this.isDistributed = isDistributed;
    }
}
