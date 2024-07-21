package Service;

import Util.CsvHandler;
import Model.User;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserCredentialService {

    private CsvHandler csvHandler;
    private String userCredentialFile = "Data/userCredentials.csv";
    List<String[]> userCredentialData;

    public UserCredentialService() {
        this.csvHandler = new CsvHandler(userCredentialFile);

        try {
            userCredentialData = csvHandler.retrieveCsvData(true);
        } catch (IOException e) {
            throw new RuntimeException("An error has occurred while initializing user credential data", e);
        }
    }

    private List<User> retrieveListOfUserObject() throws IOException, CsvException {
        List<User> userCredentials = new ArrayList<>();

        for (String[] row : userCredentialData) {
            userCredentials.add(new User(Integer.parseInt(row[0]), row[1], row[2], row[3], row[4]));
        }
        return userCredentials;
    }

    public User retrieveUserCredentialByUsername(String username) throws IOException, CsvException {
        for (User user : retrieveListOfUserObject()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
