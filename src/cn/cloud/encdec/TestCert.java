package cn.cloud.encdec;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TestCert {
    public String username = "user1";
    public static String user_name ="user1";//登录的用户名
    public static String aes_key;//bf里用的aes加密
    public static String ras_key_1;
    public static String ras_key_2;
    public static long opeart_k=2568941234L;
    public static int cescmc_k=1;
    public static int cescmc_n=1;


    public void createCert(){//创建证书
        String aes_key="AES_key:"+456123+"\r\n";
        String rsa_key1="RSA_key1:"+10000019+"\r\n";
        //String rsa_key1="RSA_key1:"+9999973+"\r\n";
        String rsa_key2="RSA_key2:"+9999991+"\r\n";
        String toname="test2";//系统内通过文本框获取,需要被生成证书的用户,被授权
        boolean choose =false; //选择生成只读或者读写授权证书
        if(toname==null || toname.equals(""))
        {
            JOptionPane.showMessageDialog(new JLabel(), "用户名不能为空");
            return ;
        }

        if(toname.equals(username)){
            JOptionPane.showMessageDialog(new JLabel(), "不能给自己生成证书");
            return ;
        }

        Cert cert=new Cert();
        cert.produceCert(toname,user_name);
        if(choose==false){
            cert.produceCert0(toname,user_name,0);//0表示读的权限
            //System.out.println(jrb1.getText());
        }
        if(choose==true){
            cert.produceCert0(toname,user_name,1);//1表示读写的权限
            //System.out.println(jrb2.getText());

        }

        cert.produceCert1(toname,user_name,aes_key,ras_key_1,ras_key_2,opeart_k,cescmc_k,cescmc_n);

        System.out.println("证书已生成为\nD:\\qingyunclient\\test\\client\\cert_"+user_name+"_"+toname+"_0.txt\n"+"D:\\qingyunclient\\test\\client\\cert_"+user_name+"_"+toname+"_1.txt");
        //JOptionPane.showMessageDialog(new JLabel(), "证书已生成，为D:\\qingyunclient\\test\\client\\cert_"+text51.getText()+".txt");

    }

    public void opendir(){//打开目录
        Desktop desktop = Desktop.getDesktop();
        File file = new File("D:\\qingyunclient\\test\\client");//打开的文件目录
        try {
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCert(){//删除证书
        String toname="test2";
        deleteFile("D:\\qingyunclient\\test\\client\\cert_"+user_name+"_"+toname+"_0.txt");
        deleteFile("D:\\qingyunclient\\test\\client\\cert_"+user_name+"_"+toname+"_1.txt");
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    public static void main(String args[]){
        TestCert test = new TestCert();
        Cert cert=new Cert();
        test.opendir();
        test.createCert();
        cert.getCert1("test2","user1");
        test.deleteCert();
    }
}
