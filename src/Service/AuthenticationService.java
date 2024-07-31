package Service;

import Model.User;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;

public class AuthenticationService {

    private final UserCredentialService userCredentialService;

    public AuthenticationService () {
        this.userCredentialService = new UserCredentialService();
    }

    public boolean authenticate(String username, String password) throws IOException, CsvException {
        User user = userCredentialService.retrieveUserCredentialByUsername(username);
        return user != null && user.isValidCredentials(username, password);
    }
}
