package edu.brown.cs.student.main;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApiDriverTest {
    public ApiDriver getApiDriver() throws IOException {
        String clientId = Files.readString(Path.of("secret/client_id.txt"));
        String clientSecret = Files.readString(Path.of("secret/client_secret.txt"));
        return new ApiDriver(Main.OUR_URL, "callback", clientId, clientSecret);
    }

    @Test
    public void startApiTest() {


    }
}
