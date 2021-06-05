package cn.cloud.encdec;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import javax.swing.*;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class TestBF {
    JTextArea text1;//BF加解密显示信息的结果
    String BFencresult;//BF加密文件结果
    String BFdecresult;//BF解密文件结果
    boolean isallempty=true;//加密文件是否全部为空
    public String username = "user1";
    public static String pwdd ="111111";
    public static String aes_key="RSA_key1:"+10000019+"\r\n";
    public static int kk=0;   //hash个数
    public static int nn=0;   //kw个数
    public static int mm=0;   //BF位数
    public static unpadded_RSA rsa =new unpadded_RSA();
    public static String user_name="user1";
    public List<String> list =new ArrayList<String>();

    public void choosefile(){//文件选择
        //String[] tmm=readFile(user_name,pwdd);
        //AES aes=new AES(tmm[0]);//AES的密钥，对文件名和文件内容进行加密，再通过BF来对加密后的文件名关键字进行映射
        AES aes=new AES();
        //String old_content=text1.getText();    //System.out.println(aes.password+"----------------5");
        Shell shellb=new Shell(SWT.CLOSE|SWT.MAX);
        FileDialog dialog=new FileDialog(shellb,SWT.MULTI);
        //FileDialog dialog=new FileDialog(new JFrame(),SWT.MULTI);
        dialog.setFileName("我的文档");
        dialog.setFilterExtensions(new String[] {"*.*","*.txt","*.mp3"});
        dialog.setFilterNames(new String[] {"All Files(*.*)","Text Files(*.txt)","mp3(*.mp3)"});
        dialog.open();
        String[] filename=dialog.getFileNames();
        String[] filedir=new String[filename.length];
        String content3="";
        for(int i=0;i<filename.length;i++){
            filedir[i]=dialog.getFilterPath()+"\\"+filename[i];
            content3=content3+filedir[i]+"\n";
            ;
        }
        //content3=content3+old_content;
        //System.out.println(content3);
        text1.append(content3);
    }

    public void bfEntrpted(){//加密
        //String dir=text1.getText();
        String test_dir="D:\\青云平台相关材料与代码\\qingyun\\user1\\test file";
        String dir="";
        File file= new File(test_dir);
        File[] fs=file.listFiles();
        for(File f : fs){
           if(f.isFile())  dir=dir+f+"\n";
        }
        System.out.println(dir);
        //String dir="D:\\青云平台相关材料与代码\\qingyun\\user1\\test file\\1703.00420.pdf\n";
        if(dir==null || dir.equals("")){
            //JOptionPane.showMessageDialog( mainPanel,"请先选择待加密文件");
            return ;
        }
        String[] tmm=readFile(user_name,pwdd);
        AES aes=new AES(tmm[0]);//AES的密钥，对文件名和文件内容进行加密，再通过BF来对加密后的文件名关键字进行映射
        //AES aes=new AES();
        int i=0;
        String ss=dir;
        while(ss.indexOf("\n")!=-1){
            i++;
            ss=ss.substring(ss.indexOf("\n")+1);
        }
        System.out.println("***************"+i);
        String[] filedir=new String[i];//先获取有几个待加密的文件
        ss=dir;
        i=0;
        while(ss.indexOf("\n")!=-1){//获取待加密的文件名
            filedir[i]=ss.substring(0,ss.indexOf("\n")).trim();
            i++;
            ss=ss.substring(ss.indexOf("\n")+1);
        }
        if(filedir.length!=0) {
            String encresult="";
            for(int j=0;j<filedir.length;j++){
                System.out.println(filedir[j]);
            }
            //System.out.println(aes.password+"----------------3");
            try{

                fileEncrypt(filedir);  //System.out.println(aes.password+"----------------4");
                if(isallempty){
                    //JOptionPane.showMessageDialog( mainPanel,"文件全部为空，加密结束");
                }else{
                    //JOptionPane.showMessageDialog( mainPanel,"加密成功 ,加密后的文件存放在 "+myencdir+"下！");
                    //text1.setText(BFencresult);
                }
                //text1.setText("\n\n"+"提示：加密后的文件存放在"+"D:\\qingyunclient\\atest\\encrypted_file\\下！");


            }
            catch(Exception ex){
                //text1.setText("\n\n"+"提示:加密失败，请重新加密！");
                ex.printStackTrace();
            }
        }
    }

    public void bftrpted(){//解密
        //String dir=text1.getText();
        String dir="";
        System.out.println(list);
        for(String i: list){
            dir=dir+"D:\\qingyunclient\\atest\\encrypted_file\\user1\\"+i+"\n";
        }
        System.out.println(dir);
        //String dir="D:\\qingyunclient\\atest\\encrypted_file\\user1\\B67E8080F25B1A27BA05BD072FA43355\n";
        if(dir==null || dir.equals("")){
            //JOptionPane.showMessageDialog( mainPanel,"请先选择待解密文件");
            return ;
        }
        String[] tmm=readFile(user_name,pwdd);
        AES aes=new AES(tmm[0]);//AES的密钥，对文件名和文件内容进行加密，再通过BF来对加密后的文件名关键字进行映射
        //AES aes=new AES();
        int i=0;
        String ss=dir;
        while(ss.indexOf("\n")!=-1){
            i++;
            ss=ss.substring(ss.indexOf("\n")+1);
        }
        String[] filedir=new String[i];
        ss=dir;
        i=0;
        while(ss.indexOf("\n")!=-1){
            filedir[i]=ss.substring(0,ss.indexOf("\n")).trim();
            i++;
            ss=ss.substring(ss.indexOf("\n")+1);
        }
        if(filedir.length!=0){

            fileDecrypt(filedir);
            //JOptionPane.showMessageDialog( mainPanel,"解密成功,解密后的文件存放在 "+mydecdir+"下！");
            //text1.setText("\n\n"+"提示：解密后的文件存放在"+"D:\\atest\\decrypted_file\\下！");
            //text1.setText("");
            System.out.println("BFdecresult "+BFdecresult);
            //text1.append(BFdecresult);
        }
    }

    public void entrptfile(){//加密目录
        String[] cmd = new String[5];
        String url = "D:\\qingyunclient\\atest\\encrypted_file";
        //String url = myencdir;
        cmd[0] = "cmd";
        cmd[1] = "/c";
        cmd[2] = "start";
        cmd[3] = " ";
        cmd[4] = url;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void detrptfile(){//解密目录
        String[] cmd = new String[5];
        String url = "D:\\qingyunclient\\atest\\decrypted_file";
        //String url = mydecdir;
        cmd[0] = "cmd";
        cmd[1] = "/c";
        cmd[2] = "start";
        cmd[3] = " ";
        cmd[4] = url;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public   String[] readFile(String name,String pwd){
        FileInputStream fin=null;

        String[] result=null;

        try{
            //System.out.println("D:\\test\\client\\key_"+name+".txt");
            int len=9;
            fin=new FileInputStream("D:\\qingyunclient\\test\\client\\key_"+name+".txt");
            result=new String[len];
            int bt=0;
            int j=0;
            String tmp="";
            AES aes=new AES(pwd);//用用户的密码作为解密本地账户信息文件AES的密钥

            while((bt=fin.read())!=-1)	tmp=tmp+(char)bt;//一次读一个字符，全部读完存储到tmp中

            tmp=aes.decrypt(tmp);
            int i=0;
            if(tmp==null){
                return null;
            }
            else {
                while(tmp.indexOf("\r\n")!=-1){//过滤掉回车换行，读取密钥，一个密钥占一行
                    result[i]=tmp.substring(0,tmp.indexOf("\r\n"));
                    tmp=tmp.substring(tmp.indexOf("\r\n")+2);
                    //System.out.println(result[i]);
                    i++;

                }
                //System.out.println("IIIIII: "+i);
                for(j=0;j<len;j++){
                    //System.out.println(result[j]);
                    result[j]=result[j].substring(result[j].indexOf(":")+1);
                    //System.out.println(result[j]);
                }
                fin.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        //System.out.println("--------------------");
        return result;
    }

    public  boolean fileEncrypt(final String[] filedir){   //加密文件内容和名字，将加密后的文件存放在相应的位置
        BFencresult="";
        isallempty=true;//全部为空
        AES aes= new AES();
        deleteAllFile("D:\\qingyunclient\\atest\\encrypted_file\\"+username);
        int sfile=filedir.length;
        int[] rand=new int[sfile];
        String[] BF=new String[sfile];
        String[] en_name=new String[sfile];
        AllFile allF=new AllFile();
            String content="";
            for(int i=0;i<filedir.length;i++){
                rand[i]=(int)(Math.random()*10000);
                }
                    File dirFile = new  File("D:\\qingyunclient\\atest\\encrypted_file\\"+username);
                    if (! (dirFile.exists())&& ! (dirFile.isDirectory())) dirFile.mkdirs();


                    for(int i=0;i<filedir.length;i++) {


                        File f = new File(filedir[i]);//对文件名进行加密
                        System.out.println("f.length()  " + f.length());
                        if (f.length() == 0) {
                            System.out.println("文件内容为空，不加密");
                            //JOptionPane.showMessageDialog( mainPanel,"文件  "+filedir[i]+"内容为空，不加密");
                            continue;
                        }
                        isallempty = false;
                        String name = f.getName();  //System.out.println(name);

                        en_name[i] = aes.encrypt(name);     //加密的文件名
                        list.add(en_name[i]);
                        content = allF.readFile(filedir[i]);
                        String en_content = aes.encrypt(content); //加密的文件内容

                        String[] kw = kwAbstract(name);      //文件关键字的BF	 。是对明文进行加密，不是经过AES加密过的  ，在这个函数里面，对明文是经过RSA加密的 。
                        //与检索参数的加密是一样的，一个关键字串对应一个加密值
                        BF[i] = kwBF(kw, 0.001, rand[i]);    //每个rand[i]都不一样，随机的，加在关键字后面进行混淆，kw是一个文件名的明文经过RSA加密过的，为检索服务，保存在0.txt文件中


                        f = new File("D:\\qingyunclient\\atest\\encrypted_file\\" + username + "\\" + en_name[i]); //加密的文件内容直接写入文件，文件名和文件内容通过AES加密。
                        if (!f.exists()) {
                            try {
                                f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        BufferedWriter output = null;
                        try {
                            output = new BufferedWriter(new FileWriter(f));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            output.write(en_content);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        BFencresult = BFencresult + "D:\\qingyunclient\\atest\\encrypted_file\\" + username + "\\" + en_name[i] + "\n";

                    }


                    //System.arraycopy(en_name,0,tesyfilename,0,en_name.length);

                    if(isallempty)
                    {
                        //JOptionPane.showMessageDialog( mainPanel,"文件全部为空，加密结束");
                        return false;

                    }


                        File f=new File("D:\\qingyunclient\\atest\\encrypted_file\\"+username+"\\0.txt");
                        if (!f.exists()) {
                            try {
                                f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        String en_info="";
                        for(int i=0;i<sfile;i++){
                            en_info=en_info+"filename:"+en_name[i]+"\r\n"; //AES 加密过的文件名，作为检索参数使用
                            en_info=en_info+"random:"+rand[i]+"\r\n";//随机值，与文件名关键字进行运算，进行混淆作用
                            en_info=en_info+"BF:"+BF[i]+"\r\n";//BF映射的结果
                        }
        BufferedWriter output1 = null;
        try {
            output1 = new BufferedWriter(new FileWriter(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output1.write(en_info);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BFencresult="D:\\qingyunclient\\atest\\encrypted_file\\"+username+"\\0.txt\n"+BFencresult;



        return isallempty;

    }

    public  void fileDecrypt(final String[] filedir){   //加密文件内容和名字，将加密后的文件存放在相应的位置
        BFdecresult="";
                deleteAllFile("D:\\qingyunclient\\atest\\decrypted_file\\"+username);
                unZip zips=new unZip();
                int sfile1=filedir.length;
                String[] de_name1=new String[sfile1];
                String[] content1=new String[sfile1];
                for(int i=0;i<sfile1;i++){
                    de_name1[i]="";
                    content1[i]="";
                }

                AllFile allF=new AllFile();
                File dirFile = new  File("D:\\qingyunclient\\atest\\decrypted_file\\"+username);
                if (! (dirFile.exists())&& ! (dirFile.isDirectory())) dirFile.mkdirs();
                for(int i=0;i<filedir.length;i++){


                    try{
                        String pt=filedir[i];
                        pt=pt.substring(pt.indexOf(".")+1);
                        System.out.println("pt="+pt.compareTo("zip"));
                        if(pt.compareTo("zip")==0){//如果是压缩文件，先解压缩

                            deleteAllFile("D:\\qingyunclient\\atest\\zip"+user_name);
                            zips.extZipFileList(filedir[i],user_name);	//解压缩文件
                            String[] unzipfiledir=zips.readFloder(user_name);//解压缩后放在特定目录下，获取该目录下的所有文件

                            int sfile=unzipfiledir.length;//解压缩后的文件个数
                            String[] de_name=new String[sfile];
                            String[] content=new String[sfile];
                            for(int j=0;j<unzipfiledir.length;j++){
                                File f=new File(unzipfiledir[j]);
                                String name=f.getName();
                                String fromother="";
                                //String owner_name=name.substring(name.indexOf("__")+1);
                                if(name.indexOf("___")!=-1){
                                    fromother=name.substring(name.indexOf("___")+3);
                                    name=name.substring(0,name.indexOf("___"));
                                    System.out.println("fromother:"+fromother);
                                }
                                //System.out.println("---------------name------------"+name);
                                //System.out.println("---------------fromother------------"+fromother);
                                if(fromother.equals("") || fromother.equals(username)){//自己的

                                    //System.out.println("##############AES key "+aes_key);
                                     //AES aes=new AES(aes_key);
                                    AES aes=new AES();
                                    de_name1[i]=aes.decrypt(name);	 //解密的文件名
                                    System.out.println(de_name[i]);


                                    content1[i]="";
                                    InputStreamReader read = new InputStreamReader(new FileInputStream(f));
                                    BufferedReader reader=new BufferedReader(read);
                                    String line="";
                                    while ((line = reader.readLine()) != null) {
                                        content1[i]=content1[i]+line;
                                        }

                                    String de_content=aes.decrypt(content1[i]); //解密的文件内容
                                    String url="D:\\qingyunclient\\atest\\decrypted_file\\"+username+"\\"+de_name1[i];
                                    BFdecresult=BFdecresult+url+"\n";
                                    //BFdecresult=BFencresult+de_name[j]+"\n";
                                    allF.writeFile(de_content, url);


                                }else{//他人的
                                    //System.out.println("others*******************");
                                    String decfiledir="D:\\qingyunclient\\atest\\decrypted_file\\"+username+"\\"+fromother;
                                    File newdir=new File(decfiledir);
                                    if(!newdir.exists())
                                        newdir.mkdir();


                                    Cert cert=new Cert();
                                    String res=cert.getCert1(fromother,user_name);
                                    String keyarr[]=res.split("&");
                                    String aeskey=keyarr[2];
                                    AES aeshere=new AES(aeskey);
                                    //AES aeshere=new AES();
                                    de_name1[i]=aeshere.decrypt(name);	 //解密的文件名
                                    content1[i]="";
                                    InputStreamReader read = new InputStreamReader(new FileInputStream(f));
                                    BufferedReader reader=new BufferedReader(read);
                                    String line="";
                                    while ((line = reader.readLine()) != null) {
                                        content1[i]=content1[i]+line;
                                        }

                                    String de_content=aeshere.decrypt(content1[i]); //解密的文件内容
                                    String url="D:\\qingyunclient\\atest\\decrypted_file\\"+username+"\\"+fromother+"\\"+de_name1[i];
                                    BFdecresult=BFdecresult+url+"\n";
                                    //BFdecresult=BFencresult+de_name[j]+"\n";
                                    allF.writeFile(de_content, url);


                                }


                                System.out.println("一共"+unzipfiledir.length+"个文件，正在解密第"+(j+1)+"个文件");

                            }
                        }else{//不是压缩文件，直接解密
                            System.out.println("------------------------------"+filedir[i]);
                            File f=new File(filedir[i]);
                            String name=f.getName();
                            String fromother="";
                            //String owner_name=name.substring(name.indexOf("__")+1);
                            if(name.indexOf("___")!=-1){
                                fromother=name.substring(name.indexOf("___")+3);
                                name=name.substring(0,name.indexOf("___"));

                            }
                            //System.out.println("---------------name------------"+name);
                            //System.out.println("---------------fromother------------"+fromother);
                            if(fromother.equals("") || fromother.equals(username)){//自己的

                                //System.out.println("##############AES key "+aes_key);
                                //AES aes=new AES(aes_key);
                                AES aes=new AES();
                                de_name1[i]=aes.decrypt(name);	 //解密的文件名

                                content1[i]="";
                                InputStreamReader read = new InputStreamReader(new FileInputStream(f));
                                BufferedReader reader=new BufferedReader(read);
                                String line="";
                                while ((line = reader.readLine()) != null) {
                                    content1[i]=content1[i]+line;
                                }

                                String de_content=aes.decrypt(content1[i]); //解密的文件内容
                                String url="D:\\qingyunclient\\atest\\decrypted_file\\"+username+"\\"+de_name1[i];
                                BFdecresult=BFdecresult+url+"\n";
                                //BFdecresult=BFencresult+de_name[j]+"\n";
                                allF.writeFile(de_content, url);


                            }else{//他人的
                                //System.out.println("others*******************");
                                String decfiledir="D:\\qingyunclient\\atest\\decrypted_file\\"+username+"\\"+fromother;
                                File newdir=new File(decfiledir);
                                if(!newdir.exists())
                                    newdir.mkdir();


                                Cert cert=new Cert();
                                String res=cert.getCert1(fromother,user_name);
                                String keyarr[]=res.split("&");
                                String aeskey=keyarr[2];
                                AES aeshere=new AES(aeskey);
                                //AES aeshere=new AES();
                                de_name1[i]=aeshere.decrypt(name);	 //解密的文件名

                                try{
                                    content1[i]="";
                                    InputStreamReader read = new InputStreamReader(new FileInputStream(f));
                                    BufferedReader reader=new BufferedReader(read);
                                    String line="";
                                    while ((line = reader.readLine()) != null) {
                                        content1[i]=content1[i]+line;
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                String de_content=aeshere.decrypt(content1[i]); //解密的文件内容
                                String url="D:\\qingyunclient\\atest\\decrypted_file\\"+username+"\\"+fromother+"\\"+de_name1[i];
                                BFdecresult=BFdecresult+url+"\n";
                                //BFdecresult=BFencresult+de_name[j]+"\n";
                                allF.writeFile(de_content, url);


                            }



                        }
                        if(pt.compareTo("zip")!=0){
                            System.out.println("一共"+filedir.length+"个文件，正在解密第"+(i+1)+"个文件");
                    }
                    }catch(Throwable el){}
                }





    }

    public static boolean deleteAllFile(String folderFullPath){
        boolean ret = false;
        File file = new File(folderFullPath);
        if(file.exists()){
            if(file.isDirectory()){
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    String filePath = fileList[i].getPath();
                    deleteAllFile(filePath);
                }
            }
            if(file.isFile()){
                file.delete();
            }
        }
        return ret;
    }

    public static  String[] kwAbstract(String filename){  //根据题目获得关键字kw
        int tag=0;
        filename=filename.substring(0, filename.indexOf("."));
        String s=filename;
        String[] result=new String[500];
        int slen=s.length();
        tag=-1;
        for(int i=0;i<slen;i++){  //tag：0--中文，1--英文，2--中英
            int c=(int)(s.charAt(i)); //System.out.println(c);
            if(c<125){
                if(tag==-1) tag=1;
                else if(tag==0) tag=2;
            }else{
                if(tag==-1) tag=0;
                else if(tag==1) tag=2;
            }
        }
        //System.out.println("tag="+tag);
        int sum=0;
        s=filename;
        if(tag==0){  //处理中文
            String[] limit={"基于","研究","的","面向","设计"};
            for(int i=0;i<limit.length;i++){
                while(s.indexOf(limit[i])!=-1){
                    int index=s.indexOf(limit[i]);
                    if(i!=2) s=s.substring(0,index)+" "+s.substring(index+2);
                    else s=s.substring(0,index)+" "+s.substring(index+1);
                }
            }
            //	int before=0;
            if(s.charAt(0)==' ') s=s.substring(1);
            for(int i=1;i<s.length();i++){
                if(s.charAt(i-1)==' '&& s.charAt(i)==' ') s=s.substring(0,i-1)+s.substring(i);
            }
            if(s.charAt(s.length()-1)==' ') s=s.substring(0,s.length()-2);
            String ss=s;
            while(ss.indexOf(" ")!=-1){
                sum++;
                ss=ss.substring(s.indexOf(" ")+1);
            }
            sum++;
            s=s+" ";
            result=new String[sum];
            int k=0;
            while(s.indexOf(" ")!=-1){
                result[k]=s.substring(0,s.indexOf(" "));
                k++;
                s=s.substring(s.indexOf(" ")+1);
            }
            String[][] sumResult=new String[sum][];
            int sumLen=0;
            for(int i=0;i<sum;i++){
                sumResult[i]=Chinese(result[i]);
                sumLen=sumLen+sumResult[i].length;
            }
            result=new String[sumLen+1];
            k=0;
            for(int i=0;i<sum;i++){
                for(int j=0;j<sumResult[i].length;j++){
                    result[k]=sumResult[i][j]; //System.out.println(result[k]);
                    k++;
                }
            }
            result[k]=filename;  sum=k+1;
        }else if(tag==1){ //处理英文
            String[] limit={"of ","based on ","Research ","Supporting ","on ","An ","A ","for ","Study ","Scheme "};
            for(int i=0;i<limit.length;i++){
                while(s.indexOf(limit[i])!=-1){
                    int index=s.indexOf(limit[i]);
                    if(s.substring(0,index).compareTo("")!=0)
                        s=s.substring(0,index)+" "+s.substring(index+limit[i].length());
                    else s=s.substring(index+limit[i].length());
                }
            }  // System.out.println(s+",");
            String ss=s;
            while(ss.indexOf("  ")!=-1){
                sum++;
                ss=ss.substring(s.indexOf("  ")+1);
            }
            sum++;     //System.out.println(sum);
            s=s+"  ";
            result=new String[sum];
            int k=0;
            while(s.indexOf("  ")!=-1){
                result[k]=s.substring(0,s.indexOf("  "));
                int i=0;
                while(result[k].charAt(i)==' ') result[k]=result[k].substring(1);
                k++;
                s=s.substring(s.indexOf("  ")+2);
            }
            String[][] sumResult=new String[sum][];
            int sumLen=0;
            for(int i=0;i<sum;i++){
                sumResult[i]=English(result[i]);
                sumLen=sumLen+sumResult[i].length;
            }
            result=new String[sumLen+1];
            k=0;
            for(int i=0;i<sum;i++){
                for(int j=0;j<sumResult[i].length;j++){
                    result[k]=sumResult[i][j]; //System.out.println(result[k]);
                    k++;
                }
            }
            result[k]=filename; //System.out.println(result[k]);
            sum=k+1;
        }else{ //处理中英
            String[] limit={"基于","研究","的","面向","设计"};
            for(int i=0;i<limit.length;i++){
                while(s.indexOf(limit[i])!=-1){
                    int index=s.indexOf(limit[i]);
                    if(i!=2) s=s.substring(0,index)+" "+s.substring(index+2);
                    else s=s.substring(0,index)+" "+s.substring(index+1);
                }
            }

            int ssl=s.length();
            int nums=0;
            for(int i=0;i<ssl;i++){
                if(s.charAt(i)!=32 &&s.charAt(i)<125){
                    nums++;
                }else{
                    if(nums>0){
                        s=s.substring(0,i-nums)+" "+s.substring(i-nums,i)+" "+s.substring(i);
                        nums=0;
                    }
                }
            }
            //	int before=0;
            if(s.charAt(0)==' ') s=s.substring(1);
            for(int i=1;i<s.length();i++){
                if(s.charAt(i-1)==' '&& s.charAt(i)==' ') s=s.substring(0,i-1)+s.substring(i);
            }
            if(s.charAt(s.length()-1)==' ') s=s.substring(0,s.length()-2);

            //System.out.println(s+"----------");

            String ss=s;
            while(ss.indexOf(" ")!=-1){
                sum++;
                ss=ss.substring(s.indexOf(" ")+1);
            }
            sum++;
            s=s+" ";
            result=new String[sum];
            int k=0;
            while(s.indexOf(" ")!=-1){
                result[k]=s.substring(0,s.indexOf(" "));
                k++;
                s=s.substring(s.indexOf(" ")+1);
            }
            String[][] sumResult=new String[sum][];
            int sumLen=0;
            for(int i=0;i<sum;i++){
                if(result[i].charAt(0)<125){
                    sumResult[i]=English(result[i]);
                }else{
                    sumResult[i]=Chinese(result[i]);
                }
                sumLen=sumLen+sumResult[i].length;
            }
            result=new String[sumLen+1];
            k=0;
            for(int i=0;i<sum;i++){
                for(int j=0;j<sumResult[i].length;j++){
                    result[k]=sumResult[i][j]; //System.out.println(result[k]);
                    k++;
                }
            }
            result[k]=filename;  sum=k+1;

        }

        BloomFilter bf=new BloomFilter(mm,kk,nn);
        BigInteger rs;
        for(int i=0;i<sum;i++){
            //System.out.println(result[i]+"-----------");
            System.out.println(result[i]);
            rs=bf.ran_encrypt_para(result[i],rsa.n.toString());
            result[i]=rs.toString();
        }
        return result;
    }

    public static String kwBF(String[] kw,double positive,int rand){   //根据加密后的kw生产BF
        byte[] mms=null;
        String result="";
        try{
            BloomFilter bf;
            if(mm==0) bf=new BloomFilter();
            else bf=new BloomFilter(mm,kk,nn);
            bf.kwInsert(kw,rand);
            mms=bf.mm;
            //System.out.println();
            result=byteToStr(mms);
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static String[] Chinese(String filename){  //处理中文词组
        int len=filename.length();
        int sum=0;
        for(int i=0;i<len;i++){
            sum=sum+(len-i);
        }
        //System.out.println("sum="+sum);
        String[] result=new String[sum];
        int k=0;
        for(int i=0;i<len;i++){
            for(int j=i;j<len;j++){
                if(i==j) result[k]=filename.substring(i,i+1);
                else result[k]=filename.substring(i,j+1);
                k++;
            }
        }
        return result;
    }

    public static  String[] English(String filename){  //处理英文词组
        String s=filename;
        int tag=0;
        while(s.indexOf(" ")!=-1){
            tag++;
            s=s.substring(s.indexOf(" ")+1);
        }
        String[] res=new String[tag+1];
        s=filename+" ";
        int k=0;
        while(s.indexOf(" ")!=-1){
            res[k]=s.substring(0,s.indexOf(" "));
            s=s.substring(s.indexOf(" ")+1);
            //System.out.println(res[k]);
            k++;
        }
        int len=res.length;
        int sum=0;
        for(int i=0;i<len;i++){
            sum=sum+(len-i);
        }
        String[] result=new String[sum];  //System.out.println("sum="+sum);
        k=0;
        for(int i=0;i<len;i++){
            String tmp="";
            for(int j=i;j<len;j++){
                if(i==j){
                    result[k]=res[i];
                    tmp=result[k];
                }
                else{
                    result[k]=tmp+" "+res[j];
                    tmp=result[k];
                }
                k++;
            }
        }
        return result;
    }

    public static String byteToStr(byte[] mmm){
        String result="";
        try{
            String tmp="";
            int BF_m=744;                  //BloomFiler的位数m的信息
            //	int t=BF_m/31;
            int ts=0;
            for(int i=0;i<BF_m;i++){
                tmp=tmp+mmm[i];
                ts++;
                if(ts==31){
                    int a=Integer.parseInt(tmp,2);
                    result=result+a+"&";
                    tmp="";
                    ts=0;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }



    public static void main(String args[]) {
        TestBF test = new TestBF();
        //test.entrptfile();
        //test.detrptfile();
        //test.choosefile();
        test.bfEntrpted();
        test.bftrpted();


    }
}
