package appli.schumanconnect.controller;

public class FlashMessage extends RuntimeException {
    public FlashMessage(String message) {
        super(message);
    }
}
