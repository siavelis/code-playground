package blockchain.common;

import java.io.*;

public class Serialization<T> {
    public static <T> byte[] writeObject(T data) throws SerializerException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            final ObjectOutputStream bos = new ObjectOutputStream(byteArrayOutputStream);
            bos.writeObject(data);
            bos.flush();
            bos.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializerException("Error during serialization. Root cause: " + e.getMessage(), e);
        }
    }

    public static <T> T readObject(byte[] data) throws SerializerException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        try {
            final ObjectInputStream bis = new ObjectInputStream(byteArrayInputStream);
            return (T) bis.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializerException("Error during serialization. Root cause: " + e.getMessage(), e);
        }
    }
}