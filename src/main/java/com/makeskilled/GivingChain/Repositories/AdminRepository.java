package com.makeskilled.GivingChain.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.makeskilled.GivingChain.Models.AdminModel;

public interface AdminRepository extends JpaRepository<AdminModel,Long>{
    AdminModel findByUsername(String username);
}
