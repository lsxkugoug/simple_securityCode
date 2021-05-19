

import java.io.*;
import java.security.*;

public class Protection
{
	public static byte[] makeBytes(long t, double q) 
	{    
		try 
		{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			DataOutputStream dataOut = new DataOutputStream(byteOut);
			dataOut.writeLong(t);
			dataOut.writeDouble(q);
			return byteOut.toByteArray();
		}
		catch (IOException e) 
		{
			return new byte[0];
		}
	}	

	//第二阶段的 random2,timestamp2 以及hash(第一阶段)的digest
	public static byte[] makeDigest(byte[] mush, long t2, double q2) throws NoSuchAlgorithmException 
	{
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(mush);
		md.update(makeBytes(t2, q2)); //使用盐值进行加密 t2 q2合并之后是一个random
		return md.digest();
	}
	
	//第一阶段的输入
	public static byte[] makeDigest(String user, String password,
									long t1, double q1)
									throws NoSuchAlgorithmException, UnsupportedEncodingException
	{	
		MessageDigest md = MessageDigest.getInstance("SHA");
		byte[] s1 = user.getBytes("utf-8");
		byte[] s2 = password.getBytes("utf-8");
		md.update(s1);
		md.update(s2);
		md.update(makeBytes(t1, q1));
		return md.digest();
	}
}