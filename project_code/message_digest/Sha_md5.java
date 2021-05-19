import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Sha_md5{

    public static byte[] sha(String s) throws NoSuchAlgorithmException 
	{
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(s.getBytes());
		return md.digest();
	}
    
    public static byte[] md5(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException 
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(s.getBytes("utf-8"));
		return md.digest();
	}

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Scanner sc = new Scanner(System.in);
        System.out.println("what methods do you want to choose?");
        System.out.println("1->SHA");
        System.out.println("2->MD5");
        String method = sc.next();
        if(!method.equals("1") && !method.equals("2")){
            System.out.println("please enter the right number!");
            System.exit(1);
        }
        System.out.println("please enter the string you want to hash");
        String s = sc.next();
        byte[] sss;
        if(method=="1"){
             sss = sha(s);
        }else{
             sss = md5(s);
        }
       
        String md5Str = new BigInteger(1, sss).toString(16);
        System.out.println(md5Str);
    }
}