import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Application {
    public static void main(String[] args) throws IOException {
        SecureKeys keys;
        try {
            keys = new SecureKeys(1024);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error during keys initialization. Root cause: " + e.getMessage(), e);
        }

        keys.savePublicKey("public_key");
    }
}
