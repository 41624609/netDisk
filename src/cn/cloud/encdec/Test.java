package cn.cloud.encdec;

import   java.io.*;
import java.math.BigDecimal;
import java.util.Random;
import   java.util.StringTokenizer;

public   class   Test   {  //求逆矩阵
	public int   N;
	public double[][]   src;
	public double[][]   result;

public   double[][]   exec(int n, int[][] src1)   throws   Exception   {
	N=n;
	src=new double[N][N];	
	result=new double[N][N];
	for(int i=0;i<N;i++){
		for(int j=0;j<N;j++){ 
			src[i][j]=src1[i][j];
			//System.out.print(src[i][j]+"  ");
		}
		result[i][i]= 1;
		//System.out.println();
	}
	
	calCol(0);	
	calColBack(N-1);
	reInit();	
	return result;
}

public void  reInit(){
	
	for(int i=0;i<N; i++){
		double  coefficient=1/src[i][i];
		src[i][i] =1;
		for(int j=0;j<N;j++)
			result[i][j]   *= coefficient;
		}
}

public void calColBack(int  col){
	for(int i= col-1;i>=0;i--){
		double  coefficient= -1*src[i][col]/src[col][col];
		for(int z= 0;z< N;z++){
			src[i][z]+= coefficient * src[col][z];
			result[i][z] += coefficient * result[col][z];
		}
	}
	if(col>0)  calColBack(col-1);
}

public void  calCol(int col){	
	for(int  i=col+1; i<N; i++){
		double   coefficient=-1* src[i][col]/src[col][col];
		for(int z=0;z <N;z++){
			src[i][z] += coefficient *src[col][z];
			result[i][z]+= coefficient*result[col][z];
		}
	}
	if(col+1<N) calCol(col+1);
}

public void outMatrix(double[][] result){ //输出数组的值
	int n1=result.length;
	int n2=result[0].length;
	for(int i=0;i<n1;i++){
		for(int j=0; j<n2;j++){
			System.out.print(result[i][j] +", ");
		}
		System.out.println();
	}
}

public void outMatrix1(BigDecimal[][] result){ //输出二维数组的值
	int n1=result.length;
	int n2=result[0].length;
	for(int i=0;i<n1;i++){
		for(int j=0; j<n2;j++){
			System.out.print(result[i][j].toString() +", ");
		}
		System.out.println();
	}
}

public void outMatrix2(BigDecimal[] result){ //输出一维数组的值
	int n1=result.length;
	
	for(int i=0;i<n1;i++){
			System.out.print(result[i]+", ");
	}
}

public double[][] tansMatrix(double[][] M){ //计算转置矩阵
	int d1=M.length;
	int d2=M[0].length;
	double[][] N=new double[d2][d1];
	for(int i=0;i<d2;i++)
		for(int j=0;j<d1;j++) N[i][j]=M[j][i];
	return N;
}

public void genMatrix(){
	for(int N=7;N<21;N++){
		double[][] result=new double[N][N];
		int[][] M=new int[N][N];
		int tag=0;
		int flag=0;
		while(tag==0){		
			//System.out.println("-----------------------------------");
			for(int i=0;i<N;i++)
				for(int j=0;j<N;j++){
					int pp=(int)(Math.random()*10);
					M[i][j]=pp;
				}
			
			try{
				int len;
				result=exec(N,M);			
				for(int i=0;i<N;i++){
					for(int j=0;j<N;j++){
						String s=result[i][j]+"";
						//System.out.println(s);
						s=s.substring(s.indexOf(".")+1);
						len=s.length();
						//System.out.println("        "+s+":"+len);
						if(len>10 || s.compareTo("NaN")==0){
							flag=1;  //System.out.print("yes");
							break;
						}  
					}
					if(flag==1) break;
				}	
				if(flag==1){
					flag=0;
					tag=0;
				} 
				else tag=1;
				
			}catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
		
		System.out.println("----------------"+N+"-----------------------------");
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++) System.out.print(result[i][j]+"   ");
			System.out.println("\n");
		} 
		
		for(int i=0;i<N;i++){
			System.out.print("{");
			for(int j=0;j<N;j++)
				System.out.print(M[i][j]+",");			
			System.out.println("},");		
		}
	}
	
}

public double[][] MulMatrix(double[][] M1,double[][] M2){
	int k1=M1.length;
	int k2=M1[0].length;   
	int k3=M2[0].length; // System.out.println(k1+" "+k2+"  "+k3);
	double[][] M=new double[k1][k3];
	for(int i=0;i<k1;i++){
		for(int j=0;j<k3;j++){
			   M[i][j]=0;
			for(int z=0;z<k2;z++){
				 BigDecimal   b1= new   BigDecimal(Double.toString(M1[i][z]));   
                 BigDecimal   b2= new   BigDecimal(Double.toString(M2[z][j]));   
                 BigDecimal   b3=b1.multiply(b2);
                 BigDecimal   b4= new   BigDecimal(Double.toString(M[i][j]));  
                 M[i][j]=b4.add(b3).doubleValue();
			}
			
			
		}
		//System.out.println();
	}
	return M;
}

