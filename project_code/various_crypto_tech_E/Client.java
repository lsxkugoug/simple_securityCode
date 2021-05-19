import java.io.*;
import java.net.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;
import javax.crypto.*;


public class Client
{
	public static void main(String[] args) throws Exception
	{
		String confidentiality = "used to test confidentiality";

		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
		DataOutputStream out = new DataOutputStream(s.getOutputStream());


		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate cert = (X509Certificate)cf.generateCertificate(new FileInputStream("./myCertificate.crt"));
		PublicKey publicKey = cert.getPublicKey();
		byte[] signature=cert.getSignature();
		System.out.println("information of received certificate:"+cert.toString());

		System.out.println("the Issuer is "+cert.getIssuerDN());
		System.out.println("the Subject is "+cert.getSubjectDN());

		Cipher confiCipher = Cipher.getInstance("RSA");
		confiCipher.init(Cipher.DECRYPT_MODE, publicKey);
		byte [] sig=cert.getSignature();//签名值
		confiCipher.doFinal(sig);
		System.out.println("the signature can be decoded by the public key. the signature validation is ok");
		cert.checkValidity(new Date());
		System.out.println("the certificate is valid");


		// test confidentiality, using Bob's public key to encode string "confidentiality"
		confiCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		String encodedConfidentiality = Base64.getEncoder().encodeToString(confiCipher.doFinal(confidentiality.getBytes("UTF-8")));

		out.writeUTF(encodedConfidentiality);
		out.close();
		s.close();
	}
}