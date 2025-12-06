package com.musiclibrary.notification.service;

import com.musiclibrary.notification.dto.LoginNotificationDTO;
import com.musiclibrary.notification.dto.NotificationDTO;
import com.musiclibrary.notification.dto.SongNotificationEmailDTO;
import com.musiclibrary.notification.dto.WelcomeEmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${app.mail.from:noreply@musiclibrary.com}")
    private String fromEmail;
    
    @Value("${app.mail.enabled:false}")
    private boolean emailEnabled;
    
    @Async
    public void sendWelcomeEmail(WelcomeEmailDTO dto) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  📧 SENDING WELCOME EMAIL                                ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  To: " + dto.getToEmail());
        System.out.println("║  Name: " + dto.getName());
        System.out.println("║  Type: " + dto.getUserType());
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        if (!emailEnabled) {
            logEmailDisabled("Welcome", dto.getToEmail());
            return;
        }
        
        try {
            String subject = "🎵 Welcome to Music Library, " + dto.getName() + "!";
            String content = buildWelcomeEmailHtml(dto);
            
            sendHtmlEmail(dto.getToEmail(), subject, content);
            
            System.out.println("✅ SUCCESS: Welcome email sent to " + dto.getToEmail());
            log.info("✅ Welcome email SENT to: {}", dto.getToEmail());
            
        } catch (Exception e) {
            System.out.println("❌ FAILED: Could not send email to " + dto.getToEmail());
            System.out.println("❌ ERROR: " + e.getMessage());
            System.out.println("❌ TIP: Make sure you're using Gmail App Password!");
            log.error("❌ Failed to send welcome email: {}", e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Async
    public void sendLoginNotification(LoginNotificationDTO dto) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  🔐 SENDING LOGIN NOTIFICATION EMAIL                     ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  To: " + dto.getToEmail());
        System.out.println("║  Name: " + dto.getName());
        System.out.println("║  Type: " + dto.getUserType());
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        if (!emailEnabled) {
            logEmailDisabled("Login Notification", dto.getToEmail());
            return;
        }
        
        try {
            String subject = "🔐 New Login to Music Library - " + dto.getUserType();
            String content = buildLoginNotificationHtml(dto);
            
            sendHtmlEmail(dto.getToEmail(), subject, content);
            
            System.out.println("✅ SUCCESS: Login notification sent to " + dto.getToEmail());
            log.info("✅ Login notification SENT to: {}", dto.getToEmail());
            
        } catch (Exception e) {
            System.out.println("❌ FAILED: Could not send login notification to " + dto.getToEmail());
            System.out.println("❌ ERROR: " + e.getMessage());
            log.error("❌ Failed to send login notification: {}", e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Async
    public void sendNewSongEmailToUser(NotificationDTO dto) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  🎵 SENDING NEW SONG EMAIL NOTIFICATION                  ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  To: " + dto.getEmail());
        System.out.println("║  Song: " + dto.getSongName());
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        if (!emailEnabled) {
            logEmailDisabled("New Song", dto.getEmail());
            return;
        }
        
        try {
            String subject = dto.getSubject() != null ? dto.getSubject() : "🎵 New Song Added - Music Library";
            String content = buildSingleUserSongNotificationHtml(dto);
            
            sendHtmlEmail(dto.getEmail(), subject, content);
            
            System.out.println("✅ SUCCESS: New song notification sent to " + dto.getEmail());
            log.info("✅ New song email SENT to: {}", dto.getEmail());
            
        } catch (Exception e) {
            System.out.println("❌ FAILED: Could not send notification to " + dto.getEmail());
            System.out.println("❌ ERROR: " + e.getMessage());
            log.error("❌ Failed to send new song email: {}", e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Async
    public void sendNewSongNotification(SongNotificationEmailDTO dto) {
        if (!emailEnabled) {
            logEmailDisabled("Song Notification", "users");
            return;
        }
        
        String subject = "🎵 New Song Added: " + dto.getSongName();
        String content = buildSongNotificationHtml(dto);
        
        for (String email : dto.getUserEmails()) {
            try {
                sendHtmlEmail(email, subject, content);
                System.out.println("✅ Song notification sent to: " + email);
            } catch (Exception e) {
                System.out.println("❌ Failed to send to: " + email + " - " + e.getMessage());
            }
        }
    }
    
    private String buildSingleUserSongNotificationHtml(NotificationDTO dto) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #1a1a2e; margin: 0; padding: 20px; }
                    .container { max-width: 600px; margin: 0 auto; background: linear-gradient(135deg, #16213e 0%%, #0f3460 100%%); border-radius: 20px; padding: 40px; }
                    .header { text-align: center; margin-bottom: 30px; }
                    .logo { font-size: 60px; margin-bottom: 10px; }
                    h1 { color: #e94560; margin: 0; font-size: 26px; }
                    .content { color: #ffffff; line-height: 1.8; text-align: center; }
                    .song-card { background: rgba(233, 69, 96, 0.2); border-radius: 15px; padding: 30px; margin: 25px 0; border: 2px solid #e94560; }
                    .song-name { color: #ffffff; font-size: 28px; margin: 0 0 10px 0; font-weight: bold; }
                    .song-message { color: #aaa; font-size: 16px; }
                    .btn { display: inline-block; background: linear-gradient(135deg, #e94560 0%%, #c92a4a 100%%); color: white; padding: 15px 40px; text-decoration: none; border-radius: 30px; font-weight: bold; font-size: 16px; margin-top: 20px; }
                    .footer { text-align: center; margin-top: 30px; color: #888; font-size: 12px; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="logo">🎶</div>
                        <h1>New Song Added!</h1>
                    </div>
                    <div class="content">
                        <p>Hey there, music lover!</p>
                        <div class="song-card">
                            <p class="song-name">🎵 %s</p>
                            <p class="song-message">%s</p>
                        </div>
                        <p>Don't miss out on this fresh track!</p>
                        <a href="http://localhost:9090/user/songs" class="btn">🎧 Listen Now</a>
                    </div>
                    <div class="footer">
                        <p>© 2024 Music Library. All rights reserved.</p>
                        <p>You received this because you're a registered user.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(dto.getSongName(), dto.getMessage());
    }
    
    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        System.out.println("📧 Connecting to SMTP server...");
        System.out.println("📧 From: " + fromEmail);
        System.out.println("📧 To: " + to);
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        
        mailSender.send(message);
        System.out.println("📧 Email sent successfully!");
    }
    
    private void logEmailDisabled(String type, String recipient) {
        System.out.println();
        System.out.println("⚠️  ═══════════════════════════════════════════════════════");
        System.out.println("⚠️  EMAIL DISABLED - " + type);
        System.out.println("⚠️  Would send to: " + recipient);
        System.out.println("⚠️  To enable: set app.mail.enabled=true in properties");
        System.out.println("⚠️  ═══════════════════════════════════════════════════════");
    }
    
    private String buildLoginNotificationHtml(LoginNotificationDTO dto) {
        String loginTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #1a1a2e; margin: 0; padding: 20px; }
                    .container { max-width: 600px; margin: 0 auto; background: linear-gradient(135deg, #16213e 0%%, #0f3460 100%%); border-radius: 20px; padding: 40px; }
                    .header { text-align: center; margin-bottom: 30px; }
                    .logo { font-size: 48px; margin-bottom: 10px; }
                    h1 { color: #28a745; margin: 0; font-size: 24px; }
                    .content { color: #ffffff; line-height: 1.8; }
                    .login-box { background: rgba(40, 167, 69, 0.2); border-left: 4px solid #28a745; border-radius: 0 15px 15px 0; padding: 20px; margin: 20px 0; }
                    .details-table { width: 100%%; margin-top: 15px; }
                    .details-table td { padding: 10px; border-bottom: 1px solid rgba(255,255,255,0.1); }
                    .details-table td:first-child { color: #aaa; width: 35%%; }
                    .details-table td:last-child { color: #fff; font-weight: bold; }
                    .warning-box { background: rgba(255, 193, 7, 0.2); border-radius: 10px; padding: 15px; margin-top: 20px; color: #ffc107; font-size: 14px; }
                    .footer { text-align: center; margin-top: 30px; color: #888; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="logo">🔐</div>
                        <h1>New Login Detected!</h1>
                    </div>
                    <div class="content">
                        <p>Hello <strong>%s</strong>,</p>
                        <p>We detected a new login to your Music Library account.</p>
                        
                        <div class="login-box">
                            <h3 style="margin-top: 0; color: #28a745;">✅ Login Details</h3>
                            <table class="details-table">
                                <tr><td>👤 Account:</td><td>%s</td></tr>
                                <tr><td>📧 Email:</td><td>%s</td></tr>
                                <tr><td>🔑 Account Type:</td><td>%s</td></tr>
                                <tr><td>🕐 Login Time:</td><td>%s</td></tr>
                                <tr><td>📍 Status:</td><td>✅ Successful</td></tr>
                            </table>
                        </div>
                        
                        <div class="warning-box">
                            ⚠️ <strong>Not you?</strong> If you didn't login, please secure your account immediately by changing your password.
                        </div>
                    </div>
                    <div class="footer">
                        <p>© 2024 Music Library. All rights reserved.</p>
                        <p>This is an automated security notification.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(dto.getName(), dto.getUsername(), dto.getToEmail(), dto.getUserType(), loginTime);
    }
    
    private String buildWelcomeEmailHtml(WelcomeEmailDTO dto) {
        String roleMessage = "ADMIN".equals(dto.getUserType()) 
            ? "As an administrator, you have full access to manage songs, view notifications, and control the music library."
            : "You can now browse songs, create playlists, and enjoy your favorite music!";
        
        String loginUrl = "ADMIN".equals(dto.getUserType()) 
            ? "http://localhost:9090/admin/login" 
            : "http://localhost:9090/user/login";
        
        String features = "ADMIN".equals(dto.getUserType()) 
            ? """
                <li>✅ Add, Edit, Delete Songs</li>
                <li>✅ Manage Song Visibility (Available/Not Available)</li>
                <li>✅ View All Notifications</li>
                <li>✅ Send Notifications to Users</li>
                <li>✅ Monitor User Activity</li>
              """
            : """
                <li>✅ Browse Available Songs</li>
                <li>✅ Search Songs by Name, Singer, Album</li>
                <li>✅ Create Custom Playlists</li>
                <li>✅ Add/Remove Songs from Playlists</li>
                <li>✅ Music Player with Play, Stop, Shuffle, Repeat</li>
              """;
        
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #1a1a2e; margin: 0; padding: 20px; }
                    .container { max-width: 600px; margin: 0 auto; background: linear-gradient(135deg, #16213e 0%%, #0f3460 100%%); border-radius: 20px; padding: 40px; }
                    .header { text-align: center; margin-bottom: 30px; }
                    .logo { font-size: 48px; margin-bottom: 10px; }
                    h1 { color: #e94560; margin: 0; font-size: 28px; }
                    .content { color: #ffffff; line-height: 1.8; }
                    .highlight { background: #e94560; color: white; padding: 3px 10px; border-radius: 5px; font-weight: bold; }
                    .details-box { background: rgba(255,255,255,0.1); border-radius: 15px; padding: 20px; margin: 20px 0; }
                    .details-box h3 { color: #e94560; margin-top: 0; }
                    .details-table { width: 100%%; }
                    .details-table td { padding: 8px 0; border-bottom: 1px solid rgba(255,255,255,0.1); }
                    .details-table td:first-child { color: #aaa; width: 40%%; }
                    .details-table td:last-child { color: #fff; font-weight: bold; }
                    .features-box { background: rgba(102, 126, 234, 0.2); border-radius: 15px; padding: 20px; margin: 20px 0; }
                    .features-box h3 { color: #667eea; margin-top: 0; }
                    .features-box ul { margin: 0; padding-left: 0; list-style: none; }
                    .features-box li { padding: 5px 0; }
                    .btn { display: inline-block; background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 15px 30px; text-decoration: none; border-radius: 30px; font-weight: bold; margin-top: 20px; }
                    .footer { text-align: center; margin-top: 30px; color: #888; font-size: 12px; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="logo">🎵</div>
                        <h1>Welcome to Music Library!</h1>
                    </div>
                    <div class="content">
                        <p>Hello <strong>%s</strong>,</p>
                        <p>Congratulations! Your account has been successfully created as a <span class="highlight">%s</span>!</p>
                        <p>%s</p>
                        
                        <div class="details-box">
                            <h3>📋 Your Account Details</h3>
                            <table class="details-table">
                                <tr><td>Full Name:</td><td>%s</td></tr>
                                <tr><td>Username:</td><td>%s</td></tr>
                                <tr><td>Email:</td><td>%s</td></tr>
                                <tr><td>Account Type:</td><td>%s</td></tr>
                                <tr><td>Status:</td><td>✅ Active</td></tr>
                            </table>
                        </div>
                        
                        <div class="features-box">
                            <h3>🚀 What You Can Do</h3>
                            <ul>
                                %s
                            </ul>
                        </div>
                        
                        <center>
                            <a href="%s" class="btn">🎧 Login Now</a>
                        </center>
                        
                        <p style="margin-top: 30px; font-size: 14px; color: #aaa;">
                            <strong>Quick Links:</strong><br>
                            🏠 Home: <a href="http://localhost:9090" style="color: #667eea;">http://localhost:9090</a><br>
                            📧 Support: support@musiclibrary.com
                        </p>
                    </div>
                    <div class="footer">
                        <p>© 2024 Music Library. All rights reserved.</p>
                        <p>This email was sent to %s</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
                dto.getName(), 
                dto.getUserType(), 
                roleMessage, 
                dto.getName(),
                dto.getUsername(),
                dto.getToEmail(),
                dto.getUserType(),
                features,
                loginUrl,
                dto.getToEmail()
            );
    }
    
    private String buildSongNotificationHtml(SongNotificationEmailDTO dto) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #1a1a2e; margin: 0; padding: 20px; }
                    .container { max-width: 600px; margin: 0 auto; background: linear-gradient(135deg, #16213e 0%%, #0f3460 100%%); border-radius: 20px; padding: 40px; }
                    .header { text-align: center; margin-bottom: 30px; }
                    .logo { font-size: 48px; margin-bottom: 10px; }
                    h1 { color: #e94560; margin: 0; font-size: 24px; }
                    .song-card { background: rgba(255,255,255,0.1); border-radius: 15px; padding: 25px; margin: 20px 0; text-align: center; }
                    .song-icon { font-size: 60px; margin-bottom: 15px; }
                    .song-title { color: #ffffff; font-size: 24px; margin: 0 0 15px 0; font-weight: bold; }
                    .song-details { background: rgba(0,0,0,0.2); border-radius: 10px; padding: 15px; margin-top: 15px; }
                    .song-details table { width: 100%%; }
                    .song-details td { padding: 8px; text-align: left; }
                    .song-details td:first-child { color: #aaa; width: 30%%; }
                    .song-details td:last-child { color: #fff; font-weight: bold; }
                    .btn { display: inline-block; background: linear-gradient(135deg, #e94560 0%%, #c92a4a 100%%); color: white; padding: 15px 40px; text-decoration: none; border-radius: 30px; font-weight: bold; font-size: 16px; }
                    .footer { text-align: center; margin-top: 30px; color: #888; font-size: 12px; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="logo">🎶</div>
                        <h1>New Song Added to Library!</h1>
                    </div>
                    <div class="song-card">
                        <div class="song-icon">🎵</div>
                        <p class="song-title">%s</p>
                        <div class="song-details">
                            <table>
                                <tr><td>🎤 Artist:</td><td>%s</td></tr>
                                <tr><td>💿 Album:</td><td>%s</td></tr>
                                <tr><td>📅 Added:</td><td>Just Now</td></tr>
                            </table>
                        </div>
                    </div>
                    <div style="text-align: center;">
                        <a href="http://localhost:9090/user/songs" class="btn">🎧 Listen Now</a>
                    </div>
                    <div class="footer">
                        <p>© 2024 Music Library. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(dto.getSongName(), dto.getSinger(), dto.getAlbumName());
    }
}
