package com.makeskilled.GivingChain.Repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.makeskilled.GivingChain.Models.RequestModel;

public interface RequestRepository extends JpaRepository<RequestModel,Long>{
    List<RequestModel> findByUsername(String username);
    List<RequestModel> findByAccepted(boolean accepted);
}
