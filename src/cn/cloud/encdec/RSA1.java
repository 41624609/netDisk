package cn.cloud.encdec;

import java.security.KeyPair;
import java.security.KeyPairGenerator;  
import java.security.interfaces.RSAPrivateKey; 
import java.security.interfaces.RSAPublicKey; 
import javax.crypto.Cipher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;


public class RSA1{ 
	public RSAPrivateKey privateKey;
	public RSAPublicKey publicKey;
	public String KEY_FILENAME = "D:\\qingyunclient\\test\\client\\SPkey.dat";
	
	public RSA1(int length)throws Exception{
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(length); 
		KeyPair keys = keyPairGen.generateKeyPair(); 
		privateKey= (RSAPrivateKey) keys.getPrivate();
		publicKey= (RSAPublicKey) keys.getPublic();		
		ObjectOutputStream out;
		out = new ObjectOutputStream(new FileOutputStream(KEY_FILENAME));
		out.writeObject(keys);
		out.close();	
	}
	
	public RSA1()throws Exception{
		ObjectInputStream in;
		in = new ObjectInputStream(new FileInputStream(KEY_FILENAME));		
		KeyPair keys = (KeyPair) in.readObject();
		privateKey= (RSAPrivateKey) keys.getPrivate();
		publicKey= (RSAPublicKey) keys.getPublic();	
		in.close();
		
	}
	
	public String bytesToString(byte[] encrytpByte){
		String result = ""; // System.out.println(encrytpByte.length);
		for (Byte bytes : encrytpByte){
			result += (char) bytes.intValue(); 
		} 
		return result;
	}
	
	public byte[] StringTobytes(String s){
		byte[] bt=new byte[s.length()];
		
		for (int i=0;i<s.length();i++){
			bt[i]=(byte)s.charAt(i);			
		} 
		return bt;
	}
	
	public byte[] encrypt(byte[] obj)throws Exception{
		if (publicKey != null){ 
			try{
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
				return cipher.doFinal(obj);
			} catch (Exception e){
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	public byte[] decrypt(byte[] obj) throws Exception{ 
		if (privateKey != null) {
			try{
				Cipher cipher = Cipher.getInstance("RSA"); 
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
				return cipher.doFinal(obj); 
			}catch (Exception e){
				e.printStackTrace();
			} 
		} 
		return null;
	}
	
	public static void main(String[] args){
		try{
			
			RSA1 rsa = new RSA1(512); 
			
			String text2 = "ÎÒÃÇcomputingaaa";
			byte[] e = rsa.encrypt(text2.getBytes());
			String enStr=rsa.bytesToString(e);               
			byte[] de = rsa.StringTobytes(enStr); 
			String str=rsa.bytesToString(rsa.decrypt(de));   //rsa.bytesToString(de);
			//System.out.println(enStr+",  "+str);	
			System.out.println(str);	
		
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
    }
	
}
