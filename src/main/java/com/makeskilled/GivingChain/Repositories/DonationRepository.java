package com.makeskilled.GivingChain.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.makeskilled.GivingChain.Models.DonationModel;

public interface DonationRepository extends JpaRepository<DonationModel,Long> {

    List<DonationModel> findByUsername(String username);
    List<DonationModel> findByIsDistributed(boolean distributed);
    
}
