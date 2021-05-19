
import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;
import java.util.Random;

import static java.math.BigInteger.ONE;

public class ElGamalAlice
{
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d)
	{
		return g.modPow(d,p);
	}

	private static BigInteger computeK(BigInteger p)
	{
		return BigInteger.probablePrime(1024-1, new Random());
	}

	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k)
	{
		return g.modPow(k,p);
	}

	private static BigInteger computeB(	String message, BigInteger d, BigInteger a, BigInteger k, BigInteger p)
	{
		// transfer the message to number
		String message_hash = String.valueOf(Math.abs(message.hashCode()));
		BigInteger m = new BigInteger(message_hash);
		BigInteger p_MinusOne = p.subtract(ONE);
		BigInteger k_Reverse = k.modInverse(p_MinusOne);
		BigInteger tmp1 = m.subtract(d.multiply(a));
		BigInteger tmp2 = tmp1.multiply(k_Reverse);
		BigInteger b = tmp2.mod(p_MinusOne);

		return b;
	}

	public static void main(String[] args) throws Exception
	{
		String message = "The quick brown fox jumps over the lazy dog.";
		//
		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		// You should consult BigInteger class in Java API documentation to find out what it is.
		BigInteger y, g, p; // public key
		BigInteger d; // private key

		int mStrength = 1024; // key bit length
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically strong pseudo-random number

		// Create a BigInterger with mStrength bit length that is highly likely to be prime.
		// (The '16' determines the probability that p is prime. Refer to BigInteger documentation.)
		p = new BigInteger(mStrength, 16, mSecureRandom);

		// Create a randomly generated BigInteger of length mStrength-1
		g = new BigInteger(mStrength-1, mSecureRandom);
		d = new BigInteger(mStrength-1, mSecureRandom);

		y = computeY(p, g, d);

		// At this point, you have both the public key and the private key. Now compute the signature.

		BigInteger k = computeK(p);
		BigInteger a = computeA(p, g, k);
		BigInteger b = computeB(message, d, a, k, p);

		// send public key
		os.writeObject(y);
		os.writeObject(g);
		os.writeObject(p);

		// send message
		os.writeObject(message);

		// send signature
		os.writeObject(a);
		os.writeObject(b);

		s.close();
	}
}