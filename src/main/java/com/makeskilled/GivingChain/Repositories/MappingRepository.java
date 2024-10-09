package com.makeskilled.GivingChain.Repositories;

import com.makeskilled.GivingChain.Models.MappingModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingRepository extends JpaRepository<MappingModel, Long> {
    // Additional queries can be added here if needed.
    List <MappingModel> findByDonation_Username(String username);
    List <MappingModel> findByRequest_Username(String username);
}