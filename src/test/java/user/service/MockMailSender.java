package user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MockMailSender implements MailSender {
    private List<String> requests = new ArrayList<String>();	
    
    public List<String> getRequests() {
        return requests;
    }

    public void send(SimpleMailMessage mailMessage) throws MailException {
        requests.add(mailMessage.getTo()[0]);  
    }

    public void send(SimpleMailMessage[] mailMessage) throws MailException {
    }
}