package com.secretkeeper.service;

import com.secretkeeper.entity.EmailDetails;
import com.secretkeeper.entity.EncryptionKey;
import com.secretkeeper.model.EmailRequest;
import com.secretkeeper.repository.EmailDetailsRepository;
import com.secretkeeper.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class EmailDetailService {
    @Autowired
    private EmailDetailsRepository repository;

    public EmailRequest saveEmailDetail(EmailRequest details, Integer customerId) {
        EmailDetails emailDetails = new EmailDetails();
        String secretKey = EncryptionUtil.generateKey();
        String encryptPwd = EncryptionUtil.encrypt(details.getPwd(), secretKey);
        EncryptionKey key = EncryptionKey.builder().encryptionKey(secretKey).build();
        key.setEmailDetails(emailDetails);
        emailDetails.setEmailId(details.getEmailId());
        emailDetails.setKey(key);
        emailDetails.setUserName(details.getUserName());
        emailDetails.setPassword(encryptPwd);
        emailDetails.setCustomerId(customerId);
        try {
            repository.save(emailDetails);
            return details;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to save customer!!");
        }
    }

    public List<EmailRequest> getAllEmailDetail(Integer id) {
        List<EmailRequest> response = new LinkedList<>();
        List<EmailDetails> emailDetailsList = repository.findByCustomerId(id);
        emailDetailsList.forEach(details -> {
            EmailRequest request = new EmailRequest();
            request.setEmailId(details.getEmailId());
            request.setPwd(EncryptionUtil.decrypt(details.getPassword(), details.getKey().getEncryptionKey()));
            request.setUserName(details.getUserName());
            response.add(request);
        });
        return response;
    }

    public String deleteEmailData(Integer emailId) {
        repository.deleteById(emailId);
        return "Email Detail deleted successfully !!";
    }
}
