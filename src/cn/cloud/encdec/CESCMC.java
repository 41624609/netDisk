package cn.cloud.encdec;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

public class CESCMC {   
	/**
	 * 自己的，循环矩阵
	 */
	public double[][] dM;         //密钥
	public double[][] Mi;    //M^-1	
	public int dsum;        //总维数
	public long myseed ;        //产生矩阵的种子
	
	public CESCMC(int n,long seed){

		dsum=n;
		myseed=seed;
		genKey();
	}	
	
	public void genKey(){   //产生密钥M，计算M^-1和M^T^-1，初始化数据库db
		dM=new double[dsum][dsum];
		//Mi=new double[dsum][dsum];		
		Mi=null;	
		//long seed = (long)(Math.random()*100+10);
		Random random = new Random(myseed);		
		do{
			for(int i=0;i<dsum;i++)
				for(int j=0;j<dsum;j++){									
					dM[i][j]=random.nextInt(1000);						
				}
			Mi=reverse(dM);			
		}
		while(Mi==null);
		//System.out.println("dM");
		//printMatrix(dM);
	}	
	
	//求逆矩阵，高斯消元法
    public  double[][] reverse(double[][] matrix)   
    {   
        double[][] temp;   
        double[][] back_temp;   
        //得到矩阵的阶数   
        int m_length=matrix.length;   
        int n_length=matrix[0].length; 
        //创建n*（2n）行列式，用来求逆矩阵，原矩阵和单位矩阵   
        temp=new double[m_length][2*m_length];   
        back_temp=new double[m_length][n_length];
        //创建返回的矩阵,初始化   
        for(int x=0;x<m_length;x++) {
        	 for(int y=0;y<n_length;y++)   {    		 
        		 back_temp[x][y]=matrix[x][y];   
        	 }     		
        }
        
        
        //将原矩阵的值赋给 temp矩阵，并添加单位矩阵的值   
        for(int x=0;x<m_length;x++)   
        {   
            for(int y=0;y<temp[0].length;y++)   
            {   
                if(y>=m_length)   
                {   
                    if(x==(y-m_length))    
                        temp[x][y]=1;   
                    else
                        temp[x][y]=0;   
                }   
                else
                {   
                    temp[x][y]=matrix[x][y];   
                }   
            }   
        }   
   
        //高斯消元求逆矩阵   
        for(int x=0;x<m_length;x++)   
        {   
            double var=temp[x][x];   
            //判断对角线上元素是否为0，是的话与后面的行进行交换行，如没有满足条件的   
            //则可认为原矩阵没有逆矩阵。然后取值要化为0的列的值   
            for(int w=x;w<temp[0].length;w++)   
            {   
                if(temp[x][x]==0) //如果对角线的值为0的话，就与后面的进行行交换  
                {   int k;   
                    for(k=x+1;k<temp.length;k++)   
                    {      
                        if(temp[k][k]!=0)  //后面的对角线有不为0的 
                        {   
                            for(int t=0;t<temp[0].length;t++)//交换两行的值   
                            {   //System.out.println(">>>"+k+"<<<");   
                                double tmp=temp[x][t];   
                                temp[x][t]=temp[k][t];   
                                temp[k][t]=tmp;   
                            }   
                        break;   
                        }   
                    }   
                    //System.out.println(""+k);   
                    //如果出现无法将temp矩阵的左边化为单位矩阵，返回原矩阵   
                    if(k>=temp.length) {//没有找到，则说明没有逆矩阵，返回原矩阵，最后一行为0的情况也是满足的
                    	System.out.println("没有可逆矩阵");   
                    	return null;  
                    }
                    var=temp[x][x];   
               
                   
                }   
                temp[x][w] /=var;   //先把当前行的对角线化为1
            }   
            //将第x列的元素出对角线上的元素外都化为0，即构建单位矩阵   
            for(int z=0;z<m_length;z++)   
            {   double var_tmp=0.0;   
                for(int w=x;w<temp[0].length;w++)   
                {   //System.out.println("-"+x+"-"+z+"-"+w+"+++" + temp[z][w]);    
                    if(w==x) var_tmp=temp[z][x];   
                    if(x!=z) temp[z][w] -=(var_tmp*temp[x][w]);   
                           
                }   
            }   
        }   
        //取逆矩阵的值   
        for(int x=0;x<m_length;x++)   
        {   
            for(int y=0;y<m_length;y++)   
            {   
                back_temp[x][y]=temp[x][y+m_length];   
            }      
        }   
        
        return back_temp;   
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
	
	//产生循环矩阵
	public double[][] genCycleMatrix(double[] vector){
		int n=vector.length;
		double[][]aa=new double[n][n];
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				//aa[j][i]=vector[(j-i+n)%n];
				aa[i][j]=vector[(j-i+n)%n];
			}
		}
		return aa;
	}
	
	//打印矩阵
	public void printMatrix(double[][] matrix){
		System.out.println("\n");	
		int n=matrix.length;
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++){				
					System.out.print(matrix[i][j]+ " ");		
			}
			System.out.println("\n");	
		}
		System.out.println("\n");	
	}
	
	//加密
	public double[][] encrypt(double d){  //加密数据的元函数
		double[] res=new double[dsum];  
		double sum=0;
		for(int i=0;i<dsum-1;i++) {			
			double ta=(int)(Math.random()*100);
			res[i]=ta;
			sum=sum+ta;
		}
		res[dsum-1]=d-sum;
		double[][] cycleMatrix=genCycleMatrix(res);
		cycleMatrix=MulMatrix(dM,cycleMatrix);
		cycleMatrix=MulMatrix(cycleMatrix,Mi);	
		return cycleMatrix;
	}
	
	//解密
	public double decrypt(double[][] en){
		int n=en.length;
		double[][] res=new double[n][n];	
		DecimalFormat to= new DecimalFormat("#.00000");	
		res=MulMatrix(Mi,en);
		res=MulMatrix(res,dM);
		double result=0;
		for(int i=0;i<n;i++)
			result=result+res[0][i];	
		return result;			
	} 

	
	
	
	//加、减法运算
	public double[][] add_sub(double[][] sn,double[][] rn,int tag){ //加、减法运算
		double[][] res=new double[sn.length][sn.length];
		if(tag==1){
			for(int i=0;i<sn.length;i++){
				for(int j=0;j<dsum;j++){
					res[i][j]=sn[i][j]+rn[i][j];
				}
			}
		}else{
			for(int i=0;i<sn.length;i++){
				for(int j=0;j<dsum;j++){
					res[i][j]=sn[i][j]-rn[i][j];
				}
			}
		}		
		//after_add_sub(res);
		return res;
	}
	
	
	
	//密文相乘
	public double[][] mul(double[][] sn,double[][] rn){
		double[][]res=MulMatrix(sn,rn);		
		return res;
	}
	
	//密文相除
	public double[][] div(double[][] sn,double[][] rn){
		
				
		try{
			//Test2 t=new Test2();			
			//double[][] rn_i=t.exec(dsum,rn);//求逆矩阵
			double[][] rn_i=reverse(rn);//求逆矩阵
			//double[][] res=new double[sn.length][sn.length];
			double[][] res=MulMatrix(sn,rn_i);
			return res;		
		}catch (Exception e) { 
			e.printStackTrace(); 
			return null;
		}
	}

	
	public static void main(String[] args) {	
		int testtime=1000;
		for(int tt=0;tt<testtime;tt++){
			double sn=100;  //被操作数
			double rn=200;	//操作数						           
			double rn2=10;	//操作数	
			int n=10;
			long seed=47;
			//CESVMCIMP aspe=new CESVMCIMP(da,dm,k);
			CESCMC cescmc=new CESCMC(n,seed);
			
			double[][] en_sn=cescmc.encrypt(sn);			//被操作数加密	
			double[][] en_rn=cescmc.encrypt(rn);			//被操作数加密	
			double[][] en_rn2=cescmc.encrypt(rn2);			//被操作数加密	
			/*
			for(int i=0;i<aspe.dsum;i++){
				for(int j=0;j<aspe.dsum;j++) {							
					System.out.print(en_sn[i][j]+",");
				}
				System.out.println();
				
			}
			*/
			double de_sn=cescmc.decrypt(en_sn);		//操作数解密		
			System.out.println("明文1: "+sn);
			System.out.println("密文1解密: "+de_sn);
			de_sn=cescmc.decrypt(en_rn);		//操作数解密			
			//System.out.println("明文2: "+rn);		
			//System.out.println("密文2解密: "+de_sn);
			de_sn=cescmc.decrypt(en_rn2);		//操作数解密			
			//System.out.println("明文3: "+rn2);		
			//System.out.println("密文3解密: "+de_sn);
			
			
			
			double[][] res_add_e=cescmc.add_sub(en_sn,en_rn,1);  //加法运算						
			double[][] res_sub_e=cescmc.add_sub(en_sn,en_rn,2);  
				
			//double res_add_d=aspe.after_add_sub(res_add_e);  //加法运算结果解密		
			//double res_sub_d=aspe.after_add_sub(res_sub_e);
			double res_add_d=cescmc.decrypt(res_add_e);  //加法运算结果解密		
			double res_sub_d=cescmc.decrypt(res_sub_e);
			
			double[][] res_sub_e2=cescmc.add_sub(res_sub_e,en_rn2,1);  
			//double res_add_d2=aspe.after_add_sub(res_sub_e2);  //加法运算结果解密
			double res_add_d2=cescmc.decrypt(res_sub_e2);  //加法运算结果解密
			
			
			//System.out.println("加法解密: "+res_add_d);
			//System.out.println("减法解密: "+res_sub_d);
			//System.out.println("三个数相加减解密: "+res_add_d2);
			
			
			double[][] res_mul_e=cescmc.mul(en_sn,en_rn);//乘法
			//double res_mul_d=aspe.after_mul(res_mul_e);
			double res_mul_d=cescmc.decrypt(res_mul_e);
			//System.out.println("乘法解密: "+res_mul_d);
			
			
			
			
			double[][] res_mul_e2=cescmc.mul(res_mul_e,en_rn2);
			//double res_mul_d2=aspe.after_mul(res_mul_e2);
			double res_mul_d2=cescmc.decrypt(res_mul_e2);
			//System.out.println("三个乘法解密: "+res_mul_d2);
			
			
			
			double[][] res_div_e=cescmc.div(en_sn,en_rn);//除法
			//double res_div_d=aspe.after_div(res_div_e);
			double res_div_d=cescmc.decrypt(res_div_e);
			//System.out.println("除法解密: "+res_div_d);
			
			
			
			
			
			double[][] res_div_e2=cescmc.div(res_div_e,en_rn2);//除法
			//double res_div_d2=aspe.after_div(res_div_e2);
			double res_div_d2=cescmc.decrypt(res_div_e2);
			//System.out.println("三个除法解密: "+res_div_d2);

			
			double[][] res_mul_add=cescmc.add_sub(res_add_e,res_mul_e2,1);
			//double res_mul_d2=aspe.after_mul(res_mul_e2);
			double res_mul_add_dec=cescmc.decrypt(res_mul_add);
			//System.out.println("乘法后相加解密: "+res_mul_add_dec);
			
			
			double[][] res_div_add=cescmc.add_sub(res_add_e,res_div_e,1);
			//double res_mul_d2=aspe.after_mul(res_mul_e2);
			double res_div_add_dec=cescmc.decrypt(res_div_add);
			//System.out.println("除法后相加解密: "+res_div_add_dec);
			
			
			double[][] res_div_add_mul=cescmc.mul(res_div_add,en_rn2);
			//double res_mul_d2=aspe.after_mul(res_mul_e2);
			double res_div_add_mul_dec=cescmc.decrypt(res_div_add_mul);
			//System.out.println("除法后相加,再相乘解密: "+res_div_add_mul_dec);
		}
			/*
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
		*/
	}


}
