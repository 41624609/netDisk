package cn.cloud.encdec;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TestCert {
    public String username = "user1";
    public static String user_name ="user1";//��¼���û���
    public static String aes_key;//bf���õ�aes����
    public static String ras_key_1;
    public static String ras_key_2;
    public static long opeart_k=2568941234L;
    public static int cescmc_k=1;
    public static int cescmc_n=1;


    public void createCert(){//����֤��
        String aes_key="AES_key:"+456123+"\r\n";
        String rsa_key1="RSA_key1:"+10000019+"\r\n";
        //String rsa_key1="RSA_key1:"+9999973+"\r\n";
        String rsa_key2="RSA_key2:"+9999991+"\r\n";
        String toname="test2";//ϵͳ��ͨ���ı����ȡ,��Ҫ������֤����û�,����Ȩ
        boolean choose =false; //ѡ������ֻ�����߶�д��Ȩ֤��
        if(toname==null || toname.equals(""))
        {
            JOptionPane.showMessageDialog(new JLabel(), "�û�������Ϊ��");
            return ;
        }

        if(toname.equals(username)){
            JOptionPane.showMessageDialog(new JLabel(), "���ܸ��Լ�����֤��");
            return ;
        }

        Cert cert=new Cert();
        cert.produceCert(toname,user_name);
        if(choose==false){
            cert.produceCert0(toname,user_name,0);//0��ʾ����Ȩ��
            //System.out.println(jrb1.getText());
        }
        if(choose==true){
            cert.produceCert0(toname,user_name,1);//1��ʾ��д��Ȩ��
            //System.out.println(jrb2.getText());

        }

        cert.produceCert1(toname,user_name,aes_key,ras_key_1,ras_key_2,opeart_k,cescmc_k,cescmc_n);

        System.out.println("֤��������Ϊ\nD:\\qingyunclient\\test\\client\\cert_"+user_name+"_"+toname+"_0.txt\n"+"D:\\qingyunclient\\test\\client\\cert_"+user_name+"_"+toname+"_1.txt");
        //JOptionPane.showMessageDialog(new JLabel(), "֤�������ɣ�ΪD:\\qingyunclient\\test\\client\\cert_"+text51.getText()+".txt");

    }

    public void opendir(){//��Ŀ¼
        Desktop desktop = Desktop.getDesktop();
        File file = new File("D:\\qingyunclient\\test\\client");//�򿪵��ļ�Ŀ¼
        try {
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCert(){//ɾ��֤��
        String toname="test2";
        deleteFile("D:\\qingyunclient\\test\\client\\cert_"+user_name+"_"+toname+"_0.txt");
        deleteFile("D:\\qingyunclient\\test\\client\\cert_"+user_name+"_"+toname+"_1.txt");
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // ����ļ�·������Ӧ���ļ����ڣ�������һ���ļ�����ֱ��ɾ��
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("ɾ�������ļ�" + fileName + "�ɹ���");
                return true;
            } else {
                System.out.println("ɾ�������ļ�" + fileName + "ʧ�ܣ�");
                return false;
            }
        } else {
            System.out.println("ɾ�������ļ�ʧ�ܣ�" + fileName + "�����ڣ�");
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
