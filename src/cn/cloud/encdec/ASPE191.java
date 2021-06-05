package cn.cloud.encdec;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ASPE191 {   
	/**
	 * 计算机学报版本,(算术运算)number,青云
	 */
	public int[][] M;         //密钥
	public double[][] Mi;    //M^-1
	public double[][] Mti;   //M^T^-1	
	
	public int da;            //数据的加减维数
	public int dm;            //数据的乘除维数
	
	public double[][] p;    //数据库中的点加密后存放在p中
	public double[] q;      //加密后的查询点
	
	
	public double[][] db;      //数据库中的所有点
	public double[] qsa;      //查询点未加密
	
	public int k;           //随机数个数
	
	public int dsum;        //总维数
	
	public ASPE191(int da1,int dm1,int k1){
		da=da1;
		dm=dm1;
		k=k1;
		dsum=da+dm+k+1;
		genKey();
	}
	
	
	public void genKey(){   //产生密钥M，计算M^-1和M^T^-1，初始化数据库db
		M=new int[dsum][dsum];
		Mi=new double[dsum][dsum];
		Mti=new double[dsum][dsum];
		
		int[] num_d={2,4,5};
		
		//  这段程序会产生d+2阶单位矩阵，当找不到合适的高阶可逆矩阵的时候可以用
		for(int i=0;i<dsum;i++)
			for(int j=0;j<dsum;j++)
				if(i==j) M[i][j]=num_d[i%3];
				else M[i][j]=0;	
		
		try{
			Test t=new Test();			
			Mi=t.exec(dsum,M);
			Mti=t.exec(dsum,tansMatrix());			
		}catch (Exception e) { 
			e.printStackTrace(); 
		}		
	}	
	
	public int[][] tansMatrix(){ //计算转置矩阵
		int[][] N=new int[dsum][dsum];
		for(int i=0;i<dsum;i++)
			for(int j=0;j<dsum;j++) N[i][j]=M[j][i];
		return N;
	}
	
	
	public double[] multipleMatrix(double[] a){  //两矩阵相乘：M*a		
		double[] tmp=new double[dsum];
	    for(int i=0;i<dsum;i++){
	    	tmp[i]=0;
	    	for(int j=0;j<dsum;j++){
	    		tmp[i]=tmp[i]+a[j]*M[i][j];
	    	}
	    }
	    return tmp;
	}
	
	public double[] multipleMatrix1(double[] a){  //两矩阵相乘：Mi*a		
		double[] tmp=new double[dsum];
	    for(int i=0;i<dsum;i++){
	    	tmp[i]=0;
	    	for(int j=0;j<dsum;j++){
	    		tmp[i]=tmp[i]+a[j]*Mi[i][j];
	    	}
	    }
	    return tmp;
	}
	
	public double[] multipleMatrix2(double[] a){  //两矩阵相乘：Mti*a		
		double[] tmp=new double[dsum];
	    for(int i=0;i<dsum;i++){
	    	tmp[i]=0;
	    	for(int j=0;j<dsum;j++){
	    		tmp[i]=tmp[i]+a[j]*Mti[i][j];
	    	}
	    }
	    return tmp;
	}
	
	public double[][] MulMatrix(double[][] M1,double[][] M2){ //两矩阵相乘：a[][]*b[][]
		int k1=M1.length;
		int k2=M1[0].length;   
		int k3=M2[0].length; // System.out.println(k1+" "+k2+"  "+k3);
		double[][] res=new double[k1][k3];
		for(int i=0;i<k1;i++){
			for(int j=0;j<k3;j++){
				res[i][j]=0;
				for(int z=0;z<k2;z++){
					 double   b1= M1[i][z]*M2[z][j];  
	                 res[i][j]=res[i][j]+b1;
				}
			}
			//System.out.println();
		}
		return res;
	}
	
	public double[][] encrypt(double[] d){  //加密数据的元函数
		double[][] res=new double[d.length][dsum];  
						
		for(int i=0;i<d.length;i++){
			double sum_a=0,sum_r=0;
			for(int j=0;j<da;j++){
				if(j!=da-1){
					double ta=(int)(Math.random()*100);
					double tr=(int)(Math.random()*100);
					res[i][j]=ta+tr;
					sum_a=sum_a+ta;
					sum_r=sum_r+tr;
				}else{
					double ta=d[i]-sum_a;
					double tr=(int)(Math.random()*100);
					res[i][j]=ta+tr;					
					sum_r=sum_r+tr;
				}				
			}
			sum_a=1;
			for(int j=da;j<da+dm;j++){
				if(j!=da+dm-1){
					res[i][j]=Math.pow(2,(int)(Math.random()*5))*Math.pow(5,(int)(Math.random()*5));
					sum_a=sum_a*res[i][j];					
				}else{
					res[i][j]=d[i]/sum_a;
				}	
			}
			for(int j=da+dm;j<dsum-1;j++){
				res[i][j]=(int)(Math.random()*100);
			}
			res[i][dsum-1]=0-sum_r;
			res[i]=multipleMatrix(res[i]);
		}		
		return res;
	}
	
	public double[] decrypt(double[][] en){
		double[] res=new double[en.length];	
		DecimalFormat to= new DecimalFormat("#.00000");	
		
		for(int i=0;i<en.length;i++){
			double[] tmp=multipleMatrix1(en[i]);
			res[i]=0;
			for(int j=0;j<da;j++) res[i]=res[i]+tmp[j];
			res[i]=res[i]+tmp[dsum-1];
			res[i]=Double.valueOf(to.format(res[i]));
		}
		return res;
	} 
	
	
	
	
	
	
	
	public void outChar1(int[] a){  //将整形数组转换为对应的字符串，最后没回车
		int n=a.length;
		char[] c=new char[n];
		for(int i=0;i<n;i++){
			c[i]=(char)a[i];
			System.out.print(c[i]);
		} 
		
	} 
	
	public void outArray(double[] a){  //输出int数组元素的值
		int len=a.length;
		for(int i=0;i<len;i++) System.out.print(a[i]+"  ");
		System.out.println();
	}
	
	public void outArrayd(double[] a){  //输出double数组元素的值
		int len=a.length;
		for(int i=0;i<len;i++) System.out.print(a[i]+"  ");
		System.out.println("-------------");
	}
	
	public void outMatrix(double[][] result){ //输出数组的值
		int n1=1;
		int n2=result[0].length;
		for(int i=0;i<n1;i++){
			for(int j=0; j<n2;j++){
				System.out.print(result[i][j] +", ");
			}
			System.out.println();
		}
	}
	
	public double[][] add_sub(double[][] sn,double[] rn,int tag){ //加、减法运算
		double[][] res=new double[sn.length][dsum];
		if(tag==1){
			for(int i=0;i<sn.length;i++){
				for(int j=0;j<dsum;j++){
					res[i][j]=sn[i][j]+rn[j];
				}
			}
		}else{
			for(int i=0;i<sn.length;i++){
				for(int j=0;j<dsum;j++){
					res[i][j]=sn[i][j]-rn[j];
				}
			}
		}		
		//after_add_sub(res);
		return res;
	}
	
	public double[] after_add_sub(double[][] en){  //user对加法、减法运算的后处理
		double[] res=new double[en.length];		
        DecimalFormat to= new DecimalFormat("#.00000");	
		
		for(int i=0;i<en.length;i++){
			double[] tmp=multipleMatrix1(en[i]);
			res[i]=0;
			for(int j=0;j<da;j++) res[i]=res[i]+tmp[j];
			res[i]=res[i]+tmp[dsum-1];
			res[i]=Double.valueOf(to.format(res[i]));
			//System.out.print(res[i]+", ");
		}
		return res;
	}
	
	public double[][][] mul(double[][] sn,double[] rn,int tag){    //乘法运算
		double[][][] res=new double[sn.length][dsum][dsum];		
		for(int i=0;i<sn.length;i++){
			for(int j=0;j<dsum;j++){
				for(int z=0;z<dsum;z++) res[i][j][z]=sn[i][j]*rn[z];
			}
		}
	//	if(tag==3) after_mul(res);
	//	else after_div(res);
		
		return res;
	}
	
	public double[] after_mul(double[][][] en){
		double[] res=new double[en.length];
		DecimalFormat to=new DecimalFormat("#.00000");
		double[][][] tmp=new double[en.length][dsum][dsum];
		for(int i=0;i<en.length;i++){
			tmp[i]=MulMatrix(Mi,en[i]);
			tmp[i]=MulMatrix(tmp[i],Mti);
			res[i]=1;
			for(int j=da;j<da+dm;j++) res[i]=res[i]*tmp[i][j][j];
			res[i]=Double.valueOf(to.format(res[i]));
			//System.out.print(res[i]+",  ");
		}
		return res;
		
	}
	
	public double[] after_div(double[][][] en){
		double[] res=new double[en.length];
		double[][][] tmp=new double[en.length][dsum][dsum];
		DecimalFormat to=new DecimalFormat("#.00000");
		for(int i=0;i<en.length;i++){
			tmp[i]=MulMatrix(Mi,en[i]);
			tmp[i]=MulMatrix(tmp[i],Mti);
			res[i]=1;
			for(int j=da;j<da+dm;j++){
				double t2=0;
				for(int z=0;z<da;z++) t2=t2+tmp[i][z][j];
				t2=t2+tmp[i][dsum-1][j];
				res[i]=res[i]*t2;
			}
			for(int j=da;j<da+dm;j++){
				double t2=0;
				for(int z=0;z<da;z++) t2=t2+tmp[i][j][z];
				t2=t2+tmp[i][j][dsum-1];
				res[i]=res[i]/t2;
			}
			res[i]=Math.pow(res[i],1.0/(dm-1));
			res[i]=Double.valueOf(to.format(res[i]));
			//System.out.print(res[i]+", ");
		}
		return res;
		
	}
	
	
	public static void main(String[] args) {	
		
		//------------------d不同时的计算负载--------------------
	
		
	
			int da=4;
			int dm=2;
			int k=1;
			
			double[] sn={10};  //被操作数
			double[] rn={20};	//操作数						
            
							
			ASPE191 aspe=new ASPE191(da,dm,k);
			
			double[][] en_sn=aspe.encrypt(sn);			//被操作数加密	
			
			String en_str="";
			for(int i=0;i<en_sn[0].length;i++){
				en_str=en_str+en_sn[0][i]+",";
			}
//			System.out.println(en_str);
			
			double de_sn=aspe.decrypt(en_sn)[0];		//操作数解密		
				
//			System.out.println(de_sn);
			
			double[] en_rn=aspe.encrypt(rn)[0];			//操作数加密
			
		
			double[][] res_add_e=aspe.add_sub(en_sn,en_rn,1);  //加法运算
			
			
			double[][] res_sub_e=aspe.add_sub(en_sn,en_rn,2);  
			
			
			double[][][] res_mul_e=aspe.mul(en_sn,en_rn,3);
			
				
			double[][][] res_div_e=aspe.mul(en_sn,en_rn,3);
			
		
			double res_add_d=aspe.after_add_sub(res_add_e)[0];  //加法运算结果解密
			
		
			double res_sub_d=aspe.after_add_sub(res_sub_e)[0];
			
			
			double res_mul_d=aspe.after_mul(res_mul_e)[0];
			
			
			double res_div_d=aspe.after_div(res_div_e)[0];
			
			
			System.out.println(res_add_d+",  "+res_sub_d+",  "+res_mul_d+",  "+res_div_d);
			System.out.println(en_str);		
		
	}


}
