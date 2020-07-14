package blockchain;

import blockchain.common.Serialization;
import blockchain.common.SerializerException;
import blockchain.common.SecureKeys;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;

public class Wallet {
    private final String walletId;
    private final SecureKeys keys;

    public Wallet(String walletId, SecureKeys keys) {
        this.walletId = walletId;
        this.keys = keys;
    }

    public byte[] signData(byte[] data) {
        try {
            return keys.sign(data);
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            throw new RuntimeException("Error during signing. Root cause: " + e.getMessage(), e);
        }
    }

    public byte[] serializeObject(Serializable serializable) {
        try {
            return Serialization.writeObject(serializable);
        } catch (SerializerException e) {
            throw new RuntimeException("Error during serialization. Root cause: " + e.getMessage(), e);
        }
    }

    public String getWalletId() {
        return walletId;
    }

    public PublicKey getPublicKey() {
        return keys.getPublicKey();
    }
}
