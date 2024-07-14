package com.secretkeeper.repository;

import com.secretkeeper.entity.CustomerDetail;
import com.secretkeeper.entity.EmailDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailDetailsRepository extends JpaRepository<EmailDetails,Integer> {
    List<EmailDetails> findByCustomerId(Integer id);
}
