package appli.schumanconnect.service;


public class ServiceFactory {

    public static PasswordResetService getPasswordResetService() {
        return new PasswordResetService();
    }

    public static EmailService getEmailService() {
        return new EmailService();
    }
}
