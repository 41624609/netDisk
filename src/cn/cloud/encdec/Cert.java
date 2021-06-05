package cn.cloud.encdec;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


public class Cert {

	public void produceCert(String myname,String username){
		String res="";
		try{
			RSA1 arsa=new RSA1();
			String sp=myname+"&"+username;
			byte[] b1=arsa.StringTobytes(sp);
			byte[] b2=arsa.encrypt(b1); 
			res=arsa.bytesToString(b2);
			ObjectOutputStream out;
			out = new ObjectOutputStream(new FileOutputStream("D:\\qingyunclient\\test\\client\\cert_"+username+".txt"));
			out.writeObject(res);
			out.close();	//System.out.println(res);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	
	public void produceCert0(String myname,String username){
		String res="";
		try{
			RSA1 arsa=new RSA1();
			String sp=username+"&"+myname;//与原来的顺序反过来
			byte[] b1=arsa.StringTobytes(sp);
			byte[] b2=arsa.encrypt(b1); 
			res=arsa.bytesToString(b2);
			ObjectOutputStream out;
			out = new ObjectOutputStream(new FileOutputStream("D:\\qingyunclient\\test\\client\\cert_"+username+"_"+myname+"_0.txt"));
			out.writeObject(res);
			out.close();	//System.out.println(res);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	
	public void produceCert0(String myname,String username,int type){//type =0表示读的权限，=1表示读写的权限
		String res="";
		try{
			RSA1 arsa=new RSA1();
			String sp=username+"&"+myname+"&"+type;//与原来的顺序反过来
			byte[] b1=arsa.StringTobytes(sp);
			byte[] b2=arsa.encrypt(b1); 
			res=arsa.bytesToString(b2);
			ObjectOutputStream out;
			out = new ObjectOutputStream(new FileOutputStream("D:\\qingyunclient\\test\\client\\cert_"+username+"_"+myname+"_0.txt"));
			out.writeObject(res);
			out.close();	//System.out.println(res);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public void produceCert1(String myname,String username,String aeskey,String rsakey1,String rsakey2,long opeart_k,int cescmc_k,int cescmc_n){
		String res="";
		try{
			RSA1 arsa=new RSA1();
			String sp=username+"&"+myname+"&"+aeskey+"&"+rsakey1+"&"+rsakey2+"&"+opeart_k+"&"+cescmc_k+"&"+cescmc_n;
			byte[] b1=arsa.StringTobytes(sp);
			byte[] b2=arsa.encrypt(b1); 
			res=arsa.bytesToString(b2);
			ObjectOutputStream out;
			out = new ObjectOutputStream(new FileOutputStream("D:\\qingyunclient\\test\\client\\cert_"+username+"_"+myname+"_1.txt"));
			out.writeObject(res);
			out.close();	//System.out.println(res);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	
	public String getCert(String username){
		
		String res="";
		try{
			RSA1 arsa=new RSA1();
			ObjectInputStream in;
			in = new ObjectInputStream(new FileInputStream("D:\\qingyunclient\\test\\client\\cert_"+username+".txt"));		
			res = (String) in.readObject();		
		    byte[] b1=arsa.StringTobytes(res);
			byte[] b2=arsa.decrypt(b1); 
			res=arsa.bytesToString(b2);
			
			System.out.println(res);
			in.close();	
		}catch(Exception e){
			e.printStackTrace();
		}	
		return res;	
	}
	
public String getCert0(String myname,String username){
		
		String res="";
		try{
			RSA1 arsa=new RSA1();
			ObjectInputStream in;
			in = new ObjectInputStream(new FileInputStream("D:\\qingyunclient\\test\\client\\cert_"+username+"_"+myname+"_0.txt"));		
			res = (String) in.readObject();		
		    byte[] b1=arsa.StringTobytes(res);
			byte[] b2=arsa.decrypt(b1); 
			res=arsa.bytesToString(b2);
			
			System.out.println(res);
			in.close();	
		}catch(Exception e){
			e.printStackTrace();
		}	
		return res;	
	}
	
public String getCert1(String myname,String username){
	
	String res="";
	try{
		RSA1 arsa=new RSA1();
		ObjectInputStream in;
		in = new ObjectInputStream(new FileInputStream("D:\\qingyunclient\\test\\client\\cert_"+username+"_"+myname+"_1.txt"));		
		res = (String) in.readObject();		
	    byte[] b1=arsa.StringTobytes(res);
		byte[] b2=arsa.decrypt(b1); 
		res=arsa.bytesToString(b2);
		
		System.out.println(res);
		in.close();	
	}catch(Exception e){
		e.printStackTrace();
	}	
	return res;	
}
	
	
	public static void main(String[] args) {
		Cert cert=new Cert();
		//cert.produceCert("hrw", "xufan");
		cert.getCert("xufan");

	}

}

