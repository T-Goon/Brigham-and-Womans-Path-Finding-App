package edu.wpi.cs3733.D21.teamB.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.stream.Collectors;

public class ExternalCommunication {

    public static List<Thread> threads = new ArrayList<>();

    /**
     * Read account information from file to set up the sender
     *
     * @return the message to be sent to recipient
     */
    public static MimeMessage setUpSender() {
        String email;
        String password;
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
     * @param name  the name of the user registering for an account
     */
    public static void sendConfirmation(String email, String name) {
        Thread emailThread = new Thread(() -> {
            MimeMessage message = setUpSender();

            try {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

                message.setSubject("Brigham and Women's Hospital Application Confirmation");
                message.setText("Hello " + name + ",\n\nThank you for registering for a Brigham and Women's Hospital Application account. " +
                        "This email confirms that your registration was successful.\n\n\nBrigham and Women's Hospital Application Team");

                Transport.send(message);
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        });
        emailThread.start();
        threads.add(emailThread);
    }

    /**
     * Send tasks assigned to user
     *
     * @param email     the email address to send the task to
     * @param name      the name of the user to send the task to
     * @param submitter the name of the user who assigned the task
     * @param task      the type of request
     */
    public static void sendAssignment(String email, String name, String submitter, String task) {
        Thread emailThread = new Thread(() -> {
            MimeMessage message = setUpSender();

            try {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

                message.setSubject("Brigham and Women's Hospital Application - New Assigned Task");
                message.setText("Hello " + name + ",\n\n" + submitter + " recently assigned you to the following task:\n" +
                        task + "\n\n\nBrigham and Women's Hospital Application Team");

                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        emailThread.start();
        threads.add(emailThread);
    }

    /**
     * Notify user when tasks they submitted are completed
     *
     * @param email    the email address of the original submitter
     * @param name     the name of the original submitter
     * @param assignee the name of the user who completed the task
     * @param task     the type of request
     */
    public static void notifyCompletion(String email, String name, String assignee, String task) {
        Thread emailThread = new Thread(() -> {
            MimeMessage message = setUpSender();

            try {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

                message.setSubject("Brigham and Women's Hospital Application - Task Complete");
                message.setText("Hello " + name + ",\n\nThe following task that you assigned to " + assignee + " was recently completed:\n" +
                        task + "\n\n\nBrigham and Women's Hospital Application Team");

                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        emailThread.start();
        threads.add(emailThread);
    }

    /**
     * Send email to user when their COVID survey is marked complete
     *
     * @param email  the email address of the survey submitter
     * @param name   the name of the survey submitter
     * @param status the submitter's status on the COVID survey
     */
    public static void sendCovidSurveyApproval(String email, String name, String status) {
        Thread emailThread = new Thread(() -> {
            MimeMessage message = setUpSender();

            try {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

                message.setSubject("Brigham and Women's Hospital Application - COVID-19 Survey Approved");
                message.setText("Hello " + name + ",\n\nYour COVID-19 survey was recently approved with status " + status +
                        ".\n\n\nBrigham and Women's Hospital Application Team");

                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        emailThread.start();
        threads.add(emailThread);
    }
}