public BigDecimal[][] MulMatrixD(BigDecimal[][] M1,BigDecimal[][] M2){
	int k1=M1.length;
	int k2=M1[0].length;   
	int k3=M2[0].length;  
	BigDecimal[][] M=new BigDecimal[k1][k3];
	for(int i=0;i<k1;i++){
		for(int j=0;j<k3;j++){
			   M[i][j]=new BigDecimal("0");
			for(int z=0;z<k2;z++){
				//System.out.println(i+", "+j+", "+z);
				// System.out.print(M1[i][z]+"* "+M2[z][j]+"  ");
                 M[i][j]=M[i][j].add(M1[i][z].multiply(M2[z][j]));                 
			}
			
		}
		
	}
	//System.out.println();
	return M;
}

public double[][] addMatrix(double[][] M1,double[][] M2){
	int k1=M1.length;
	int k2=M1[0].length;  
	   //System.out.println(k1+" "+k2+"--------");
	double[][] M=new double[k1][k2];
	for(int i=0;i<k1;i++){
		for(int j=0;j<k2;j++){
			BigDecimal b1=new BigDecimal(Double.toString(M1[i][j]));   
            BigDecimal b2=new   BigDecimal(Double.toString(M2[i][j]));   
            M[i][j]=b1.add(b2).doubleValue();
			//System.out.print(M[i][j]+",");
			
		}
		//System.out.println();
	}
	return M; 
}

public double[][] subMatrix(double[][] M1,double[][] M2){
	int k1=M1.length;
	int k2=M1[0].length;  
	   //System.out.println(k1+" "+k2+"  "+k3);
	double[][] M=new double[k1][k2];
	for(int i=0;i<k1;i++){
		for(int j=0;j<k2;j++){
			 BigDecimal b1=new BigDecimal(Double.toString(M1[i][j]));   
             BigDecimal b2=new   BigDecimal(Double.toString(M2[i][j]));   
             M[i][j]=b1.subtract(b2).doubleValue();  
			
           // System.out.print(M1[i][j]+"-"+M2[i][j]+"="+M[i][j]+",");
			
		}
		//System.out.println();
	}
	return M; 
}

public double[][] divMatrix(double[][] M1,double[][] M2){
	double[][] result=new double[1][1];
	return result;
}

/*

public static void main(String[] args){
	int k=3;
	int[][] matrix={{2,0,2},{7,4,4},{2,0,3}}; 
	
	
	/*=new int[k][k];
	for(int i=0;i<k;i++)
		for(int j=0;j<k;j++)
			if(i==j) matrix[i][j]=1;
			else matrix[i][j]=0;	 */
	
/*
    try{
		Test t=new Test();
		/*double[][] result=t.exec(k, matrix);
		t.outMatrix(result);
		result=t.exec(k, t.tansMatrix(matrix));
		System.out.println();
		t.outMatrix(result); */
	
		
/*	
		int d=2;
		int r1=(int)(Math.random()*1000);
		int r2=(int)(Math.random()*1000);
		
		double[][] p1={{1,1,0}};
		double[][] p2={{15,5,100}};
		double[][] m0={{2,7,2},{0,4,0},{2,4,3}};  //M^t
		double[][] m1={{1.5,-1.625,-1},{0,0.25,0},{-1,0.75,1}}; //M^T^-1
		double[][] m2={{1960.0,896.0,1372.0},{560.0,256.0,392.0},{1820.0,832.0,1274.0}};
		double[][] m3={{1.5,0,-1},{-1.625,0.25,0.75},{-1,0,1}};   //M^-1
		
		double[][] t1=t.MulMatrix(p1,m0);
		t.outMatrix(t1);    
		System.out.println("---------p1---------------\n");
		double[][] t2=t.MulMatrix(p2,m0);
		t.outMatrix(t2);
		System.out.println("---------p2-----------------\n");
		double[][] addM=t.addMatrix(t1, t2);
		t.outMatrix(addM);
		System.out.println();
		double[][] addresult=t.MulMatrix(addM,m3);		
		double sum=0;
		for(int i=0;i<d;i++) sum=sum+addresult[0][i]; 
		System.out.println("p1+p2="+sum+"\n");
		System.out.println("---------p2+p2-----------------\n");
		
		
		double[][] subM=t.subMatrix(t1,t2);
		t.outMatrix(subM);
		System.out.println();
		double[][] subresult=t.MulMatrix(subM, m3);		
		double sub=0;
		for(int i=0;i<d;i++) sub=sub+subresult[0][i]; 
		System.out.println("p1-p2="+sub+"\n");
		System.out.println("---------p2-p2-----------------\n");
		
		double[][] tmp=t.MulMatrix(t.tansMatrix(t1),t2);
		t.outMatrix(tmp);
		System.out.println();
		double[][] tmp1=t.MulMatrix(m1,tmp);
		double[][] mulresult=t.MulMatrix(tmp1,m3);
		double mul=0;
		for(int i=0;i<d;i++)
			for(int j=0;j<d;j++){
				BigDecimal b1=new BigDecimal(Double.toString(mulresult[i][j]));   
	            BigDecimal b2=new   BigDecimal(Double.toString(mul));   
	            mul=b1.add(b2).doubleValue();
			} 
		System.out.println("p1*p2="+mul+"\n");
		System.out.println("---------p2*p2-----------------\n");
		
	}catch (Exception e) { 
			e.printStackTrace(); 
	}  

	
//	Test t=new Test();
//	t.genMatrix();
	
}
*/	

}