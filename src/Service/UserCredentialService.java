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

    public UserCredentialService() {
        this.csvHandler = new CsvHandler(userCredentialFile);
    }

    private List<User> retrieveUserCredentials() throws IOException, CsvException {
        List<String[]> data = csvHandler.retrieveCsvData(true);
        List<User> userCredentials = new ArrayList<>();

        for (String[] row : data) {
            userCredentials.add(new User(Integer.parseInt(row[0]), row[1], row[2], row[3], row[4]));
        }
        return userCredentials;
    }

    public User retrieveUserCredentialByUsername(String username) throws IOException, CsvException {
        for (User user : retrieveUserCredentials()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
