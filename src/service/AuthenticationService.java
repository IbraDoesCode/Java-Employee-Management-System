package service;

import model.User;

public class AuthenticationService {

    private final UserCredentialService userCredentialService;

    public AuthenticationService () {
        this.userCredentialService = new UserCredentialService();
    }

    public boolean authenticate(String username, String password) {
        User user = userCredentialService.retrieveUserCredentialByUsername(username);
        return user != null && checkPassword(user, password);
    }

    public boolean checkPassword(User user, String password) {
        return user.getPassword().equals(password);
    }

}
