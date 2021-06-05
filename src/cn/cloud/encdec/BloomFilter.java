package cn.cloud.encdec;

import java.io.File;
import java.math.BigInteger;

public class BloomFilter{
	public int m; //位向量长度
	public int h;  //hash函数个数
	public int k;  //keyword个数
	public byte[] mm;
	
	public BloomFilter(int m1,int h1, int k1){
		m=m1;
		h=h1;
		k=k1;
		mm=new byte[m];
		for(int i=0;i<m;i++) mm[i]=0;					
	}
	
	public BloomFilter(){
		h=(int)(Math.log(0.001)/Math.log(0.5))+1;   //hash个数
		k=50;                                         //kw个数
		m=744;  
		mm=new byte[m];
		for(int i=0;i<m;i++) mm[i]=0;	
	}
	
	
	public int[] hash4(BigInteger key)  //double hashing scheme
	{
		Hash hash=new Hash(m);
		int[] result=new int[h];		
		BigInteger tmp;
		String tmp1,tmp2;    
		try{
			
			tmp1=hash.SHA1(key.toString());
			tmp2=hash.MD5(key.toString());
			
			tmp=new BigInteger(tmp1);
			for(int i=0;i<h;i++){				
				tmp=tmp.add(new BigInteger(i+"").multiply(new BigInteger(tmp2)));
				tmp=tmp.add(new BigInteger(i*i*i*i+"")).mod(new BigInteger(m+""));
				result[i]=tmp.intValue();    //已经减过1了
				//System.out.println(result[i]);
			} 
		} catch (Exception e) {
            System.out.println(e);
        }
		return result;
	}
	
	public void insert(BigInteger key,int tag){		
		int[] pos;		
		pos=hash4(key);
		for(int i=0;i<h;i++) mm[pos[i]]=1;
	}
	
	public int query(BigInteger key,int tag){
		int str=1;
		int[] pos;		
		pos=hash4(key);
		for(int i=0;i<h;i++){
			if(mm[pos[i]]==0) str=0; //true;
			 //false;
		} 
		return str;
	//	if(str==false) System.out.println("No");
	//	else System.out.println("Yes");
	}
	
	public double false_rate(){
		double result1=0,m1=(double)m;
		result1=Math.pow(1-1/m1,k*h);  
		result1=Math.pow(1-result1,h);
		//System.out.println(result1);
		return result1;
	}
	
	public BigInteger[] ran_encrypt(String[] aa,String pub_key){	//加密关键字		
		unpadded_RSA rsa=new unpadded_RSA();  
		BigInteger[] result=new BigInteger[aa.length];
		for(int i=0;i<aa.length;i++){
			result[i]=rsa.encrypt(aa[i],pub_key);			
		}
		return result;
	}	
	
    public BigInteger ran_encrypt_para(String aa,String pub_key){	//加密检索参数	
    	unpadded_RSA rsa=new unpadded_RSA();  
		BigInteger result=rsa.encrypt(aa,pub_key);			
		return result;
	}	
	
	public void kwInsert(String[] aa,int rand){
		BigInteger[] kw=new BigInteger[aa.length];
		try{
			for(int i=0;i<kw.length;i++){
				kw[i]=new BigInteger(aa[i]+rand);
				int[] pos=hash4(kw[i]);  
				for(int j=0;j<pos.length;j++){	
					mm[pos[j]]=1;
				} 
			}
			
		}catch(Exception e){
			System.out.println(e);
		}
		
	}
	
	public static void main(String args[]) {	
		
		
	}

}

