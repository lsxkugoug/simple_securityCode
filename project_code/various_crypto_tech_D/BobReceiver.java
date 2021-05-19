import java.io.*;
import java.net.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.*;


public class BobReceiver
{
	public static void main(String[] args) throws Exception
	{
		int port = 7999;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		DataInputStream in = new DataInputStream(s.getInputStream());
		//receive the encoded data
		String encodedConfidentiality = in.readUTF();
		String encodedIntegrity = in.readUTF();
		String combination = in.readUTF();

		//get AlicePublic key
		FileReader alicePubReader = new FileReader("./AlicePub.txt");
		BufferedReader bAlicePubReader = new BufferedReader(alicePubReader);
		String alicePub = bAlicePubReader.readLine();
		bAlicePubReader.close();
		alicePubReader.close();

		// get Bob's private key
		FileReader bobPriReader = new FileReader("./BobPri.txt");
		BufferedReader bBobPriReader = new BufferedReader(bobPriReader);
		String bobPri = bBobPriReader.readLine();
		bBobPriReader.close();
		bobPriReader.close();


		// test confidentiality, using Bob's private key to decode string encodedConfidentiality, which is ecoded by Bob's public key
		byte[] deBobPri = Base64.getDecoder().decode(bobPri);
		RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(deBobPri));
		Cipher conDecipher = Cipher.getInstance("RSA");
		conDecipher.init(Cipher.DECRYPT_MODE, priKey);
		byte[] bytesConfidentiality = Base64.getDecoder().decode(encodedConfidentiality.getBytes("UTF-8"));
		String confidentiality = new String(conDecipher.doFinal(bytesConfidentiality));
		System.out.println(confidentiality);


		// test integrity, using Alice's public key to decode string encodedIntegrity, which is ecoded by Alice's public ket
		byte[] deAlicePub = Base64.getDecoder().decode(alicePub);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(deAlicePub));
		Cipher integrityCipher = Cipher.getInstance("RSA");
		integrityCipher.init(Cipher.DECRYPT_MODE, pubKey);
		byte[] bytesIntegrity = Base64.getDecoder().decode(encodedIntegrity.getBytes("UTF-8"));
		String integrity = new String(integrityCipher.doFinal(bytesIntegrity));
		System.out.println(integrity);

//		test combination, use Bob's private key and Alice public key to decode
		byte[] bytesCombination3 = Base64.getDecoder().decode(combination.getBytes("UTF-8"));
		byte[] combination1 = new byte[128];
		byte[] combination2 = new byte[128];
		System.arraycopy(bytesCombination3, 0, combination1, 0, 128);
		System.arraycopy(bytesCombination3, 128, combination2, 0, 128);

		byte[] temp1 = conDecipher.doFinal(combination1);
		byte[] temp2 = conDecipher.doFinal(combination2);
		byte[] temp = new byte[128];
		System.arraycopy(temp1, 0, temp, 0, 64);
		System.arraycopy(temp2, 0, temp, 64, 64);
		String combinationS = new String(integrityCipher.doFinal(temp));
		System.out.println(combinationS);
	}
}