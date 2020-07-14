package blockchain.common;

import java.security.*;

public class SecureKeys {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public SecureKeys(int keyLength) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keyLength);
        KeyPair pair = keyGen.generateKeyPair();

        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public byte[] sign(byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(privateKey);
        rsa.update(data);
        return rsa.sign();
    }
}
