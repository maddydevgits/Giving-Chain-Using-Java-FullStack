package com.makeskilled.GivingChain.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.makeskilled.GivingChain.Models.RegistrationModel;


public interface RegisterRepository  extends JpaRepository<RegistrationModel,Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    RegistrationModel findByUsername(String username);
    RegistrationModel findByEmail(String email);
}
