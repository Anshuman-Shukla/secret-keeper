package com.secretkeeper.repository;

import com.secretkeeper.entity.CustomerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDetailRepository extends JpaRepository<CustomerDetail,Integer> {

    List<CustomerDetail> findByEmail(String email);


}
