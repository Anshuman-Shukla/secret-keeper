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
import java.util.Optional;

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
            request.setId(details.getId());
            request.setEmailId(details.getEmailId());
            request.setPwd(EncryptionUtil.decrypt(details.getPassword(), details.getKey().getEncryptionKey()));
            request.setUserName(details.getUserName());
            response.add(request);
        });
        return response;
    }

    public EmailRequest getEmailDetail(Integer id) {
        EmailRequest response = new EmailRequest();
        Optional<EmailDetails> emailDetail = repository.findById(id);
        if (emailDetail.isPresent()) {
            EmailDetails details = emailDetail.get();
            response.setId(details.getId());
            response.setEmailId(details.getEmailId());
            response.setPwd(EncryptionUtil.decrypt(details.getPassword(), details.getKey().getEncryptionKey()));
            response.setUserName(details.getUserName());
        }
        return response;
    }

    public String deleteEmailData(Integer emailId) {
        repository.deleteById(emailId);
        return "Email Detail deleted successfully !!";
    }

    public EmailRequest updateEmailDetail(Integer emailId, EmailRequest request) {

        EmailDetails emailDetailById = repository.getById(emailId);
        String secretKey = EncryptionUtil.generateKey();
        String encryptPwd = EncryptionUtil.encrypt(request.getPwd(), secretKey);
        EncryptionKey updatedKey = emailDetailById.getKey();
        updatedKey.setEncryptionKey(secretKey);
        updatedKey.setEmailDetails(emailDetailById);
        emailDetailById.setEmailId(request.getEmailId());
        emailDetailById.setKey(updatedKey);
        emailDetailById.setUserName(request.getUserName());
        emailDetailById.setPassword(encryptPwd);
        try {
            EmailDetails response = repository.save(emailDetailById);
            return new EmailRequest(response.getId(), response.getEmailId(), request.getPwd(), response.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
