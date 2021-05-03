package edu.wpi.cs3733.D21.teamB.util;

import java.io.*;
import java.util.List;
import java.util.Objects;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.Properties;
import java.util.stream.Collectors;

public class ExternalCommunication {

    // create text file and add to git ignore
    // confirmation email after registering
    // assigned to a task, and a task you submitted is completed
    // look at submitter field to get email address

    /**
     * Read account information from file
     */
    public void getAccountInformation() {

    }

    public void sendConfirmation(String recipient, String name) {
        String email = null;
        String password = null;
        InputStream s = CSVHandler.class.getResourceAsStream("/edu/wpi/cs3733/D21/teamB/account/account.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(s)));
        List<String> lines = bufferedReader.lines().collect(Collectors.toList());

        email = lines.get(0);
        password = lines.get(1);
        Properties properties = System.getProperties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String finalEmail = email;
        String finalPassword = password;
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(finalEmail, finalPassword);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            message.setSubject("Brigham and Women's Hospital Application Confirmation");
            message.setText("Hello " + name + ",\n\nThank you for registering for a Brigham and Women's Hospital Application account. " +
                    "This email confirms that your registration was successful.\n\n\nBrigham and Women's Hospital Application Team");

            Transport.send(message);
            System.out.println("Sent message successfully...");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
