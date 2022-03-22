package com.revo.myboard.email;

import com.revo.myboard.exception.EmailSendingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@Service
public class EmailService {

    private static final String FROM = "Activation@revo-dev.pl";
    private static final String SUBJECT = "Kod aktywacyjny dla konta board.revo-dev.pl";
    private static final String TYPE = "text/html; charset=utf-8";
    private static final String CONTENT = "Aby aktywować swoje konto przejdź do linku: http://board.revo-dev.pl/#/active/%s";
    private static final String HOST_PROPERTIES = "mail.smtp.host";
    private static final String PORT_PROPERTIES = "mail.smtp.port";

    @Value("${mail.smtp.host}")
    private String host;
    @Value("${mail.smtp.port}")
    private String port;

    public void sendActiavtionLink(String email, String code) {
        try {
            Transport.send(getMimeMessage(email, code));
        } catch (Exception exception) {
            throw new EmailSendingException(email, exception.getMessage() + exception.getCause().getMessage());
        }
    }

    private MimeMessage getMimeMessage(String email, String code) throws Exception {
        var mimeMessage = new MimeMessage(this.getSession());
        mimeMessage.setFrom(new InternetAddress(FROM));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        mimeMessage.setSubject(SUBJECT);
        mimeMessage.setContent(this.getMultipart(code));
        return mimeMessage;
    }

    private Session getSession(){
        return Session.getInstance(getProperties());
    }

    private Properties getProperties(){
        var properties = new Properties();
        properties.put(HOST_PROPERTIES, host);
        properties.put(PORT_PROPERTIES, port);
        return properties;
    }

    private Multipart getMultipart(String code) throws Exception {
        var multipart = new MimeMultipart();
        multipart.addBodyPart(getMimeBodyPart(code));
        return multipart;
    }

    private MimeBodyPart getMimeBodyPart(String code) throws Exception{
        var mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(CONTENT.formatted(code), TYPE);
        return mimeBodyPart;
    }
}
