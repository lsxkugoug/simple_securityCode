import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;


public class RASGenerator {
    public static void generateAlice() throws NoSuchAlgorithmException, FileNotFoundException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024,new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String privateKeyString = new String(Base64.getEncoder().encode(privateKey.getEncoded()));
        String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
        File alicePub = new File("./AlicePub.txt");
        File alicePri = new File("./AlicePri.txt");
        PrintWriter prPub = new PrintWriter(alicePub);
        PrintWriter prPri = new PrintWriter(alicePri);
        prPub.print(publicKeyString);
        prPri.print(privateKeyString);
        prPub.close();
        prPri.close();
    }
    public static void generateBob() throws FileNotFoundException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024,new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String privateKeyString = new String(Base64.getEncoder().encode(privateKey.getEncoded()));
        String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
        File alicePub = new File("./BobPub.txt");
        File alicePri = new File("./BobPri.txt");
        PrintWriter prPub = new PrintWriter(alicePub);
        PrintWriter prPri = new PrintWriter(alicePri);
        prPub.print(publicKeyString);
        prPri.print(privateKeyString);
        prPub.close();
        prPri.close();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException {
        generateAlice();
        generateBob();
    }

}
