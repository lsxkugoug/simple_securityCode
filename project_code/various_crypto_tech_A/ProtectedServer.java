
import java.io.*;
import java.net.*;
import java.security.*;
public class ProtectedServer
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException 
	{
		DataInputStream in = new DataInputStream(inStream);
		
		String user = in.readUTF();
		double q1 = in.readDouble();
		long t1 = in.readLong();
		double q2 = in.readDouble();
		long t2 = in.readLong();
		String password = lookupPassword(user);
		byte[] diggest1 = Protection.makeDigest(user, password, t1, q1);
		byte[] diggest2 = Protection.makeDigest(diggest1, t2, q2);

		byte[] diggest2Test = in.readAllBytes();

		//compare diggest2 == diggest2Test
		boolean res = true;
		if (diggest2.length == diggest2Test.length) {
			for (int i = 0; i < diggest2.length; ++i) {
				if (diggest2[i] != diggest2Test[i]) {
					res = false;
				}
			}
			return res;
		}
		System.out.println(user);
		return res;
		// IMPLEMENT THIS FUNCTION.
	}

	protected String lookupPassword(String user) { return "abc123"; }

	public static void main(String[] args) throws Exception 
	{	

		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();

		ProtectedServer server = new ProtectedServer();

		if (server.authenticate(client.getInputStream()))
		  System.out.println("Client logged in.");
		else
		  System.out.println("Client failed to log in.");

		s.close();
	}
}