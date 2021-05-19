import java.io.*;
import java.net.*;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.*;


public class AliceSender
{
	public static void main(String[] args) throws Exception
	{
		String confidentiality = "used to test confidentiality";
		String integrity = "used to test integrity";
		String combination = "used to test confidentiality and integrity";

		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
		DataOutputStream out = new DataOutputStream(s.getOutputStream());


		// read Alice's private, and Bob's public key
		FileReader alicePriReader = new FileReader("./AlicePri.txt");
		BufferedReader bAlicePriReader = new BufferedReader(alicePriReader);
		String alicePri = bAlicePriReader.readLine();
		bAlicePriReader.close();
		alicePriReader.close();



		FileReader bobPubReader = new FileReader("./BobPub.txt");
		BufferedReader bBobReader = new BufferedReader(bobPubReader);
		String bobPub = bBobReader.readLine();
		bBobReader.close();
		bobPubReader.close();

		// test confidentiality, using Bob's public key to encode string "confidentiality"
		byte[] deBobPub = Base64.getDecoder().decode(bobPub);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(deBobPub));
		Cipher confiCipher = Cipher.getInstance("RSA");
		confiCipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String encodedConfidentiality = Base64.getEncoder().encodeToString(confiCipher.doFinal(confidentiality.getBytes("UTF-8")));


		// test Integrity, using Alice's private key to encode string "integrity"
		byte[] deAlicePri = Base64.getDecoder().decode(alicePri);
		RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(deAlicePri));
		Cipher integrityCipher = Cipher.getInstance("RSA");
		integrityCipher.init(Cipher.ENCRYPT_MODE, priKey);
		String encodedIntegrity = Base64.getEncoder().encodeToString(integrityCipher.doFinal(integrity.getBytes("UTF-8")));

		//test combination, using Alice's private key and Bob's public key to encode
		byte[] temp = integrityCipher.doFinal(combination.getBytes("UTF-8"));
		byte[] temp1 = new byte[64];
		byte[] temp2 = new byte[64];
		// because RAS only can encode byte <= 117, so should split it from 128 into 64
		System.arraycopy(temp, 0, temp1, 0, 64);
		System.arraycopy(temp, 64, temp2, 0, 64);
		byte[] combination1 = confiCipher.doFinal(temp1);
		byte[] combination2 = confiCipher.doFinal(temp2);
		byte[] combination3 = new byte[256];
		System.arraycopy(combination1, 0, combination3, 0, 128);
		System.arraycopy(combination2, 0, combination3, 128, 128);

		String encodedCombination = Base64.getEncoder().encodeToString(combination3);


		out.writeUTF(encodedConfidentiality);
		out.writeUTF(encodedIntegrity);
		out.writeUTF(encodedCombination);
		out.close();
		s.close();
	}
}