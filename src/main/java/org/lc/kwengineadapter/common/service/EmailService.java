package org.lc.kwengineadapter.common.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@legalconnect.com}")
    private String fromEmail;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Async
    public void sendWelcomeEmail(String toEmail, String firstName, String lastName) {
        String subject = "Welcome to LegalConnect!";
        String htmlBody = buildWelcomeEmailTemplate(firstName, lastName);
        sendHtmlEmail(toEmail, subject, htmlBody);
    }

    @Async
    public void sendPasswordResetEmail(String toEmail, String firstName, String resetToken) {
        String subject = "Reset Your Password - LegalConnect";
        String resetLink = frontendUrl + "/reset-password?token=" + resetToken;
        String htmlBody = buildPasswordResetEmailTemplate(firstName, resetLink);
        sendHtmlEmail(toEmail, subject, htmlBody);
    }

    @Async
    public void sendPasswordChangedEmail(String toEmail, String firstName) {
        String subject = "Your Password Has Been Changed - LegalConnect";
        String htmlBody = buildPasswordChangedEmailTemplate(firstName);
        sendHtmlEmail(toEmail, subject, htmlBody);
    }

    @Async
    public void sendVerificationEmail(String toEmail, String firstName, String verificationLink) {
        String subject = "Verify Your Email - LegalConnect";
        String htmlBody = buildVerificationEmailTemplate(firstName, verificationLink);
        sendHtmlEmail(toEmail, subject, htmlBody);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", to, e);
        }
    }

    private String buildWelcomeEmailTemplate(String firstName, String lastName) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: ##333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, ##667eea 0%%, ##764ba2 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: ##f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .button { display: inline-block; padding: 12px 30px; background: ##667eea; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                    .footer { text-align: center; margin-top: 30px; color: ##666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>⚖️ Welcome to LegalConnect</h1>
                    </div>
                    <div class="content">
                        <h2>Hello %s %s!</h2>
                        <p>Thank you for joining LegalConnect - your gateway to legal resources and expert advice.</p>
                        <p>Your account has been successfully created. You can now:</p>
                        <ul>
                            <li>Browse our comprehensive legal resources</li>
                            <li>Bookmark articles for later reading</li>
                            <li>Access expert legal insights</li>
                            <li>Stay updated with the latest legal news</li>
                        </ul>
                        <a href="%s" class="button">Explore LegalConnect</a>
                        <p>If you have any questions, feel free to reach out to our support team.</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2025 LegalConnect. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(firstName, lastName, frontendUrl);
    }

    private String buildPasswordResetEmailTemplate(String firstName, String resetLink) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: ##333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, ##667eea 0%%, ##764ba2 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: ##f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .button { display: inline-block; padding: 12px 30px; background: ##667eea; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                    .warning { background: ##fff3cd; border-left: 4px solid ##ffc107; padding: 15px; margin: 20px 0; }
                    .footer { text-align: center; margin-top: 30px; color: ##666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🔐 Reset Your Password</h1>
                    </div>
                    <div class="content">
                        <h2>Hello %s!</h2>
                        <p>We received a request to reset your password for your LegalConnect account.</p>
                        <p>Click the button below to reset your password. This link will expire in 1 hour.</p>
                        <a href="%s" class="button">Reset Password</a>
                        <div class="warning">
                            <strong>Security Note:</strong> If you didn't request this password reset, please ignore this email. Your password will remain unchanged.
                        </div>
                        <p>For security reasons, this link can only be used once and will expire in 1 hour.</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2025 LegalConnect. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(firstName, resetLink);
    }

    private String buildPasswordChangedEmailTemplate(String firstName) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: ##333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, ##667eea 0%%, ##764ba2 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: ##f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .alert { background: ##d1ecf1; border-left: 4px solid ##17a2b8; padding: 15px; margin: 20px 0; }
                    .footer { text-align: center; margin-top: 30px; color: ##666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>✅ Password Changed Successfully</h1>
                    </div>
                    <div class="content">
                        <h2>Hello %s!</h2>
                        <p>This is a confirmation that your password has been successfully changed.</p>
                        <div class="alert">
                            <strong>Important:</strong> If you did not make this change, please contact our support team immediately.
                        </div>
                        <p>Your account security is our top priority. We recommend using a strong, unique password for your LegalConnect account.</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2025 LegalConnect. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(firstName);
    }

    private String buildVerificationEmailTemplate(String firstName, String verificationLink) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: ##333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, ##667eea 0%%, ##764ba2 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: ##f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .button { display: inline-block; padding: 12px 30px; background: ##667eea; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                    .footer { text-align: center; margin-top: 30px; color: ##666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>📧 Verify Your Email</h1>
                    </div>
                    <div class="content">
                        <h2>Hello %s!</h2>
                        <p>Thank you for signing up with LegalConnect!</p>
                        <p>Please verify your email address by clicking the button below:</p>
                        <a href="%s" class="button">Verify Email Address</a>
                        <p>If you didn't create an account with LegalConnect, please ignore this email.</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2025 LegalConnect. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(firstName, verificationLink);
    }
}
