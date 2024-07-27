package Service;

import Util.CsvHandler;
import Model.User;
import java.util.ArrayList;
import java.util.List;

public class UserCredentialService {

    private final CsvHandler csvHandler;

    private final String USER_CREDENTIAL_FILE = "Data/userCredentials.csv";

    private final List<String[]> userCredentialData;

    public UserCredentialService() {
        this.csvHandler = new CsvHandler(USER_CREDENTIAL_FILE);

        userCredentialData = csvHandler.retrieveCsvData(true);

    }

    private List<User> retrieveListOfUserObject() {
        List<User> userCredentials = new ArrayList<>();

        for (String[] row : userCredentialData) {
            userCredentials.add(new User(Integer.parseInt(row[0]), row[1], row[2], row[3], row[4]));
        }
        return userCredentials;
    }

    public User retrieveUserCredentialByUsername(String username)  {
        for (User user : retrieveListOfUserObject()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
