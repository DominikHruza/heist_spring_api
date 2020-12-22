package com.hackatonapi.HackatonRest.service;

import com.hackatonapi.HackatonRest.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

   public void sendMail(String email, String subject, String body) throws MailException {
       SimpleMailMessage mail = new SimpleMailMessage();
       mail.setTo(email);
       mail.setFrom("team@ag04.com");
       mail.setSubject(subject);
       mail.setText(body);

       javaMailSender.send(mail);
   }
}
