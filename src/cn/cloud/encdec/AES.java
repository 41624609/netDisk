package cn.cloud.encdec;
import java.io.*;
import java.security.*;
import javax.crypto.spec.*;
import javax.crypto.*;

public class AES{ 
	String password;

public AES(String pw){
	password=pw;
}

public AES(){
	password="abcd11234567@";
}

 public String createKey(){
		 String key="";
		 try{     
				KeyGenerator kg= KeyGenerator.getInstance("AES");
	    		kg.init(128);
			    key= kg.generateKey().toString();
	     }catch(NoSuchAlgorithmException e){
	 		e.printStackTrace();
	 	 }
	     return key;
 }
 
 public String encrypt(String content) {   
	 String s="";
	 try {              
             KeyGenerator kgen = KeyGenerator.getInstance("AES");   
             kgen.init(128, new SecureRandom(password.getBytes()));   
             SecretKey secretKey = kgen.generateKey();   
             byte[] enCodeFormat = secretKey.getEncoded();   
             SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");   
             Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
             byte[] byteContent = content.getBytes("utf-8");   
             cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
             byte[] result = cipher.doFinal(byteContent);  
             s=parseByte2HexStr(result);
              // 加密   
     } catch (NoSuchAlgorithmException e) {   
             e.printStackTrace();   
     } catch (NoSuchPaddingException e) {   
             e.printStackTrace();   
     } catch (InvalidKeyException e) {   
             e.printStackTrace();   
     } catch (UnsupportedEncodingException e) {   
             e.printStackTrace();   
     } catch (IllegalBlockSizeException e) {   
             e.printStackTrace();   
     } catch (BadPaddingException e) {   
             e.printStackTrace();   
     }   
     return s;   
}  

 
 
 //解密
 public String decrypt(String bs)throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{   
	 String s="";
     try {   
    	      byte[] content=parseHexStr2Byte(bs);
              KeyGenerator kgen = KeyGenerator.getInstance("AES");   
              kgen.init(128, new SecureRandom(password.getBytes()));   
              SecretKey secretKey = kgen.generateKey();   
              byte[] enCodeFormat = secretKey.getEncoded();   
              SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");               
              Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
             cipher.init(Cipher.DECRYPT_MODE, key);// 初始化   
             byte[] result = cipher.doFinal(content);   
             s=new String(result, "UTF-8");
              // 加密   
     } catch (NoSuchAlgorithmException e) {   
             e.printStackTrace();   
     } catch (NoSuchPaddingException e) {   
             e.printStackTrace();   
     } catch (InvalidKeyException e) {   
             e.printStackTrace();   
     } catch (IllegalBlockSizeException e) {   
             e.printStackTrace();   
    	     //throw new IllegalBlockSizeException();
     } catch (BadPaddingException e) {   
             //e.printStackTrace();
    	     //System.out.println("BadPaddingException##############################");
         return null;
    	// throw new BadPaddingException();
     }   
     return s;   
}  


 public String parseByte2HexStr(byte buf[]) {
	 StringBuffer sb = new StringBuffer(); 
	 for(int i = 0; i < buf.length; i++){ 
		 String hex = Integer.toHexString(buf[i] & 0xFF);
		 if (hex.length() == 1){ 
			 hex = '0' + hex;
		 } 
		 sb.append(hex.toUpperCase()); 
	 } 
	 return sb.toString(); 
 } 
 
 public byte[] parseHexStr2Byte(String hexStr){
	 if (hexStr.length() < 1) return null; 
	 byte[] result = new byte[hexStr.length()/2]; 
	 for (int i = 0;i< hexStr.length()/2; i++) {
		 int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
		 int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
		 result[i] = (byte) (high * 16 + low); 
	 } 
	 return result; 
 } 
 
 public String readFile(String fileName){
 	String s="";    
    
 	try{  //System.out.println("----"+fileName);
 		 File file = new File("E:\\test\\kk\\txt1\\"+fileName+".txt");
         if(file.exists()){
        	 FileInputStream fis=new FileInputStream("E:\\test\\kk\\txt1\\"+fileName+".txt");
    		 int tt; 
    	     StringBuffer sb=new StringBuffer();   
    	     while((tt=fis.read())!=-1){   
    	         sb.append((char)tt); 
    	            
    	     }   
    	     s=sb.toString();
         }
 	}catch (FileNotFoundException e) {
 	   //e.printStackTrace();
 		//System.out.println("file not found!-----------------------------");
     }catch (IOException e){
 	   e.printStackTrace();
 	}   
     return s;    
 }


 public void storeInput(String s,String fileName){
 	try { 
			File f = new File("E:\\test\\kk\\txt1\\"+fileName+".txt"); 			
			BufferedWriter output = new BufferedWriter(new FileWriter(f)); 
			output.write(s); 
			output.close();   
		}catch (Exception e) { 
			    e.printStackTrace(); 
		} 		
 }
public static void main(String[] args){
	
		
	try{
		String s="我们好好bcd123";//"abcdefghij";//"";//"我们好好学习天天向上";//
		int len=30;
		String ss="";
		for(int i=0;i<len;i++) ss=ss+s;
		
		long t1=System.nanoTime();
		String key="aaaaaa"; 
		AES t=new AES(key);
		String a1=t.encrypt(ss);//加密		
		long t2=System.nanoTime();
		String a2=t.decrypt(a1); 
		long t3=System.nanoTime();
		
    	System.out.println((t2-t1)/Math.pow(10,6)+"\n"); //+(t3-t2)/Math.pow(10,6)+"\n"); 
    	System.out.println(a1.length());
    	
    	
   }catch (UnsupportedEncodingException e){
	   e.printStackTrace();
   } catch (IllegalBlockSizeException e){
	   e.printStackTrace();
   } catch (BadPaddingException e){
	   e.printStackTrace();
   }
	
 }
}

