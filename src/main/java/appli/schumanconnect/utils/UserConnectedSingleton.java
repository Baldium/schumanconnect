package appli.schumanconnect.utils;

import appli.schumanconnect.model.*;

public class UserConnectedSingleton {

    private static UserConnectedSingleton instance;

    private User userConnected;

    private UserConnectedSingleton() {
        this.userConnected = null;
    }

    public static UserConnectedSingleton getInstance() {
        if (instance == null) {
            instance = new UserConnectedSingleton();
        }
        return instance;
    }

    public User getUserConnected() {
        return userConnected;
    }

    public void setUserConnected(User user) {
        this.userConnected = user;
    }

    public boolean isUserConnected() {
        return userConnected != null;
    }

    public void logout() {
        this.userConnected = null;
    }
}

