package service;

import util.CsvHandler;
import model.User;
import java.util.ArrayList;
import java.util.List;


public class UserCredentialService {

    private final List<String[]> userCredentialData;

    public UserCredentialService() {
        CsvHandler csvHandler = new CsvHandler("src/database/userCredentials.csv");
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
