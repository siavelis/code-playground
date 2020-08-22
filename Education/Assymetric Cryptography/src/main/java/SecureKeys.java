import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

public class SecureKeys {

    private final String signatureAlgorithm;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public SecureKeys(int keyLength) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keyLength);
        KeyPair pair = keyGen.generateKeyPair();

        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
        this.signatureAlgorithm = "SHA1withRSA";
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public byte[] sign(byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        Signature rsa = Signature.getInstance(signatureAlgorithm);
        rsa.initSign(privateKey);
        rsa.update(data);
        return rsa.sign();
    }

    public boolean verify(byte[] data, byte[] sign) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        final Signature sig = Signature.getInstance(signatureAlgorithm);
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(sign);
    }

    public void savePublicKey(String filename) throws IOException {
        // Store Public Key
        final File publicKeyFile = new File(filename);
        publicKeyFile.createNewFile();

        String encodedString =
            "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getEncoder().encodeToString(publicKey.getEncoded())
                + "\n-----END PUBLIC KEY-----\n";

        try {
            try (OutputStream fos = Files.newOutputStream(publicKeyFile.toPath())) {
                fos.write(encodedString.getBytes());
            }
        } catch (IOException exception) {
            exception.printStackTrace();

        }
    }
}
