package Service;

import Model.User;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;

public class AuthenticationService {

    private final UserCredentialRepository userCredentialRepository;

    public AuthenticationService () {
        this.userCredentialRepository = new UserCredentialRepository();
    }

    public boolean authenticate(String username, String password) throws IOException, CsvException {
        User user = userCredentialRepository.retrieveUserCredentialByUsername(username);
        return user != null && user.isValidCredentials(username, password);
    }
}
