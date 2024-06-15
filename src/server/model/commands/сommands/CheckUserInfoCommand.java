package server.model.commands.сommands;

import commonData.commandData.Command;
import commonData.requests.RequestUser;
import server.SQLController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Properties;

public class CheckUserInfoCommand implements Command<RequestUser> {
    private final SQLController sqlController;
    public CheckUserInfoCommand(SQLController sqlController){
        this.sqlController = sqlController;
    }
    @Override
    public String execute(RequestUser request) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        String salt = sqlController.getSalt(request);
        if (salt == null) {
            salt = generateSalt();
        }
        String password = request.getPassword() + salt + findPepper();
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] hashedBytes = md.digest(passwordBytes);
        request.setPassword(Base64.getEncoder().encodeToString(hashedBytes));

        return sqlController.checkUserInfo(request, salt);
    }
    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
    public String findPepper(){
        Properties pepperProp = new Properties();
        try (InputStream input = new FileInputStream("pepper.properties")){
            pepperProp.load(input);
            return pepperProp.getProperty("pepper");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
