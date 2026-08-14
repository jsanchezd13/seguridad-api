package gt.edu.umg.utils;

import java.io.File;
import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class KeyUtils {

    public static void generateKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Guardar llave privada
        String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" +
                Base64.getMimeEncoder().encodeToString(privateKey.getEncoded()) +
                "\n-----END PRIVATE KEY-----";
        
        try (FileWriter writer = new FileWriter(new File("src/main/resources/keys/private_key.pem"))) {
            writer.write(privateKeyPEM);
        }

        // Guardar llave pública
        String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getMimeEncoder().encodeToString(publicKey.getEncoded()) +
                "\n-----END PUBLIC KEY-----";
        
        try (FileWriter writer = new FileWriter(new File("src/main/resources/keys/public_key.pem"))) {
            writer.write(publicKeyPEM);
        }

        System.out.println("Llaves generadas exitosamente!");
        System.out.println("Ubicación: src/main/resources/keys/");
    }

    public static void main(String[] args) throws Exception {
        generateKeys();
    }
}
