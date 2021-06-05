package cn.cloud.encdec;

import   java.io.*; 

import   java.util.zip.*; 

public class unZip{
	
	public String[] readFloder(String user_name){
		String[] res=null;
		try{
			File file = new File("D:\\qingyunclient\\atest\\zip_"+user_name+"\\");
			res=file.list();
			for(int i=0;i<res.length;i++){
				res[i]="D:\\qingyunclient\\atest\\zip_"+user_name+"\\"+res[i];
				System.out.println(res[i]);
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
		
	public void extZipFileList(String zipFileName,String user_name){ 
		try{ 
			ZipInputStream in=new ZipInputStream(new FileInputStream(zipFileName)); 
			ZipEntry entry = null;
			File file2 = new File("D:\\qingyunclient\\atest\\zip_"+user_name);
			file2.mkdirs();
			while((entry =in.getNextEntry())!=null){
				String entryName = entry.getName();
				if(entry.isDirectory()){ 
					File file = new File("D:\\qingyunclient\\atest\\zip_"+user_name+"\\"+entryName);
					file.mkdirs(); 
					System.out.println( "创建文件夹: "   +   entryName); 
				}else{
					
					
					FileOutputStream os=new   FileOutputStream( "D:\\qingyunclient\\atest\\zip_"+user_name+"\\"+entryName);
					byte[]   buf   =   new   byte[1024]; 
					int   len;
					while((len=in.read(buf))> 0){ 
						os.write(buf,0, len);
					}
					os.close();
					in.closeEntry();
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		} 
		//System.out.println( "解压文件成功 ");
	}
	
	public   static   void   main(String[]   args){ 
		unZip zips=new unZip();
		//zips.extZipFileList( "C://Documents and Settings//Administrator//桌面//allqq.zip");
		//zips.readFloder();
	} 


} 
