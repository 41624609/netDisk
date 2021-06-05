package cn.cloud.encdec;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

//import com.search.code.Index;

public class AllFile {

	public String readFile(String url){
	    //String type=url.substring(url.indexOf(".")+1);
		byte[] buffer=new byte[1024];
		String content="";
		try {
		    // Save as File
		    FileInputStream input = new FileInputStream(new File(url));
		    while ((input.read(buffer)) != -1) {            	
                content=content+new String(buffer,"ISO-8859-1");
            }
		    input.close();
		} catch (Exception e) {
		    e.printStackTrace();
		} 
		return content;
	}
	
	public void writeFile(String content,String name){
	   
		try {  System.out.println(name);
		    FileOutputStream output = new FileOutputStream(new File(name));
		    byte[] abuffer=content.getBytes("ISO-8859-1"); 
		    output.write(abuffer);
		    output.close();
		} catch (Exception e) {
		    e.printStackTrace();
		} 
	}
	
	public static void main(String[] args) {
		try{
			String dir="D:\\1.txt";
			AllFile file=new AllFile();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}

}
