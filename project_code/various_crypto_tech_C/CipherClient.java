import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class CipherClient
{
	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);

		// YOU NEED TO DO THESE STEPS:
		// -Generate a DES key.

		SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
		String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		// -Store it in a file.
		File fp = new File("./secrete_key.txt");
		PrintWriter pfp = new PrintWriter(fp);
		pfp.print(encodedKey);
		pfp.close();
		// -Use the key to encrypt the message above and send it over socket s to the server.
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] enciphered_message = cipher.doFinal(message.getBytes());
		DataOutputStream out = new DataOutputStream(s.getOutputStream());
		out.write(enciphered_message);
		out.close();

	}
}