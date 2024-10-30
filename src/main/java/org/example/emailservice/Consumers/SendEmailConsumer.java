package org.example.emailservice.Consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.example.emailservice.Dtos.SendEmailDto;
import org.example.emailservice.Utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

@Service
public class SendEmailConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "signup",groupId = "emailService")
    public void SendMessage(String message) throws JsonProcessingException {
        SendEmailDto sendEmailDto = objectMapper.readValue(message,SendEmailDto.class);

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendEmailDto.getFrom(), "ptykogfbncyuyggj");
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session, sendEmailDto.getTo(),
                sendEmailDto.getSubject(), sendEmailDto.getBody());

    }
}
