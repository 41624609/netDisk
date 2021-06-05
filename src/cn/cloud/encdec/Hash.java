package cn.cloud.encdec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class Hash {
	public BigInteger m;
	
	public Hash(int mm){
		m=new BigInteger(mm+"");
	}   

    public String MD5(String str) throws NoSuchAlgorithmException {
        if (str == null || str.length() == 0) {
            return "";
        }

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(str.getBytes());
        byte[] md5Bytes = messageDigest.digest();
        return byte2hex(md5Bytes);
    }

    public String SHA1(String str) throws NoSuchAlgorithmException {
        if (str == null || str.length() == 0) {
            return "";
        }

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update(str.getBytes());
        byte[] sha1Bytes = messageDigest.digest();
        return byte2hex(sha1Bytes);
    }

    public String byte2hex(byte[] b) //二行制转字符串
    {
        String hs = "";
        String stmp = "";
        String hs1="";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if (n < b.length - 1)
                hs = hs + ":";
            hs1=hs1+Integer.valueOf(stmp,16).toString();             
        }
        BigInteger a=new BigInteger(hs1);
        a=a.mod(m);
        return a.toString();
    }
    
    public static void main(String[] args) {

    //    String str = "12345";
     //   Hash ha=new Hash(200);        
       
    }

}

