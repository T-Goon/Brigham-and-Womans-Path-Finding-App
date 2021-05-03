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

    /**
     * Read account information from file to set up the sender
     *
     * @return the message to be sent to recipient
     */
    public MimeMessage setUpSender() {
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

        MimeMessage message = null;
        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

        return message;
    }

    /**
     * Send confirmation email upon registration
     *
     * @param email the email address to send the confirmation email to
     * @param name the name of the user registering for an account
     */
    public void sendConfirmation(String email, String name) {
        MimeMessage message = setUpSender();

        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            message.setSubject("Brigham and Women's Hospital Application Confirmation");
            message.setText("Hello " + name + ",\n\nThank you for registering for a Brigham and Women's Hospital Application account. " +
                    "This email confirms that your registration was successful.\n\n\nBrigham and Women's Hospital Application Team");

            Transport.send(message);
            System.out.println("Sent message successfully...");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    // assigned to a task, and a task you submitted is completed
    // look at submitter field to get email address

    /**
     * Send tasks assigned to user
     */
    public void sendAssignment(String email, String name, String submitter, String task) {
        MimeMessage message = setUpSender();

        System.out.println("sendAssignment");
        System.out.println("email: " + email);
        System.out.println("name: " + name);
        System.out.println("submitter: " + submitter);
        System.out.println("task: " + task);
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            message.setSubject("Brigham and Women's Hospital Application - New Assigned Task");
            message.setText("Hello " + name + ",\n\n" + submitter + " recently assigned you to the following task:\n" +
                    task + "\n\n\nBrigham and Women's Hospital Application Team");

            Transport.send(message);
            System.out.println("Sent message successfully...");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Notify user when tasks they submitted are completed
     */
    public void notifyCompletion(String email, String name, String submitter, String task) {
        MimeMessage message = setUpSender();

        System.out.println("notifyCompletion");
        System.out.println("email: " + email);
        System.out.println("name: " + name);
        System.out.println("submitter: " + submitter);
        System.out.println("task: " + task);
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            message.setSubject("Brigham and Women's Hospital Application - Task Complete");
            message.setText("Hello " + name + ",\n\nThe following task that you assigned to " + submitter + " was recently completed:\n" +
                    task + "\n\n\nBrigham and Women's Hospital Application Team");

            Transport.send(message);
            System.out.println("Sent message successfully...");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
