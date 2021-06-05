package cn.cloud.encdec;

import java.math.BigInteger;
public class unpadded_RSA {

	public BigInteger p;
    public BigInteger q;
    public BigInteger n;
    public BigInteger fn;
    public BigInteger e;
    public BigInteger d1;
    
    public unpadded_RSA()
    {
    	p=new BigInteger("71593");   //私钥
        q=new BigInteger("77041");   //私钥
    	n=p.multiply(q);              //公钥
    	BigInteger temp=new BigInteger("1");
    	fn=p.subtract(temp).multiply(q.subtract(temp));
    	e=new BigInteger("71");
    	d1=extendedEuclid(e,fn);
    }    
    
    public unpadded_RSA(String s1,String s2)
    {
    	p=new BigInteger(s1);
        q=new BigInteger(s2);
    	n=p.multiply(q);
    	BigInteger temp=new BigInteger("1");
    	fn=p.subtract(temp).multiply(q.subtract(temp));
    	e=new BigInteger("71");
    	d1=extendedEuclid(e,fn);
    }    
    
    public BigInteger extendedEuclid(BigInteger d,BigInteger p){  //求d^-1 mod p
		BigInteger x1,x2,x3,y1,y2,y3,t1,t2,t3,z;
		x1=new BigInteger("0");  x2=new BigInteger("0"); x3=p;
		y1=new BigInteger("0");  y2=new BigInteger("1"); y3=d;
		z=new BigInteger("0");
		while(true){
			if(y3.compareTo(z)==0){
				d=new BigInteger("-1");
				break;
			}else if(y3.compareTo(new BigInteger("1"))==0){
				d=y2;
				break;
			}else{
				d=x3.divide(y3);
				t1=x1.subtract(d.multiply(y1));
				t2=x2.subtract(d.multiply(y2));
				t3=x3.subtract(d.multiply(y3));
				x1=y1; x2=y2; x3=y3;
				y1=t1; y2=t2; y3=t3; 
			}
		}
		if(d.compareTo(z)<0) d=p.add(d);
		return d;		
	}
    
    public BigInteger StrtoBig(String s){    	
    	String sum="";
    	for(int i=0;i<s.length();i++){
    		int c=(int)s.charAt(i); // System.out.println(c);
    		sum=sum+c;
    	}
    	BigInteger result=new BigInteger(sum); // System.out.println(result.toString());
    	return result;
    }
    
    public BigInteger encrypt(String s,String nn){
    	System.out.println(nn);
    	BigInteger e1=new BigInteger("71");
    	BigInteger x=StrtoBig(s);
    	BigInteger c=x.modPow(e1,new BigInteger(nn));
    	return c;
    }
    
    public BigInteger decrypt(BigInteger c){
    	BigInteger m=c.modPow(d1,n);
    	return m;
    }
    
    
    
	public static void main(String[] args) {
		
	/*	unpadded_RSA rsa=new unpadded_RSA();
		String s="好好学习";
		
		BigInteger c=rsa.encrypt(s);
		BigInteger c1=rsa.encrypt(s);
		
		
		
		System.out.println(c);
		System.out.println(c1); */
		
		
					
			
			
		
	}

}

