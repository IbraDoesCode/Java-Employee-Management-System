package service;

import util.CsvHandler;
import model.User;
import java.util.ArrayList;
import java.util.List;


public class UserCredentialService {

    private final String USER_CREDENTIAL_FILE = "Data/userCredentials.csv";

    private final CsvHandler csvHandler;
    private final List<String[]> userCredentialData;

    public UserCredentialService() {
        this.csvHandler = new CsvHandler(USER_CREDENTIAL_FILE);
        userCredentialData = csvHandler.retrieveCsvData();
    }

    private List<User> getAllUsers() {
        List<User> userCredentials = new ArrayList<>();

        for (int i = 1; i < userCredentialData.size(); i++) {
            String[] row = userCredentialData.get(i);
            userCredentials.add(new User(Integer.parseInt(row[0]), row[1], row[2], row[3], row[4]));
        }
        return userCredentials;
    }

    public User retrieveUserCredentialByUsername(String username)  {
        for (User user : getAllUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
