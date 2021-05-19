import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Base64;
import javax.crypto.*;


public class Server
{
	public static void main(String[] args) throws Exception
	{
		int port = 7999;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		DataInputStream in = new DataInputStream(s.getInputStream());
		//receive the encoded data
		String encodedConfidentiality = in.readUTF();
		FileInputStream fis2 = new FileInputStream("./keystore.p12");
		KeyStore ks = KeyStore.getInstance("PKCS12");         // load associated item
		char[] keypwd = "password".toCharArray();
		ks.load(fis2, keypwd);              // load keystore
		PrivateKey privateKey = (PrivateKey)ks.getKey("server", keypwd);


		// test confidentiality, using Bob's private key to decode string encodedConfidentiality, which is ecoded by Bob's public key
		Cipher conDecipher = Cipher.getInstance("RSA");
		conDecipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] bytesConfidentiality = Base64.getDecoder().decode(encodedConfidentiality.getBytes("UTF-8"));
		String confidentiality = new String(conDecipher.doFinal(bytesConfidentiality));
		System.out.println(confidentiality);

	}
}