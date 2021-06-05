package cn.cloud;




import cn.cloud.encdec.CESCMC;
import cn.cloud.encdec.unZip;
import cn.cloud.encdec.AllFile;
import cn.cloud.encdec.Cert;
import cn.cloud.encdec.ASPE191;
import cn.cloud.encdec.OPEART;
import cn.cloud.encdec.BloomFilter;
import cn.cloud.encdec.AES;
import cn.cloud.encdec.unpadded_RSA;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.cloud.task.ChangePwdTask;
import cn.cloud.task.DeleteTask;
import cn.cloud.task.DownloadTask;
import cn.cloud.task.ExpandTask;
import cn.cloud.task.ForgetPwdTask;
import cn.cloud.task.GlobalState;
import cn.cloud.task.ListenTask;
import cn.cloud.task.ListenTask2;
import cn.cloud.task.LoginTask;
import cn.cloud.task.UploadTask;
import cn.cloud.task.createFile;

import cn.cloud.util.FileType;
import cn.cloud.util.FileUtil;
import cn.cloud.util.MyRenderer;

import cs.xjtu.cn.bean.FileBean;

public class ClientView extends JFrame{
	public static AES aes=new AES();
	public static unpadded_RSA rsa =new unpadded_RSA() ;
	public static int kk=0;   //hash个数
	public static int nn=0;   //kw个数
	public static int mm=0;   //BF位数
	//public static int ii=0;
	public static String pwdd;
	public static String user_name;

	public static String aes_key;
	public static String ras_key_1;
	public static String ras_key_2;
	public static double Bloom_Filter_ju;
	public static long opeart_k=2568941234L;
	public static int cescmc_k=1;
	public static int cescmc_n=1;
	public static int uploadlimit=1;
	public static int downloadlimit=1;
	static String head0="000000000000";

	int len=9;//本地用户信息的文件内容数

	/**
	 * 	界面控件信息
	 */

	//获取屏幕大小,定位窗体
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JMenuBar menuBar;	//菜单栏
	private JMenu fileMenu;

	private JMenuItem StopWatchMenuItem;
	private JMenuItem StartWatchMenuItem;

	private JMenu switchMenu;
	private JMenuItem switchMenuItem;//切换登陆用户
	private JMenuItem switchMenuItem2;//切换登陆模式

	private JMenu changepwdMenu;
	private JMenuItem passwdMenuItem;

	private JMenu showMenu;
	private JMenuItem showMenuItem1;
	private JMenuItem showMenuItem2;
	private JMenuItem showMenuItem3;

	private JMenu helpMenu;
	private JMenuItem helpMenuItem;
	private JMenuItem aboutMenuItem;
	private JMenuItem webMenuItem;
	private JMenuItem exitMenuItem;




	private JPanel bottomPanel ;//底部面板
	private JPanel mainPanel;//中部面板
	private JPanel midPanel;//中部面板
	//中部面板内容
	private JPanel leftPanel;//中左部面板1
	private JPanel leftPanel1;//中左部面板1
	private JPanel leftPanel2;//中左部面板2
	private JTree jTree1;
	private JTree jTree2;
	//DefaultMutableTreeNode node1;
	private DefaultMutableTreeNode tempNode;
	private DefaultMutableTreeNode[] tempNodearr=null;//云端目录选择的多个节点
	private DefaultMutableTreeNode[] tempNodearrlocal=null;//本地目录选择的多个节点
	private JMenuItem[] jMenuItem1 = {new JMenuItem("刷新"),
			new JMenuItem("下载"),new JMenuItem("删除"), new JMenuItem("新建文件夹"), new JMenuItem("Excel文件选择"), new JMenuItem("文件重命名"),new JMenuItem("属性")};
	private JMenuItem[] jMenuItem2 = {new JMenuItem("刷新"),new JMenuItem("打开"),
			new JMenuItem("上传")  ,new JMenuItem("删除"),new JMenuItem("新建文件夹"),new JMenuItem("BF文件选择"),new JMenuItem("Excel文件选择"),new JMenuItem("新建文件"),new JMenuItem("重命名"),new JMenuItem("属性")};
	private JPopupMenu jPopupMenu1;	//java右键弹出菜单
	private JPopupMenu jPopupMenu2;
	private JScrollPane jScrollPane1;//java滚动条
	private JScrollPane jScrollPane2;
	private JTabbedPane tabbedPane ;//右部面板分页容器	文件加解密。属性加解密等页面分页器

	private JLabel islistenjl;//是否在监听本地文件
	private JLabel showtime;//是否在监听本地文件
	/**
	 * 	其他信息
	 */
	public String username;
	final static Display display = new Display(); //Display负责处理所有SWT窗口小部件和操作系统之间的交互
	final static Shell shell=new Shell(display,SWT.CLOSE|SWT.MAX); //整个视图窗口

	ListenTask2 listen=null;

	JTextArea text1;//BF加解密显示信息的结果
	String BFencresult;//BF加密文件结果
	String BFdecresult;//BF解密文件结果
	boolean isallempty=true;//加密文件是否全部为空

	JComboBox box31;//Excel文件加解密	属性下拉框
	JComboBox box32;//Excel文件加解密	加解密算法下拉框
	JTextField text33;//Excel文件加解密	文件路径
	JTextArea text34;//Excel文件加解密		文件详细信息
	JTextArea text35;//Excel文件加解密		选择的属性和加解密算法

	JTextField text42;//Excel文件加解密	文件路径  云端
	JTextField text43;//Excel文件加解密	文件基本信息，条数，属性  云端

	JTextField text45;//Excel文件加解密	保序关键字	  云端
	JTextField text451;//Excel文件加解密	下边界			 云端
	JTextField text452;//Excel文件加解密	上边界  云端

	JTextField text410;		//算术关键字
	JTextField	text4101;	//行

	JComboBox box44;//Excel文件加解密	云端	保序属性
	JComboBox box47;//Excel文件加解密	云端	算术属性
	JComboBox box48;//Excel文件加解密	云端	运算方法
	JComboBox box49;//Excel文件加解密	云端	条件选择
	JTextArea texta46;//Excel文件加解密	保序提示
	JTextArea texta411;//Excel文件加解密	算术提示
	int excelLine;//Excel文件的行数

	String nomalSearchName;//普通检索
	String BFSearchName;//BF加密检索

	JCheckBox checkBox[]=null;//检索文件下载选择框
	JScrollPane scrollPaneCheckBox ;
	JPanel panelCheckBox;

	JLabel Label100;//云端Close
	JLabel Label101;//云端Open
	JLabel Label102;//本地Close
	JLabel Label103;//本地Open

	//public_key.txt存储的是 user1:RSA公钥，用来加密检索参数使用的，需要经过RSA加密后再上传到云端进行BF映射查找，检索参数加密时需要指定是用哪个用户的密钥，如果说要查询自己和其他用户的，那就需要多次加密检索参数，并进行多次查询，一次只能查询一个用户的，因为每个用户的密钥不同，所以检索参数加密后也不同
	//cert_user2.txt是证书，存储  授权用户&被授权用户  RSA加密过的，通过系统函数获取公钥私钥对  从"D:\\test\\client\\SPkey.dat"文件中获取密钥对，云端在查询时，根据cert_user2.txt和SPkey.dat进行解密，得到授权的用户，并在可查询范围内进行查找
	//key_user1.txt存储用户的相关信息
	//0.txt存储BF映射后 文件名，rand值和BF值，有多个文件就有多个段,rand和BF值在云端对查询关键字进行BF映射时用到，与本地文件关键字的BF映射是一样的


	public ClientView(String username){//,List<FileBean> list){
		this.username=username;
		initComponents(username);
		System.out.println("initComponents");
		drawTree1(username);
		//drawTree2();

		//DefaultMutableTreeNode root=(DefaultMutableTreeNode)((DefaultTreeModel) jTree1.getModel()).getRoot();
		//drawTree(root);
		drawTree2();

		//getfilechildren1(root);

		//updateJTree2();
	}

	private void initComponents(final String username) {
		this.setTitle("青云实验平台客户端");
		this.setSize(1020, 600);
		this.setLocation((int)((screenSize.getWidth()-this.getWidth())/2),(int)(screenSize.getHeight()-this.getHeight())/2);
		this.setLayout(new BorderLayout(10, 10));
		try {
			//设置外观
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//这是把外观设置成你所使用的平台的外观,也就是你这个程序在哪个平台运行,显示的窗口,对话框外观将是哪个平台的外观
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * 菜单栏
		 */
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		fileMenu.setText("文件");


		StopWatchMenuItem = new JMenuItem();
		StopWatchMenuItem.setText("停止监控本地文件目录");
		StartWatchMenuItem = new JMenuItem();
		StartWatchMenuItem.setText("开始监控本地文件目录");

		//fileMenu.add(exitMenuItem);
		fileMenu.add(StopWatchMenuItem);
		fileMenu.add(StartWatchMenuItem);

		switchMenu= new JMenu();
		switchMenu.setText("切换");
		switchMenuItem= new JMenuItem();
		switchMenuItem.setText("切换用户");
		switchMenu.add(switchMenuItem);

		switchMenuItem2= new JMenuItem();
		switchMenuItem2.setText("切换登录模式");
		switchMenu.add(switchMenuItem2);

		changepwdMenu= new JMenu();
		changepwdMenu.setText("修改密码");
		passwdMenuItem = new JMenuItem();
		passwdMenuItem.setText("修改密码");
		changepwdMenu.add(passwdMenuItem);


		showMenu = new JMenu();
		showMenu.setText("显示");
		showMenuItem1 = new JMenuItem();
		showMenuItem1.setText("显示本地目录");
		showMenuItem2 = new JMenuItem();
		showMenuItem2.setText("显示加密目录");
		showMenuItem3 = new JMenuItem();
		showMenuItem3.setText("显示云端已存储空间");

		showMenu.add(showMenuItem1);
		showMenu.add(showMenuItem2);
		showMenu.add(showMenuItem3);

		helpMenu = new JMenu();
		helpMenu.setText("帮助");
		aboutMenuItem = new JMenuItem();
		aboutMenuItem.setText("关于");
		helpMenuItem = new JMenuItem();
		helpMenuItem.setText("帮助");
		webMenuItem = new JMenuItem();
		webMenuItem.setText("网页版");
		exitMenuItem = new JMenuItem();
		exitMenuItem.setText("退出");
		helpMenu.add(aboutMenuItem);
		helpMenu.add(helpMenuItem);
		helpMenu.add(webMenuItem);
		helpMenu.add(exitMenuItem);


		menuBar.add(fileMenu);
		menuBar.add(switchMenu);
		menuBar.add(changepwdMenu);
		menuBar.add(showMenu);
		menuBar.add(helpMenu);

		//切换用户
		switchMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				LoginView.dialog=new LoginJFrame("青云实验平台客户端");
				LoginView. dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				LoginView.dialog.setVisible(true);
				try{
					if(listen!=null){
						listen.removeWatch();
						listen=null;
					}
				}
				catch(Exception ex){
					ex.printStackTrace();
					listen=null;
				}
				LoginView.client.setVisible(false);
				//LoginView.dialog.setVisible(true);
				// dispose();

			}
		});

		//切换登录模式
		switchMenuItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if((GlobalState.loginmodel==2)){//离线换成在线

					LoginTask login=new LoginTask(username,LoginView.password);
					Boolean flag=login.login();
					if (flag) {
						GlobalState.state="logined";
						GlobalState.loginmodel=1;//普通登陆
						LoginView.client.setVisible(false);
						LoginView.client=null;
						LoginView.client=new ClientView(username);
						LoginView.client.setVisible(true);


					}


				}
				else {//在线换成离线
					GlobalState.loginmodel=2;//离线登陆
					GlobalState.state="logined";
					LoginView.client.setVisible(false);
					LoginView.client=null;
					LoginView.client=new ClientView(username);
					LoginView.client.setVisible(true);
				}

			}
		});



		//修改密码  //待添加， 忘记密码也需要再添加
		passwdMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(GlobalState.loginmodel==2){
					JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持修改密码");
					return;
				}

				ChangePwdTask changePwd=new ChangePwdTask(LoginView.username,new String (LoginView.password));
				boolean tag=changePwd.changepwd();

			}
		});
		//显示本地目录
		showMenuItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String[] cmd = new String[5];
					String url = "D:\\"+LoginView.username;
					cmd[0] = "cmd";
					cmd[1] = "/c";
					cmd[2] = "start";
					cmd[3] = " ";
					cmd[4] = url;
					Runtime.getRuntime().exec(cmd);
				}catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		//显示加密目录
		showMenuItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String[] cmd = new String[5];
					String url = "D:\\qingyunclient";
					cmd[0] = "cmd";
					cmd[1] = "/c";
					cmd[2] = "start";
					cmd[3] = " ";
					cmd[4] = url;
					Runtime.getRuntime().exec(cmd);
				}catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});

		//显示云端已存储文件的大小
		showMenuItem3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {



				if(GlobalState.loginmodel==1){//在线的登陆才可以查询
					try{
						List<NameValuePair> formparams = new ArrayList<NameValuePair>();
						formparams.add(new BasicNameValuePair("username", username));
						HttpClient client = new DefaultHttpClient();
						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
						HttpPost httppost = new HttpPost( GlobalState.getServer() + "/getcontent");
						httppost.setEntity(entity);
						httppost.setHeader("Cookie", LoginTask.cookies);
						HttpResponse response = client.execute(httppost);
						int statusCode = response.getStatusLine().getStatusCode();
						if (statusCode != 200) {
							JOptionPane.showMessageDialog(mainPanel, "服务器错误，请稍后再试");
							response.getEntity().getContent().close();

						}
						else {
							Header[] header = response.getHeaders("content");
							if (header == null || header.length == 0) {
								JOptionPane.showMessageDialog(mainPanel,"服务器错误，请稍后再试");
								response.getEntity().getContent().close();

							}
							else{//linjiancai
								String value= header[0].getValue().toString();
								if(value.equals("")){

									response.getEntity().getContent().close();

								}else{
									//System.out.println("CERTALL  "+value);
									double doublevalue=Double.parseDouble(value);
									DecimalFormat df=new DecimalFormat("#.00");
									value=df.format(doublevalue);
									JOptionPane.showMessageDialog(mainPanel, "您一共使用了 "+value+"M的空间");
									response.getEntity().getContent().close();

								}
							}
						}
					}
					catch (Exception e1) {
						JOptionPane.showMessageDialog(mainPanel, "请检查网络连接是否正常");
						e1.printStackTrace();

					}


				}
				else {
					JOptionPane.showMessageDialog(mainPanel, "离线登录无法查询");
				}


			}
		});

		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(new JLabel(), "本软件由西安交通大学青云实验室制作\n，主要完成了“云存储与该平台下同态加密算法的应用”");
			}
		});
		helpMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(new JLabel(), "右击左侧的文件列表可以进行相应的文件操作，如上传、下载、删除等。\n主面板可以选择文件，进行加密、解密等操作。");
			}
		});

		webMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					java.net.URI uri=new java.net.URI("http://202.117.10.253:8080/qcloud");
					java.awt.Desktop.getDesktop().browse(uri);
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});

		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});



		StopWatchMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if(listen!=null){
						System.out.println("在监听，可以停止监听");
						if(listen.removeWatch()){
							JOptionPane.showMessageDialog(new JLabel(), "停止监听文件目录成功");
							System.out.println("停止监听文件目录成功  "+listen.watchID);

							listen.removeWatch();
							islistenjl.setText("没有监听本地目录");

						}else{
							JOptionPane.showMessageDialog(new JLabel(), "停止监听文件目录失败");
						}
						listen=null;
					}
					else{
						System.out.println("已经停止监听了");
						JOptionPane.showMessageDialog(new JLabel(), "本来就已经停止监听文件目录了");

					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(new JLabel(), "停止监听文件目录异常");
					ex.printStackTrace();
				}
			}
		});

		StartWatchMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(listen==null){
					System.out.println("没有监听，可以监控文件目录");
					listen = new ListenTask2(jTree2,(DefaultMutableTreeNode) ((DefaultTreeModel) jTree2.getModel()).getRoot(), "D:/"+LoginView.username);
					try {
						listen.addWatch();
						JOptionPane.showMessageDialog(new JLabel(), "开始监听文件目录成功");
						islistenjl.setText("在监听本地目录");
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(new JLabel(), "开始监听文件目录异常");
						ex.printStackTrace();
					}
				}else{
					System.out.println("已经在监听了");
					JOptionPane.showMessageDialog(new JLabel(), "本来就已经在监听了");
				}
			}
		});
		/*
		 * 面板
		 */
		//底部面板，显示登陆信息等。
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT,30, 5));
		bottomPanel.add(new JLabel("欢迎："+username));
		//bottomPanel.add(new JLabel("登陆时间："+new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
		bottomPanel.add(new JLabel("登陆时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
		try {
			bottomPanel.add(new JLabel("当前IP："+InetAddress.getLocalHost().getHostAddress()));
		} catch (UnknownHostException e) {
			bottomPanel.add(new JLabel("当前IP：获取失败"));
			e.printStackTrace();
		}
		JLabel loginmodeljl=new JLabel();
		if(GlobalState.loginmodel==2){
			loginmodeljl.setText("登录模式: 离线登陆");
		}
		else{
			loginmodeljl.setText("登录模式: 在线登陆");
		}
		bottomPanel.add(loginmodeljl);

		islistenjl=new JLabel();
		if(listen==null){
			islistenjl.setText("没有监听本地目录");
		}
		else{
			islistenjl.setText("在监听本地目录");
		}
		bottomPanel.add(islistenjl);


		JLabel nowtime=new JLabel("当前时间:");
		bottomPanel.add(nowtime);
		showtime=new JLabel();
		showtime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		bottomPanel.add(showtime);
		/*
		 * 中部面板
		 */
		mainPanel=new JPanel();
		leftPanel = new JPanel();//中左部面板，维持云端目录和客户端目录。
		leftPanel.setLayout(new BorderLayout(10, 10));
		leftPanel1 = new JPanel();//中左部面板，维持云端目录和客户端目录。
		leftPanel1.setLayout(new BorderLayout(0, 0));
		//云端目录
		jTree1 = new javax.swing.JTree();
		jPopupMenu1 = new JPopupMenu();
		JLabel Label1=new JLabel();
		Label1.setText("云端目录 ");
		jScrollPane1 = new JScrollPane();
		jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jScrollPane1.setAutoscrolls(true);
		jScrollPane1.setWheelScrollingEnabled(true);
		jScrollPane1.scrollRectToVisible(getBounds());
		jScrollPane1.setName("jScrollPane1");
		jTree1.setName("jTree1");
		jScrollPane1.setViewportView(jTree1);//设置Tree为jScrollPane的组件


		Label100=new JLabel();
		Label100.setText("Close All ");

		Label101=new JLabel();
		Label101.setText("Open All");

		JPanel leftp1=new JPanel();
		leftp1.add(Label1);
		leftp1.add(Label100);
		leftp1.add(Label101);
		leftPanel1.add(leftp1,BorderLayout.NORTH);

		//leftPanel1.add(Label1,BorderLayout.NORTH);
		leftPanel1.add(jScrollPane1,BorderLayout.SOUTH);



		//本地目录
		leftPanel2 = new JPanel();//中左部面板，维持云端目录和客户端目录。
		leftPanel2.setLayout(new BorderLayout(0, 0));
		jTree2 = new javax.swing.JTree();
		jPopupMenu2 = new JPopupMenu();
		JLabel Label2=new JLabel();
		Label2.setText("本地目录 ");
		jScrollPane2 = new JScrollPane();
		jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jScrollPane2.setAutoscrolls(true);
		jScrollPane2.setWheelScrollingEnabled(true);
		jScrollPane2.scrollRectToVisible(getBounds());
		jScrollPane2.setName("jScrollPane2");
		jTree2.setName("jTree2");
		jScrollPane2.setViewportView(jTree2);//设置Tree为jScrollPane的组件

		Label102=new JLabel();
		Label102.setText("Close All ");

		Label103=new JLabel();
		Label103.setText("Open All");

		JPanel leftp2=new JPanel();
		leftp2.add(Label2);
		leftp2.add(Label102);
		leftp2.add(Label103);
		leftPanel2.add(leftp2,BorderLayout.NORTH);

		//leftPanel2.add(Label2,BorderLayout.NORTH);
		leftPanel2.add(jScrollPane2,BorderLayout.SOUTH);
		//将本地目录和云端目录添加到左部面板
		//leftPanel.add(leftPanel1,BorderLayout.NORTH);
		//leftPanel.add(leftPanel2,BorderLayout.SOUTH);
		leftPanel.add(leftPanel1,BorderLayout.WEST);
		leftPanel.add(leftPanel2,BorderLayout.EAST);


		MyJLableMouseListen mjpl1 = new MyJLableMouseListen();
		Label100.addMouseListener(mjpl1);
		Label101.addMouseListener(mjpl1);
		Label102.addMouseListener(mjpl1);
		Label103.addMouseListener(mjpl1);

        /*
        //另外一种添加鼠标监听的方法
        Label100.addMouseListener(new MouseAdapter(){
        	public void mouseEntered(MouseEvent e) //鼠标进入
        	{
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
           }
        	public  void mouseExited(MouseEvent e)  //鼠标移除
        	{
            setCursor(Cursor.getDefaultCursor());
        	}
        }

        );
        */

		//中右部分页容器
		tabbedPane= new JTabbedPane();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		//String titles[] = {"文件加密", "检索参数加密", "文件解密", "属性加解密", "属性关键字", "证书授权"};//,"系统参数设置"};
		//int mnemonics[] = {KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5};//,KeyEvent.VK_6};
		//String titles[] = {"文件加密","文件解密","检索参数加密", "属性加解密", "属性关键字", "证书授权","系统参数设置"};
		//int mnemonics[] = {KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5,KeyEvent.VK_6};

		String titles[] = {"BF文件加解密","文件检索", "Excel加解密", "Excel密文运算", "证书授权","系统参数设置"};
		int mnemonics[] = {KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5};


		pwdd=LoginView.password;
		user_name=LoginView.username;

		try{
			File userinfo=new File("D:\\qingyunclient\\test\\client\\key_"+user_name+".txt");
			if(!userinfo.exists()){//不存在
				System.out.println("需要创建本地用户信息文件");
				aes=new AES(pwdd);
				String aes_key="AES_key:"+456123+"\r\n";
				String rsa_key1="RSA_key1:"+10000019+"\r\n";
				//String rsa_key1="RSA_key1:"+9999973+"\r\n";
				String rsa_key2="RSA_key2:"+9999991+"\r\n";
				String ppositive="Bloom_Filter_ju:"+0.0010+"\r\n";
				String stropeart_k="OPEART_key:2568941234\r\n";
				String strcescmc_k="CESCMC_key:"+47+"\r\n";
				String strcescmc_n="CESCMC_n:"+8+"\r\n";
				String uploadlimit="uploadlimit:5"+"\r\n";
				String downloadlimit="downloadlimit:10"+"\r\n";
				String ss=aes_key+rsa_key1+rsa_key2+ppositive+stropeart_k+strcescmc_k+strcescmc_n+uploadlimit+downloadlimit;
				System.out.println(ss);
				String res3=aes.encrypt(ss);
				writeFile(res3,user_name);
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}



		System.out.println("##############################");


		String[] res=readFile(user_name,pwdd);  //System.out.println(aes.password+"----------------1");

		if(res==null)
		{
			System.out.println("读参数文件错误");

		}
		else{

			for(int i=0;i<res.length;i++){
				System.out.println(res[i]);
			}
			String key=res[0];

			aes_key=key;
			ras_key_1=res[1];
			ras_key_2=res[2];
			System.out.println("##############AES key "+aes_key);
			aes=new AES(key);	  //System.out.println(res[2]+"----------------2");
			rsa=new unpadded_RSA(res[1],res[2]);//RSA的两个密钥p和q
			Bloom_Filter_ju=Double.parseDouble(res[3]);	//BF检索中用户允许的最大误差
			opeart_k=Long.parseLong(res[4]);
			cescmc_k=Integer.parseInt(res[5]);
			cescmc_n=Integer.parseInt(res[6]);
			uploadlimit=Integer.parseInt(res[7]);
			downloadlimit=Integer.parseInt(res[8]);
			GlobalState.basicsize=uploadlimit*1024*1024;	//上传
			GlobalState.downbasicsize=downloadlimit*1024*1024;//	下载
			//影响Bloom Filter的误检率p的三个参数分别是哈希函数的个数k，位数组的大小m和集合的元素个数n(一个文件名中关键字的最多个数),
			kk=(int)(Math.log(Bloom_Filter_ju)/Math.log(0.5))+1;   //hash个数
			System.out.println("kk+"+kk);
			nn=50;                                         //kw个数
			mm=744;
		}



		for(int i=0, n=titles.length; i<n; i++) {
			JPanel panel=new JPanel();
			panel=initPanel(panel,i);
			add(tabbedPane, titles[i], mnemonics[i],panel);
		}

		JLabel Label3=new JLabel();
		Label3.setText("加解密模块");

		midPanel=new JPanel();
		midPanel.add(Label3,BorderLayout.NORTH);
		midPanel.add(tabbedPane,BorderLayout.SOUTH);

		//主面板
		mainPanel.add(leftPanel,BorderLayout.WEST);
		mainPanel.add(midPanel,BorderLayout.EAST);

		jScrollPane1.setPreferredSize(new Dimension(170,450));
		jScrollPane2.setPreferredSize(new Dimension(170,450));
		tabbedPane.setPreferredSize(new Dimension(600,470));
		midPanel.setPreferredSize(new Dimension(650,500));
		bottomPanel.setPreferredSize(new Dimension(1000,40));
		/*
		 * 把面板添加到框架
		 */
		this.add(mainPanel,BorderLayout.WEST);
		this.add(bottomPanel,BorderLayout.SOUTH);
		//this.add(tabbedPane,BorderLayout.EAST);
		this.setJMenuBar(menuBar);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		//更新时间
		Thread  thr=new Thread(new Runnable(){

			public void run(){
				while(true){
					try{
						Thread.sleep(1000);
						//showtime.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
						showtime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						Date dt=new Date();
						String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
						Calendar cal = Calendar.getInstance();
						cal.setTime(dt);
						String days="";
						int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
						if (w < 0)
							w = 0;
						days= weekDays[w];
						showtime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" "+days);

					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}

		});
		thr.start();
		//this.setVisible(true);

		/*
		aes=new AES("111111");
		//String name="name:"+text61.getText()+"\r\n";
		//String key="key:"+text62.getText()+"\r\n";
		String aes_key="AES_key:"+456123+"\r\n";
		String rsa_key1="RSA_key1:"+9999973+"\r\n";
		String rsa_key2="RSA_key2:"+9999991+"\r\n";
		String ppositive="Bloom_Filter_ju:"+0.0010+"\r\n";
		String stropeart_k="OPEART_key:2568941234\r\n";
		String strcescmc_k="CESCMC_key:"+47+"\r\n";
		String strcescmc_n="CESCMC_n:"+8+"\r\n";

		//String ss=name2+key2+aes_key+rsa_key1+rsa_key2+ppositive+strda+strdm+straspe_k+stropeart_k+strcescmc_k+strcescmc_n;
		String ss=aes_key+rsa_key1+rsa_key2+ppositive+stropeart_k+strcescmc_k+strcescmc_n;
		System.out.println(ss);
		String res3=aes.encrypt(ss);


		writeFile(res3,"user2");
		*/

	}

	public   String[] readFile(String name,String pwd){
		FileInputStream fin=null;

		String[] result=null;

		try{
			//System.out.println("D:\\test\\client\\key_"+name+".txt");
			len=9;
			fin=new FileInputStream("D:\\qingyunclient\\test\\client\\key_"+name+".txt");
			result=new String[len];
			int bt=0;
			int j=0;
			String tmp="";
			aes=new AES(pwd);//用用户的密码作为解密本地账户信息文件AES的密钥

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

	public static void writeFile(String content,String name){
		try{
			System.out.println("D:\\qingyunclient\\test\\client\\key_"+name+".txt");
			File f=new File("D:\\qingyunclient\\test\\client\\key_"+name+".txt");
			if (!f.exists()) {
				f.createNewFile();
			}
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(content);
			output.close();
		}catch(Exception e){
			e.printStackTrace();
		}

	}


	public JPanel initPanel(JPanel panel,int count) {

		switch (count){
//		--------------------------文件加解密------------------------------------
			case 0:
				JPanel panel1=new JPanel();
				panel1.setLayout(new BorderLayout(0, 0));
				JPanel panel2=new JPanel();
				JLabel j1=new JLabel("信息显示：Bloom Filter检索加解密算法");
				//final JTextArea text1=new JTextArea(15,80);
				text1=new JTextArea(15,80);
				text1.setEditable(false);
				JScrollPane scrollPane = new JScrollPane(text1);



				scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
				scrollPane.setAutoscrolls(true);
				scrollPane.setWheelScrollingEnabled(true);
				scrollPane.scrollRectToVisible(getBounds());
				scrollPane.setName("scrollPane");
				//scrollPane.add(text1);
				scrollPane.setPreferredSize(new Dimension(500,350));


				JButton button110=new JButton("选择文件");
				JButton button120=new JButton("加密");
				JButton button13=new JButton("解密");
				JButton button14=new JButton("加密目录");
				JButton button15=new JButton("解密目录");
				JButton button16=new JButton("清空");
				JButton button17=new JButton("加密上传");


				//panel1.setLayout(new BorderLayout(0, 0));
				//panel1.add(j1);
				//panel1.add(scrollPane);
				panel1.add(j1,BorderLayout.NORTH);
				//panel1.add(text1,BorderLayout.SOUTH);
				panel1.add(scrollPane,BorderLayout.SOUTH);
				panel2.add(button110);
				panel2.add(button120);
				panel2.add(button13);
				panel2.add(button17);
				panel2.add(button14);
				panel2.add(button15);
				panel2.add(button16);


				panel.add(panel1);
				panel.add(panel2);

				final String myencdir="D:\\qingyunclient\\atest\\encrypted_file\\"+username;
				final String mydecdir="D:\\qingyunclient\\atest\\decrypted_file\\"+username;
				File encdecdir=new File(myencdir);
				if(!encdecdir.exists())
					encdecdir.mkdir();

				encdecdir=new File(mydecdir);
				if(!encdecdir.exists())
					encdecdir.mkdir();

				button110.addActionListener( new ActionListener(){//选择文件
					public void actionPerformed(ActionEvent e) {

						text1.setText("");
						String[] tmm=readFile(user_name,pwdd);
						aes=new AES(tmm[0]);//AES的密钥，对文件名和文件内容进行加密，再通过BF来对加密后的文件名关键字进行映射
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
						//System.out.println(content3);
						//text1.setText(content3);

						/*
						JFileChooser chooser = new JFileChooser(new File("D:" + "/" + username));
						    FileNameExtensionFilter filter = new FileNameExtensionFilter("All Files(*.*)","xls","xlsx");
						       // "JPG & GIF Images", "jpg", "gif");
						    chooser.setFileFilter(filter);
						    chooser.setDialogTitle("文件选择");
						    chooser.showOpenDialog(null);
						    File selectedFile=chooser.getSelectedFile();
						    if(chooser.getSelectedFile()!=null) { //有选择文件时
						    	text1.setText(selectedFile.getAbsolutePath());
						    }
						    */
					}
				});
				button120.addActionListener( new ActionListener(){//加密
					public void actionPerformed(ActionEvent e) {

						String dir=text1.getText();
						if(dir==null || dir.equals("")){
							JOptionPane.showMessageDialog( mainPanel,"请先选择待加密文件");
							return ;
						}
						String[] tmm=readFile(user_name,pwdd);
						aes=new AES(tmm[0]);//AES的密钥，对文件名和文件内容进行加密，再通过BF来对加密后的文件名关键字进行映射
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
									JOptionPane.showMessageDialog( mainPanel,"文件全部为空，加密结束");
								}else{
									JOptionPane.showMessageDialog( mainPanel,"加密成功 ,加密后的文件存放在 "+myencdir+"下！");
									text1.setText(BFencresult);
								}
								//text1.setText("\n\n"+"提示：加密后的文件存放在"+"D:\\qingyunclient\\atest\\encrypted_file\\下！");


							}
							catch(Exception ex){
								text1.setText("\n\n"+"提示:加密失败，请重新加密！");
								ex.printStackTrace();
							}
						}
					/*
					String dir=text1.getText();
					fileEncrypt(dir);  //System.out.println(aes.password+"----------------4");
						//MessageBox box=new MessageBox(shella,SWT.ICON_INFORMATION);
						JOptionPane.showMessageDialog( mainPanel,"加密成功!");
						text1.setText("\n\n"+"提示：加密后的文件存放在"+"D:\\qingyunclient\\atest\\encrypted_file\\下！");
						*/
					}
				});

				button13.addActionListener( new ActionListener(){//解密
					public void actionPerformed(ActionEvent e) {


						String dir=text1.getText();
						if(dir==null || dir.equals("")){
							JOptionPane.showMessageDialog( mainPanel,"请先选择待解密文件");
							return ;
						}
						String[] tmm=readFile(user_name,pwdd);
						aes=new AES(tmm[0]);//AES的密钥，对文件名和文件内容进行加密，再通过BF来对加密后的文件名关键字进行映射
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
							JOptionPane.showMessageDialog( mainPanel,"解密成功,解密后的文件存放在 "+mydecdir+"下！");
							//text1.setText("\n\n"+"提示：解密后的文件存放在"+"D:\\atest\\decrypted_file\\下！");
							text1.setText("");
							System.out.println("BFdecresult "+BFdecresult);
							text1.append(BFdecresult);
						}


					}
				});
				button17.addActionListener( new ActionListener(){//加密结果上传
					public void actionPerformed(ActionEvent e) {
						try {
							if(GlobalState.loginmodel==2){
								JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持BF加密文件的上传");
								return;
							}




							String dir=text1.getText();
							if(dir==null || dir.equals("")){
								JOptionPane.showMessageDialog( mainPanel,"请选择待上传的加密文件，需要0.txt文件");
								return;
							}
							String[] encResultsplit=dir.split("\n");
							System.out.println("filedir len "+encResultsplit.length);
							for(int j=0;j<encResultsplit.length;j++){
								//System.out.println(encResultsplit[j]);
							}
							if(encResultsplit.length<2){
								JOptionPane.showMessageDialog( mainPanel,"请选择待上传的加密文件，需要0.txt文件");
								return;
							}
							if(!dir.contains("0.txt")){
								JOptionPane.showMessageDialog( mainPanel,"请选择待上传的加密文件，需要0.txt文件");
								return;
							}

							if(tempNodearr==null){
								JOptionPane.showMessageDialog( mainPanel,"请选择云端目录");
								return;
							}

							if(tempNodearr.length==0){
								JOptionPane.showMessageDialog( mainPanel,"请选择云端目录");
								return;
							}
							if(tempNodearr.length>1){
								JOptionPane.showMessageDialog( mainPanel,"请选择1个云端目录");
								return;
							}

							DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNodearr[0];
							FileBean fb=(FileBean)node.getUserObject();
							if(fb.isIsFile()){
								JOptionPane.showMessageDialog( mainPanel,"云端选择的是文件，请选择1个云端目录");
								return;
							}
							String parentpath=fb.getPath();
							String parentname=fb.getName();
							System.out.println("parentpath  "+parentpath+"  parentname  "+parentname);


							GlobalState.state = "upload";
							UploadTask upload=  new UploadTask(username,jTree1,mainPanel);
							try {

								//upload.upload4(parentname,parentpath,encResultsplit);//上传加密文件,有限制1m大小
								upload.upload40(parentname,parentpath,encResultsplit);//上传加密文件，没有限制大小
								updateTree10();//刷新云目录
								//updateTree20();//刷新本地目录
							}
							catch (Exception e1) {
								e1.printStackTrace();
							}


							GlobalState.state = "logined";
							System.out.println("submit local");



						}catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});


				button14.addActionListener( new ActionListener(){//显示加密
					public void actionPerformed(ActionEvent e) {
						try {
							String[] cmd = new String[5];
							//String url = "D:\\qingyunclient\\atest\\encrypted_file";
							String url = myencdir;
							cmd[0] = "cmd";
							cmd[1] = "/c";
							cmd[2] = "start";
							cmd[3] = " ";
							cmd[4] = url;
							Runtime.getRuntime().exec(cmd);
						}catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
				button15.addActionListener( new ActionListener(){//显示解密
					public void actionPerformed(ActionEvent e) {
						try {
							String[] cmd = new String[5];
							//String url = "D:\\qingyunclient\\atest\\decrypted_file";
							String url = mydecdir;
							cmd[0] = "cmd";
							cmd[1] = "/c";
							cmd[2] = "start";
							cmd[3] = " ";
							cmd[4] = url;
							Runtime.getRuntime().exec(cmd);
						}catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
				button16.addActionListener( new ActionListener(){//清空
					public void actionPerformed(ActionEvent e) {
						text1.setText("");
					}
				});


				break;



//			--------------------检索参数加密------------------------------------------
			case 1:
				JPanel Panel001=new JPanel();
				JPanel Panel011=new JPanel();
				JPanel Panel11=new JPanel();
				JPanel Panel12=new JPanel();
				JPanel Panel13=new JPanel();
				JPanel Panel14=new JPanel();
				JPanel Panel15=new JPanel();
				JPanel Panel16=new JPanel();
				panel.setLayout(new FlowLayout(FlowLayout.LEFT));

				JLabel Label1notice=new JLabel();
				Label1notice.setText("提示：普通检索和BF检索可以同时检索多个授权用户的文件");
				Label1notice.setForeground(Color.red);
				Panel001.add(Label1notice);


				JLabel Label011=new JLabel();
				Label011.setText("普通检索参数：  ");
				final JTextField text011=new JTextField(40);

				JLabel Label012=new JLabel();
				Label012.setText(" 普通检索范围：");
				final JComboBox box10=new JComboBox();

				box10.addItem("用户本人");



				JLabel Label11=new JLabel();
				Label11.setText("BF检索参数：    ");
				JLabel Label12=new JLabel();
				Label12.setText(" BF检索范围  ：");
				final JComboBox box11=new JComboBox();

				box11.addItem("用户本人");


				JLabel Label13=new JLabel();
				Label13.setText("BF参数加密结果：");
				final JTextField text11=new JTextField(40);
				final JTextField text12=new JTextField(60);
				text12.setEditable(false);

				JButton button101=new JButton();
				button101.setText("BF参数加密");
				JButton button102=new JButton();
				button102.setText("清空");
				JButton button103=new JButton();
				button103.setText("普通检索");
				JButton button104=new JButton();
				button104.setText("BF检索");
				JButton button105=new JButton();
				button105.setText("检索下载");

				JButton button106=new JButton();
				button106.setText("显示结果");

				JButton button107=new JButton();
				button107.setText("Excel运算");

				final JTextArea text101=new JTextArea(15,80);
				text101.setEditable(true);
				//text101.setText("检索结果");

				//获取可检索的用户
				String filedir="D:\\qingyunclient\\test\\client";
				File file=new File(filedir);
				if(file.isDirectory()){
					String[] tempList = file.list();
					File temp = null;
					for (int i = 0; i < tempList.length; i++) {
						if (filedir.endsWith(File.separator)) {
							temp = new File(filedir + tempList[i]);
						} else {
							temp = new File(filedir + File.separator + tempList[i]);
						}
						if(temp.isFile()){
							String filename=temp.getName();

							if(filename.startsWith("cert_"+username+"_") && filename.endsWith("_1.txt")){
								String tmpsplit[]=filename.split("_");
								box11.addItem(tmpsplit[2]);
								box10.addItem(tmpsplit[2]);
								//System.out.println(tmpsplit[2]);

							}
						}
					}
				}
				if(box10.getItemCount()>1){
					box10.addItem("全部用户");
				}

				if(box11.getItemCount()>1){
					box11.addItem("全部用户");
				}

				box11.addActionListener(new ActionListener(){ //BF检索范围
					public void actionPerformed(ActionEvent e){
						text12.setText("");
					}
				});

				JScrollPane scrollPane101 = new JScrollPane(text101);



				scrollPane101.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
				scrollPane101.setAutoscrolls(true);
				scrollPane101.setWheelScrollingEnabled(true);
				scrollPane101.scrollRectToVisible(getBounds());
				scrollPane101.setName("scrollPane101");
				scrollPane101.setPreferredSize(new Dimension(500,250));





				Panel011.add(Label011);
				Panel011.add(text011);
				Panel011.add(Label012);
				Panel011.add(box10);

				Panel11.add(Label11);
				Panel11.add(text11);
				Panel11.add(Label12);
				Panel11.add(box11);
				//Panel12.add(Label12);
				//Panel12.add(box11);
				Panel13.add(Label13);
				Panel13.add(text12);
				Panel14.add(button101);
				Panel14.add(button103);
				Panel14.add(button104);
				Panel14.add(button105);
				Panel14.add(button106);
				Panel14.add(button107);
				Panel14.add(button102);


				Panel15.setLayout(new BorderLayout(0, 0));
				JLabel j101=new JLabel("检索结果");
				Panel15.add(j101,BorderLayout.NORTH);
				Panel15.add(scrollPane101,BorderLayout.SOUTH);




				//panelCheckBox = new JPanel(new GridLayout(0,1));//一行一个组件
				panelCheckBox=new JPanel();
				//panelCheckBox.setLayout((new FlowLayout(FlowLayout.LEFT)));
				//panelCheckBox.setLayout((new FlowLayout(FlowLayout.RIGHT)));
			/*
			String[]str={"全选","a**********************************************************************************","b","c**********************************************************************************","d","e","f","g","h","i","j","k","l","全选","a","b","c","d","e","f","g","h","i","j","k","l"};
			checkBox=new JCheckBox[str.length];
			 for (int i = 0; i < str.length; i++) {
				     checkBox[i]=new JCheckBox(str[i]);
				    // scrollPaneCheckBox.add(checkBox[i]);
				     panelCheckBox.add(checkBox[i]);
				    // panelCheckBox.add
			}
		 */


				// JScrollPane scrollPaneCheckBox = new JScrollPane(panel53);
				// scrollPaneCheckBox = new JScrollPane(panelCheckBox);
				scrollPaneCheckBox = new JScrollPane(panelCheckBox);

				scrollPaneCheckBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
				scrollPaneCheckBox.setAutoscrolls(true);
				scrollPaneCheckBox.setWheelScrollingEnabled(true);
				scrollPaneCheckBox.scrollRectToVisible(getBounds());
				scrollPaneCheckBox.setName("scrollPaneCheckBox");
				//scrollPane.add(text1);
				scrollPaneCheckBox.setPreferredSize(new Dimension(500,250));




				JLabel j102=new JLabel("检索结果");


				Panel16.setLayout(new BorderLayout(0, 0));
				//Panel16.add(scrollPaneCheckBox);
				Panel16.add(j102,BorderLayout.NORTH);
				Panel16.add(scrollPaneCheckBox,BorderLayout.SOUTH);



				panel.add(Panel001);
				panel.add(Panel011);
				panel.add(Panel11);
				//panel.add(Panel12);
				panel.add(Panel13);
				//panel.add(Panel15);//文本框的话是加这个

				panel.add(Panel16);

				panel.add(Panel14);

				button101.addActionListener(new ActionListener(){//BF加密
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stubString content=text.getText();
						text101.setText("");

						if(checkBox!=null){
							for(int i=0;i<checkBox.length;i++){
								checkBox[i].setVisible(false);
								checkBox[i].removeAll();
								checkBox[i]=null;
							}
						}
						checkBox=null;
						panelCheckBox.removeAll();//删除
						panelCheckBox.updateUI();
						//scrollPaneCheckBox.removeAll();//删除

						String content=text11.getText();
						if(content.compareTo("")==0)
							JOptionPane.showMessageDialog( mainPanel,"请输入BF检索参数！");
						else{



							BloomFilter bf;
							if(mm==0) bf=new BloomFilter();
							else bf=new BloomFilter(mm,kk,nn);
							String name1=box11.getItemAt(box11.getSelectedIndex()).toString();

							//System.out.println(authors[tag][0]+",    "+authors[tag][1]);
							//其实是RSA两个参数的乘积为authors[tag][1]
							//BigInteger result=bf.ran_encrypt_para(content,authors[tag][1]);		//authors[tag][1]是用户对文件名关键字进行RSA加密时的公钥
							BigInteger result=null;
							String resultstr="";

							int getcount=box11.getItemCount();
							int selectindex=box11.getSelectedIndex();
							if(selectindex==0){	//用户本身
								result=bf.ran_encrypt_para(content,rsa.n.toString());
								resultstr=result.toString();
							}
							else if(selectindex==(getcount-1)){//全部用户
								result=bf.ran_encrypt_para(content,rsa.n.toString());
								resultstr=result.toString();
								for(int j=1;j<getcount-1;j++){
									Cert cert=new Cert();
									String res=cert.getCert1(box11.getItemAt(j).toString(),user_name);
									String keyarr[]=res.split("&");
									String rsakey1=keyarr[3];
									String rsakey2=keyarr[4];

									unpadded_RSA rsalocal=new unpadded_RSA(rsakey1,rsakey2);
									BigInteger resulttmp=bf.ran_encrypt_para(content,rsalocal.n.toString());
									resultstr=resultstr+";"+resulttmp.toString();
								}
							}
							else{
								Cert cert=new Cert();
								String res=cert.getCert1(name1,user_name);
								String keyarr[]=res.split("&");
								String rsakey1=keyarr[3];
								String rsakey2=keyarr[4];

								unpadded_RSA rsalocal=new unpadded_RSA(rsakey1,rsakey2);
								result=bf.ran_encrypt_para(content,rsalocal.n.toString());

								resultstr=result.toString();
							}

							try{//检索参数进行RSA加密，并没有BF映射和使用到rand参数
								text12.setText(resultstr);
								Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();   //获得系统粘贴板
								StringSelection textInfoSelected = new StringSelection(text11.getText()); //建立一个粘贴板内容实例.
								clipboard.setContents(textInfoSelected, null);  //将textInfoSelected加入到粘贴板中;
							}catch(Exception ef){
								ef.printStackTrace();
							}
						}
					}
				});
				button102.addActionListener(new ActionListener(){//清空
					@Override
					public void actionPerformed(ActionEvent e) {
						text011.setText("");
						text11.setText("");
						text12.setText("");
						text101.setText("");

						if(checkBox!=null){
							for(int i=0;i<checkBox.length;i++){
								checkBox[i].setVisible(false);
								checkBox[i].removeAll();
								checkBox[i]=null;
							}
						}
						checkBox=null;
						panelCheckBox.removeAll();//删除
						panelCheckBox.updateUI();
						//scrollPaneCheckBox.removeAll();//删除


					}
				});
				//普通检索
				button103.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {



						if(GlobalState.loginmodel==2){
							JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持普通检索");
							return;
						}



						text101.setText("");
						String searchparameter =text011.getText();
						if(searchparameter==null || searchparameter.equals("")){
							JOptionPane.showMessageDialog( mainPanel,"请输入普通检索参数！");
						}
						else{
							if(checkBox!=null){
								for(int i=0;i<checkBox.length;i++){
									checkBox[i].setVisible(false);
									checkBox[i].removeAll();
									checkBox[i]=null;

									;							}
							}
							checkBox=null;
							panelCheckBox.removeAll();//删除
							panelCheckBox.updateUI();
							//scrollPaneCheckBox.removeAll();//删除

							int getcount=box10.getItemCount();
							int selectindex=box10.getSelectedIndex();
							if(selectindex==0){	//用户本身
								nomalSearchName=username;
							}
							else if(selectindex==(getcount-1)){//全部用户
								nomalSearchName=username;
								for(int j=1;j<getcount-1;j++){
									nomalSearchName=nomalSearchName+";"+box10.getItemAt(j).toString();
								}
							}else{
								nomalSearchName=box10.getItemAt(selectindex).toString();
							}
							System.out.println("nomalSearchName  "+nomalSearchName);

							List<FileBean> list = null;
							try {
								HttpClient client = new DefaultHttpClient();
								List<NameValuePair> formparams = new ArrayList<NameValuePair>();
								formparams.add(new BasicNameValuePair("username", username));
								formparams.add(new BasicNameValuePair("userrole", "1"));
								formparams.add(new BasicNameValuePair("searchmodel", "1"));
								formparams.add(new BasicNameValuePair("encparameter", searchparameter));//封装的参数
								formparams.add(new BasicNameValuePair("searchfield", nomalSearchName));//检索范围
								UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");//设置编码格式
								HttpPost httppost = new HttpPost(GlobalState.server + "/bfsearchclient");//发起请求的网址,即   "http://202.117.10.253:8080/qcloud/login";
								httppost.setEntity(entity);
								HttpContext localContext = new BasicHttpContext();
								HttpResponse response = client.execute(httppost, localContext);
								int statusCode = response.getStatusLine().getStatusCode();
								if (statusCode != 200) {
									JOptionPane.showMessageDialog(mainPanel,"服务器出错，请稍后再试");

									response.getEntity().getContent().close();
									return ;
								}
								Header[] header0 = response.getHeaders("notcert");
								if (header0 == null || header0.length == 0) {
									JOptionPane.showMessageDialog(mainPanel,"信息错误");
									response.getEntity().getContent().close();
									return ;
								}
								else{
									String notcert= header0[0].getValue().toString();
									if(notcert.equals("")){
										System.out.println("notcert  ok");
										//JOptionPane.showMessageDialog(mainPanel,"没有查询到相关的文件");
										//response.getEntity().getContent().close();
										//return ;
									}
									else{
										//JOptionPane.showMessageDialog(mainPanel,"查询到  "+filenum+" 个文件");
										System.out.println("notcert "+notcert);

										JOptionPane.showMessageDialog(mainPanel,"您没有权限查询用户 "+notcert+"的文件");
										String certsplit[]=notcert.split(";");

										for(int i=0;i<certsplit.length;i++){
											String deletecertname="D:\\qingyunclient\\test\\client\\cert_"+username+"_"+certsplit[i]+"_1.txt";
											File filehere=new File(deletecertname);
											if(filehere.exists())
												filehere.delete();

											deletecertname="D:\\qingyunclient\\test\\client\\cert_"+username+"_"+certsplit[i]+"_0.txt";
											filehere=new File(deletecertname);
											if(filehere.exists())
												filehere.delete();
										}
										box10.removeAllItems();
										box11.removeAllItems();
										box10.addItem("用户本人");
										box11.addItem("用户本人");


										String filedir="D:\\qingyunclient\\test\\client";
										File file=new File(filedir);
										if(file.isDirectory()){
											String[] tempList = file.list();
											File temp = null;
											for (int i = 0; i < tempList.length; i++) {
												if (filedir.endsWith(File.separator)) {
													temp = new File(filedir + tempList[i]);
												} else {
													temp = new File(filedir + File.separator + tempList[i]);
												}
												if(temp.isFile()){
													String filename=temp.getName();

													if(filename.startsWith("cert_"+username+"_") && filename.endsWith("_1.txt")){
														String tmpsplit[]=filename.split("_");
														box11.addItem(tmpsplit[2]);
														box10.addItem(tmpsplit[2]);
														//System.out.println(tmpsplit[2]);

													}
												}
											}
										}
										if(box10.getItemCount()>1){
											box10.addItem("全部用户");
										}
										if(box11.getItemCount()>1){
											box11.addItem("全部用户");
										}

										box10.setSelectedIndex(0);
										box11.setSelectedIndex(0);

									}
								}



								Header[] header = response.getHeaders("filenum");
								if (header == null || header.length == 0) {
									JOptionPane.showMessageDialog(mainPanel,"信息错误");
									response.getEntity().getContent().close();

									return ;
								}
								else{//linjiancai
									String filenum= header[0].getValue().toString();
									if(filenum.equals("0")){
										JOptionPane.showMessageDialog(mainPanel,"没有查询到相关的文件");
										response.getEntity().getContent().close();
										return ;
									}
									else{




										checkBox=new JCheckBox[Integer.parseInt(filenum)+1];
										checkBox[0]=new JCheckBox("全选");



										ObjectInputStream os = new ObjectInputStream(response.getEntity().getContent());

										list = (List<FileBean>) os.readObject();

										System.out.println("list "+list.toString());
										Iterator it=list.iterator();
										String content3="";
										int checkcount=1;
										while(it.hasNext()){
											FileBean fb=(FileBean)it.next();
											String filepath=fb.getPath();
											double fblength=fb.getLength()*1024*1024;
											content3=content3+filepath+";"+fblength+"\n";

											checkBox[checkcount]=new JCheckBox(filepath+";"+fblength);
											checkcount++;
											//content3=content3+filepath+"\n";
											//System.out.println("fb getName "+fb.getName());
											//System.out.println("fb getPath "+fb.getPath());
											//System.out.println("fb getModifytime "+fb.getModifytime());
											//System.out.println("fb isFile "+fb.isFile());
											//System.out.println("fb getFileType "+fb.getFileType());

										}
										text101.append(content3);
										os.close();
										response.getEntity().getContent().close();

										if(checkBox.length<10)
											panelCheckBox.setLayout( new GridLayout(10,1));
										else
											panelCheckBox.setLayout( new GridLayout(checkBox.length,1));
										// panelCheckBox=new JPanel();
										for(int i=0;i<checkBox.length;i++){
											//scrollPaneCheckBox.add(checkBox[i]);
											panelCheckBox.add(checkBox[i]);
										}


										for(int j=0;j<checkBox.length;j++){
											checkBox[j].setSelected(true);
										}

										checkBox[0].addActionListener(new ActionListener(){
											public void actionPerformed(ActionEvent e){
												if(checkBox[0].isSelected()){
													for(int j=1;j<checkBox.length;j++){
														checkBox[j].setSelected(true);
													}
												}
												else{
													for(int j=1;j<checkBox.length;j++){
														checkBox[j].setSelected(false);
													}
												}
											}
										});


										scrollPaneCheckBox.updateUI();
										//scrollPaneCheckBox.add(panelCheckBox);
										//  scrollPaneCheckBox.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
										// scrollPaneCheckBox.setViewportView(panelCheckBox);
										// scrollPaneCheckBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
										// scrollPaneCheckBox.setAutoscrolls(true);
										// scrollPaneCheckBox.setWheelScrollingEnabled(true);
										// scrollPaneCheckBox.scrollRectToVisible(getBounds());
										// scrollPaneCheckBo
										//scrollPane.add(text1);



										JOptionPane.showMessageDialog(mainPanel,"查询到  "+filenum+" 个文件");
									}
								}


							} catch (IOException ex) {

								JOptionPane.showMessageDialog(mainPanel,"IO错误，请检查网络连接是否正确");
								ex.printStackTrace();
								return ;
							}catch (ClassNotFoundException ex) {

								JOptionPane.showMessageDialog(mainPanel,"系统出错，请稍后再试");
								return ;
							}
						}
					}
				});

				//BF检索
				button104.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {


						if(GlobalState.loginmodel==2){
							JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持BF检索");
							return;
						}

						text101.setText("");
						String encparameter =text12.getText();
						if(encparameter==null || encparameter.equals("")){
							JOptionPane.showMessageDialog( mainPanel,"请先加密检索参数");
						}
						else{

							if(checkBox!=null){
								for(int i=0;i<checkBox.length;i++){
									checkBox[i].setVisible(false);
									checkBox[i].removeAll();
									checkBox[i]=null;

								}
							}

							checkBox=null;
							panelCheckBox.removeAll();//删除
							panelCheckBox.updateUI();
							//scrollPaneCheckBox.removeAll();//删除

							int getcount=box11.getItemCount();
							int selectindex=box11.getSelectedIndex();
							if(selectindex==0){	//用户本身
								BFSearchName=username;
							}
							else if(selectindex==(getcount-1)){//全部用户
								BFSearchName=username;
								for(int j=1;j<getcount-1;j++){
									BFSearchName=BFSearchName+";"+box11.getItemAt(j).toString();
								}
							}
							else{
								BFSearchName=box11.getItemAt(selectindex).toString();
							}
							System.out.println("BFSearchName  "+BFSearchName);






							List<FileBean> list = null;
							try {
								HttpClient client = new DefaultHttpClient();
								List<NameValuePair> formparams = new ArrayList<NameValuePair>();
								formparams.add(new BasicNameValuePair("username", username));
								formparams.add(new BasicNameValuePair("userrole", "1"));
								formparams.add(new BasicNameValuePair("searchmodel", "2"));
								formparams.add(new BasicNameValuePair("encparameter", encparameter));//封装的参数
								formparams.add(new BasicNameValuePair("searchfield", BFSearchName));//检索范围
								UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");//设置编码格式
								HttpPost httppost = new HttpPost(GlobalState.server + "/bfsearchclient");//发起请求的网址,即   "http://202.117.10.253:8080/qcloud/login";
								httppost.setEntity(entity);
								HttpContext localContext = new BasicHttpContext();
								HttpResponse response = client.execute(httppost, localContext);
								int statusCode = response.getStatusLine().getStatusCode();
								if (statusCode != 200) {
									JOptionPane.showMessageDialog(mainPanel,"服务器出错，请稍后再试");

									response.getEntity().getContent().close();
									return ;
								}


								Header[] header0 = response.getHeaders("notcert");
								if (header0 == null || header0.length == 0) {
									JOptionPane.showMessageDialog(mainPanel,"信息错误");
									response.getEntity().getContent().close();
									return ;
								}
								else{
									String notcert= header0[0].getValue().toString();
									if(notcert.equals("")){
										System.out.println("notcert  ok");
										//JOptionPane.showMessageDialog(mainPanel,"没有查询到相关的文件");
										//response.getEntity().getContent().close();
										//return ;
									}
									else{//没有证书或者证书被删除了，同时删除本地的证书，并且更新下拉选择框
										//JOptionPane.showMessageDialog(mainPanel,"查询到  "+filenum+" 个文件");
										System.out.println("notcert "+notcert);
										JOptionPane.showMessageDialog(mainPanel,"您没有权限查询用户 "+notcert+"的文件");
										String certsplit[]=notcert.split(";");

										for(int i=0;i<certsplit.length;i++){
											String deletecertname="D:\\qingyunclient\\test\\client\\cert_"+username+"_"+certsplit[i]+"_1.txt";
											File filehere=new File(deletecertname);
											if(filehere.exists())
												filehere.delete();

											deletecertname="D:\\qingyunclient\\test\\client\\cert_"+username+"_"+certsplit[i]+"_0.txt";
											filehere=new File(deletecertname);
											if(filehere.exists())
												filehere.delete();
										}
										box10.removeAllItems();
										box11.removeAllItems();
										box10.addItem("用户本人");
										box11.addItem("用户本人");


										String filedir="D:\\qingyunclient\\test\\client";
										File file=new File(filedir);
										if(file.isDirectory()){
											String[] tempList = file.list();
											File temp = null;
											for (int i = 0; i < tempList.length; i++) {
												if (filedir.endsWith(File.separator)) {
													temp = new File(filedir + tempList[i]);
												} else {
													temp = new File(filedir + File.separator + tempList[i]);
												}
												if(temp.isFile()){
													String filename=temp.getName();

													if(filename.startsWith("cert_"+username+"_") && filename.endsWith("_1.txt")){
														String tmpsplit[]=filename.split("_");
														box11.addItem(tmpsplit[2]);
														box10.addItem(tmpsplit[2]);
														//System.out.println(tmpsplit[2]);

													}
												}
											}
										}
										if(box10.getItemCount()>1){
											box10.addItem("全部用户");
										}
										if(box11.getItemCount()>1){
											box11.addItem("全部用户");
										}

										box10.setSelectedIndex(0);
										box11.setSelectedIndex(0);


									}
								}


								Header[] header = response.getHeaders("filenum");
								if (header == null || header.length == 0) {
									JOptionPane.showMessageDialog(mainPanel,"信息错误");
									response.getEntity().getContent().close();

									return ;
								}
								else{//linjiancai
									String filenum= header[0].getValue().toString();
									if(filenum.equals("0")){
										JOptionPane.showMessageDialog(mainPanel,"没有查询到相关的文件");
										response.getEntity().getContent().close();
										return ;
									}
									else{

										checkBox=new JCheckBox[Integer.parseInt(filenum)+1];
										checkBox[0]=new JCheckBox("全选");

										ObjectInputStream os = new ObjectInputStream(response.getEntity().getContent());

										list = (List<FileBean>) os.readObject();

										System.out.println("list "+list.toString());
										Iterator it=list.iterator();
										String content3="";
										int checkcount=1;
										while(it.hasNext()){
											FileBean fb=(FileBean)it.next();
											String filepath=fb.getPath();
											double fblength=fb.getLength()*1024*1024;
											content3=content3+filepath+";"+fblength+"\n";
											//System.out.println("fb getName "+fb.getName());
											//System.out.println("fb getPath "+fb.getPath());
											//System.out.println("fb getModifytime "+fb.getModifytime());
											//System.out.println("fb isFile "+fb.isFile());
											//System.out.println("fb getFileType "+fb.getFileType());


											checkBox[checkcount]=new JCheckBox(filepath+";"+fblength);
											checkcount++;


										}
										text101.append(content3);
										os.close();
										response.getEntity().getContent().close();

										if(checkBox.length<10)
											panelCheckBox.setLayout( new GridLayout(10,1));
										else
											panelCheckBox.setLayout( new GridLayout(checkBox.length,1));


										for(int i=0;i<checkBox.length;i++){
											//scrollPaneCheckBox.add(checkBox[i]);
											panelCheckBox.add(checkBox[i]);
										}


										for(int j=0;j<checkBox.length;j++){
											checkBox[j].setSelected(true);
										}

										checkBox[0].addActionListener(new ActionListener(){
											public void actionPerformed(ActionEvent e){
												if(checkBox[0].isSelected()){
													for(int j=1;j<checkBox.length;j++){
														checkBox[j].setSelected(true);
													}
												}
												else{
													for(int j=1;j<checkBox.length;j++){
														checkBox[j].setSelected(false);
													}
												}
											}
										});

										panelCheckBox.setLayout( new GridLayout(checkBox.length,1));
										// scrollPaneCheckBox.add(panelCheckBox);
										//scrollPaneCheckBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
										// scrollPaneCheckBox.setAutoscrolls(true);
										// scrollPaneCheckBox.setWheelScrollingEnabled(true);
										// scrollPaneCheckBox.scrollRectToVisible(getBounds());



										JOptionPane.showMessageDialog(mainPanel,"查询到  "+filenum+" 个文件");
									}
								}


							} catch (IOException ex) {

								JOptionPane.showMessageDialog(mainPanel,"IO错误，请检查网络连接是否正确");
								ex.printStackTrace();
								return ;
							}catch (ClassNotFoundException ex) {

								JOptionPane.showMessageDialog(mainPanel,"系统出错，请稍后再试");
								return ;
							}
						}

					}
				});
				//检索下载
				button105.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(GlobalState.loginmodel==2){
							JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持检索下载");
							return;
						}
						//用checkBox  方法1
						if(checkBox==null){
							JOptionPane.showMessageDialog( mainPanel,"请先进行检索");
						}
						else{
							String searchResult ="";
							for(int i=1;i<checkBox.length;i++){
								if(checkBox[i].isSelected()){
									searchResult=searchResult+checkBox[i].getText().toString()+"\n";
								}
							}
							System.out.println("searchResult \n"+searchResult);
							if(searchResult.equals("")){
								JOptionPane.showMessageDialog( mainPanel,"请先选择要下载的文件");
								return;
							}
						/*
					}
					//这个是用文本框的，也可以用来判定和下载  方法2
					String searchResult =text101.getText();
					if(searchResult==null || searchResult.equals("")){
						JOptionPane.showMessageDialog( mainPanel,"请先进行检索");
					}
					else{*/
							String[] searchResultsplit=searchResult.split("\n");
							System.out.println("filedir len "+searchResultsplit.length);
							for(int j=0;j<searchResultsplit.length;j++){
								//System.out.println(searchResultsplit[j]);
							}
							String[] filedirarr=new String[searchResultsplit.length];
							String[] filelengtharr=new String[searchResultsplit.length];
							for(int j=0;j<searchResultsplit.length;j++){
								String tmpsplit[]=searchResultsplit[j].split(";");
								filedirarr[j]=tmpsplit[0];
								filelengtharr[j]=tmpsplit[1];
								//System.out.println("-----------------------");
								//System.out.println(filedirarr[j]);
								//System.out.println(filelengtharr[j]);
							}


							GlobalState.state = "download";
							DownloadTask download= new DownloadTask(jTree2);
							//download.download3(filedirarr,filelengtharr);//本人用户检索下载
							download.download4(filedirarr,filelengtharr,username);//证书检索下载


							GlobalState.state = "logined";
							updateTree20();
							System.out.println("download cloud file");

						}
					}
				});
				//显示结果
				button106.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							String[] cmd = new String[5];
							String url = "D:\\"+username;
							cmd[0] = "cmd";
							cmd[1] = "/c";
							cmd[2] = "start";
							cmd[3] = " ";
							cmd[4] = url;
							Runtime.getRuntime().exec(cmd);
						}catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});

				//Excel文件选择
				button107.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						try {

							if(GlobalState.loginmodel==2){
								JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持Excel运算");
								return;
							}

							if(checkBox==null){
								JOptionPane.showMessageDialog( mainPanel,"请先进行检索");
							}
							else{
								String searchResult ="";
								int getcount=0;
								for(int i=1;i<checkBox.length;i++){
									if(checkBox[i].isSelected()){
										getcount++;
										searchResult=searchResult+checkBox[i].getText().toString();
									}
								}
								if(getcount != 1){
									JOptionPane.showMessageDialog( mainPanel,"请先选择1个Excel文件");
									return;
								}

								System.out.println("searchResult \n"+searchResult);
								if(searchResult.equals("")){
									JOptionPane.showMessageDialog( mainPanel,"请先选择1个Excel文件");
									return;
								}

								String strarr[]=searchResult.split(";");
								String filepath=strarr[0];
								String fileuser=filepath.substring(filepath.indexOf("/")+1);
								fileuser=fileuser.substring(0,fileuser.indexOf("/"));
								String filename=filepath.substring(filepath.lastIndexOf("/")+1);

								System.out.println("fileuser \n"+fileuser);
								System.out.println("filename \n"+filename);

								//分本人和其他用户


								try {

									HttpClient client = new DefaultHttpClient();//HttpClient不支持流输出
									List<NameValuePair> formparams = new ArrayList<NameValuePair>();
									formparams.add(new BasicNameValuePair("username", LoginView.username));
									formparams.add(new BasicNameValuePair("userrole", "1"));
									formparams.add(new BasicNameValuePair("path",filepath)); //文件路径
									formparams.add(new BasicNameValuePair("name",filename));//文件名
									formparams.add(new BasicNameValuePair("fileuser",fileuser));//文件的所有者

									UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");//设置编码格式
									HttpPost httppost = new HttpPost(GlobalState.server+ "/getExcelInfo");//发起请求的网址,即   "http://202.117.10.253:8080/qcloud/login";
									httppost.setHeader("Cookie", LoginTask.cookies);
									httppost.setEntity(entity);
									HttpContext localContext = new BasicHttpContext();
									HttpResponse response = client.execute(httppost, localContext);
									int statusCode = response.getStatusLine().getStatusCode();
									if (statusCode == 200) {

										Header[] header = response.getHeaders("info");
										if (header == null || header.length == 0) {
											JOptionPane.showMessageDialog(mainPanel,"获取Excel文件信息失败，您没有 "+fileuser+" 用户的写权限");

											//response.getEntity().getContent().close()


										}
										else{//linjiancai
											String value= header[0].getValue().toString();
											value=URLDecoder.decode(value,"UTF-8");
											//System.out.println("value "+value);
											if(value.equals("")|| value==null  || value.equals("null")){
												JOptionPane.showMessageDialog(mainPanel,"获取Excel文件信息失败，您没有 "+fileuser+" 用户的写权限");
											}else{

												String[] infoarr=value.split(";");
												excelLine=Integer.parseInt(infoarr[infoarr.length-1].substring(0,infoarr[infoarr.length-1].indexOf("行")));//行数
												System.out.println("excelLine "+excelLine);

												text42.setText(filepath);
												text43.setText(value);//Excel文件加解密	文件基本信息，条数，属性  云端

												box44.removeAllItems();		//Excel文件加解密	云端	保序属性
												box44.addItem("请选择保序加密属性");
												box44.setSelectedIndex(0);

												box47.removeAllItems();;//Excel文件加解密	云端	算术属性
												box47.addItem("请选择算术加密属性");
												box47.setSelectedIndex(0);


												for(int i=0;i<infoarr.length;i++){
													String tmp=infoarr[i];//1@#ID
													if(tmp.startsWith("1@#")){
														box44.addItem(""+i+":"+tmp.substring(3));
													}
													else if(tmp.startsWith("2@#")){
														box47.addItem(""+i+":"+tmp.substring(3));
													}
												}





												text45.setText("");//保序关键字
												text451.setText("");//下边界
												text452.setText("");	//上边界
												texta46.setText("");//Excel文件加解密	保序提示


												box48.setSelectedIndex(0);//加减乘除
												box49.setSelectedIndex(0);//条件选择
												text410.setText("");		//算术关键字
												text4101.setText("");	//行
												texta411.setText("");//Excel文件加解密	算术提示

												tabbedPane.setSelectedIndex(3);
											}
										}

									}
									else{
										System.out.println(response.getStatusLine().getStatusCode());
										JOptionPane.showMessageDialog(new JLabel(), "服务器出错，请稍后再试");
									}
									response.getEntity().getContent().close();


								} catch (Exception ex) {
									JOptionPane.showMessageDialog(new JLabel(), "请检查网络连接是否正常");
									ex.printStackTrace();
								}







							}




						}catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});


				break;



//			--------------------Excel文件加解密------------------------------------------
			case 2:

				panel.setLayout(new FlowLayout(FlowLayout.LEFT));
				JPanel jpanel30=new JPanel();
				JPanel jpanel301=new JPanel();
				JPanel jpanel31=new JPanel();
				JPanel jpanel32=new JPanel();
				JPanel jpanel33=new JPanel();
				JPanel jpanel34=new JPanel();
				JPanel jpanel35=new JPanel();
				JPanel jpanel36=new JPanel();

			/*
			JTextArea text30=new JTextArea(2,60);
			text30.setEditable(false);
			text30.setText("提示：Excel文件，保序加密算法用来进行关系运算(检索)，算术加密算法用来进行数值数据的加减乘除运算\n保序加密算法可加密数值数据和字符串，算术加密算法可加密数值数据");
			text30.setForeground(Color.red);
			jpanel30.add(text30);
			*/

				JLabel Label30=new JLabel();
				Label30.setText("提示：Excel文件，保序加密算法用来进行关系运算(检索)，算术加密算法用来进行数值数据的加减乘除运算\n");
				Label30.setForeground(Color.red);
				jpanel30.add(Label30);

				JLabel Label301=new JLabel();
				Label301.setText("保序加密算法可加密数值数据和字符串，算术加密算法可加密数值数据");
				Label301.setForeground(Color.red);
				jpanel301.add(Label301);

				JLabel Label31=new JLabel();
				Label31.setText("加解密属性：");
				box31=new JComboBox();
				box31.addItem("请选择加解密属性");
				box31.setSelectedIndex(0);

				JLabel Label32=new JLabel();
				Label32.setText("	加解密算法：");
				box32=new JComboBox();
				box32.addItem("请选择加解密算法");
				box32.addItem("保序加密算法");
				box32.addItem("算术加密算法");
				box32.setSelectedIndex(0);

				jpanel31.add(Label31);
				jpanel31.add(box31);
				jpanel31.add(Label32);
				jpanel31.add(box32);

				JLabel Label33=new JLabel();
				Label33.setText("选择的文件路径：");
				text33=new JTextField(70);
				text33.setEditable(false);

				jpanel32.add(Label33);
				jpanel32.add(text33);

				JLabel Label34=new JLabel();
				Label34.setText("Excel文件基本信息：");
				text34=new JTextArea(18,36);
				text34.setEditable(false);
				JScrollPane scrollPane34 = new JScrollPane(text34);
				scrollPane34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
				scrollPane34.setAutoscrolls(true);
				scrollPane34.setWheelScrollingEnabled(true);
				scrollPane34.scrollRectToVisible(getBounds());
				scrollPane34.setName("scrollPane34");
				scrollPane34.setPreferredSize(new Dimension(280,250));

				jpanel33.setLayout(new BorderLayout(0, 0));
				jpanel33.add(Label34,BorderLayout.NORTH);
				jpanel33.add(scrollPane34,BorderLayout.SOUTH);

				JLabel Label35=new JLabel();
				Label35.setText("选择的属性与算法：");
				text35=new JTextArea(18,36);
				text35.setEditable(false);
				JScrollPane scrollPane35 = new JScrollPane(text35);
				scrollPane35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
				scrollPane35.setAutoscrolls(true);
				scrollPane35.setWheelScrollingEnabled(true);
				scrollPane35.scrollRectToVisible(getBounds());
				scrollPane35.setName("scrollPane35");
				scrollPane35.setPreferredSize(new Dimension(280,250));

				jpanel34.setLayout(new BorderLayout(0, 0));
				jpanel34.add(Label35,BorderLayout.NORTH);
				jpanel34.add(scrollPane35,BorderLayout.SOUTH);

				jpanel35.add(jpanel33,BorderLayout.WEST);
				jpanel35.add(jpanel34,BorderLayout.EAST);


				JButton button31=new JButton();
				button31.setText("选择文件");
				JButton button32=new JButton();
				button32.setText("属性加密");
				JButton button33=new JButton();
				button33.setText("属性解密");
				JButton button34=new JButton();
				button34.setText("清空");
				JButton button35=new JButton();
				button35.setText("打开文件");
				JButton button36=new JButton();
				button36.setText("打开目录");


				jpanel36.add(button31);
				jpanel36.add(button32);
				jpanel36.add(button33);
				jpanel36.add(button34);
				jpanel36.add(button35);
				jpanel36.add(button36);




				panel.add(jpanel30);
				panel.add(jpanel301);
				panel.add(jpanel31);
				panel.add(jpanel32);
				panel.add(jpanel33);
				panel.add(jpanel34);
				panel.add(jpanel35);
				panel.add(jpanel36);


				box31.addActionListener(new ActionListener(){ //属性选择
					public void actionPerformed(ActionEvent e){
						box32.setSelectedIndex(0);
					}
				});
				box32.addActionListener(new ActionListener(){ //算法选择
					public void actionPerformed(ActionEvent e){
						int porperty=box31.getSelectedIndex();
						int enctype=box32.getSelectedIndex();
						if(porperty>0 && enctype > 0) {
							String por_enc=porperty+":"+box31.getItemAt(porperty).toString()+","+enctype+":"+box32.getItemAt(enctype).toString();
							String property = box31.getItemAt(porperty).toString();
							String total= text35.getText();
							if(total.contains(property)){
								JOptionPane.showMessageDialog( mainPanel,"该属性已经选择了，请选择其他属性");
							}
							else {
								if(!total.contains(por_enc)){
									total=por_enc+"\n"+total;
									text35.setText(total);
								}
							}
						}
					}
				});

				//选择文件
				button31.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){

					/*//方法1
					 JFileChooser chooser = new JFileChooser(new File("D:" + "/" + username));
					    FileNameExtensionFilter filter = new FileNameExtensionFilter("All Files(*.*)","xls","xlsx");
					       // "JPG & GIF Images", "jpg", "gif");
					    chooser.setFileFilter(filter);
					    chooser.setDialogTitle("文件选择");
					    chooser.showOpenDialog(null);
					    File selectedFile2=chooser.getSelectedFile();
					*/
					/*//方法2
					 System.out.println("dialog");
					java.awt.FileDialog fd=new java.awt.FileDialog(new JFrame(),"选择文档",java.awt.FileDialog.LOAD);
					 FilenameFilter ff=new FilenameFilter(){
					   public boolean accept(File dir, String name) {
					    if (name.endsWith(".xls") || name.endsWith(".xlsx")){
					     return true;
					    }
					    return false;
					   }
					  };
					  fd.setFilenameFilter(ff);
					 fd.setVisible(true);
					 System.out.println(fd.getDirectory()+fd.getFile());
					if(true)
						return;

					 */
						//方法3
						Shell shellb=new Shell(SWT.CLOSE|SWT.MAX);


						//Shell shellb=new Shell();
						//JFrame jf=new JFrame();
						//jf.setPreferredSize((new Dimension(250,150)));
						//java.awt.FileDialog dialog=new java.awt.FileDialog(new JFrame(),"文件选择",java.awt.FileDialog.LOAD);

						FileDialog dialog=new FileDialog(shellb,SWT.OPEN);
						//FileDialog dialog=new FileDialog(new JFrame(),SWT.MULTI);
						dialog.setFileName("我的文档");
						dialog.setFilterExtensions(new String[] {"*.xls*","*.xlsx","*"});
						dialog.setFilterNames(new String[] {"Excel文件(*.xls)","Excel文件(*.xlsx)"});
						dialog.open();
						String filename=dialog.getFileName();
						String fileparentpath=dialog.getFilterPath();
						String filepath=fileparentpath+"\\"+filename;

						//String[] filenames=dialog.getFileNames();
						System.out.println("filename  "+filename);
						if(!(filename.endsWith(".xls") || filename.endsWith(".xlsx"))){//不是excel文件，返回
							JOptionPane.showMessageDialog(mainPanel,"请选择Excel文件");
							return;
						}
						box31.removeAllItems();
						box31.addItem("请选择加解密属性");
						box31.setSelectedIndex(0);
						box32.setSelectedIndex(0);
						text33.setText(filepath);
						String fileinfo="";
						File selectedFile=new File(filepath);
						if (selectedFile.exists()) { //文件存在时
							try {
								FileInputStream finput = new FileInputStream(selectedFile);
								POIFSFileSystem fs = new POIFSFileSystem(finput);//Excel读入系统
								HSSFWorkbook wb = new HSSFWorkbook(fs);//获取Excel文件
								HSSFSheet sheet = wb.getSheetAt(0);//获取Excel文件的第一个工作页面
								HSSFRow row = sheet.getRow(0);//第一行
								HSSFRow row2 = sheet.getRow(1);//第二行
								int rsColumns =row.getLastCellNum();//获取Sheet表中所包含的总列数
								for(int i=0;i<rsColumns;i++) {
									HSSFCell cell   = row.getCell(i);//得到第一行的各列单元格，即属性
									String getexcle=cell.getStringCellValue();
									box31.addItem(getexcle);

									HSSFCell cell2  = row2.getCell(i);
									if (cell2.getCellType() == HSSFCell.CELL_TYPE_STRING) { //单元格是字符串
										fileinfo=fileinfo+getexcle+"	字符串型       数据:"+cell2.getStringCellValue()+"...\n";
									}
									else if(cell2.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { //单元格是数字
										fileinfo=fileinfo+getexcle+"	数值型         数据:"+cell2.getNumericCellValue()+"...\n";
									}

								}
								finput.close();

							}
							catch(Exception ex)
							{
								ex.printStackTrace();
							}
							text34.setText(fileinfo);
							text35.setText("");
						}
						else{
							JOptionPane.showMessageDialog(mainPanel,"该文件不存在");
						}
					}
				});


				//属性加密
				button32.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String filePath=text33.getText();
						int type=0;//加密
						int enc_property=0;//加密属性
						int enc_type=0;//加密算法
						enc_property= box31.getSelectedIndex();
						enc_type=box32.getSelectedIndex();
						String total=text35.getText();
						System.out.println(total);
						if((!filePath.equals("") && filePath!=null)&& (!total.equals("") && total!=null)){
							String[] arrselect=total.split("\n");
							int[] protype=new int[arrselect.length];//加密属性
							int[] enctype=new int[arrselect.length];//加密算法
							int count=0;
							int count1=0;
							for(int i=0;i<arrselect.length;i++)	{
								String[] str=arrselect[i].split(",");  // 1:工资,2:算术算法
								String[] str2=str[0].split(":");
								protype[i]=Integer.valueOf(str2[0]);
								str2=str[1].split(":");
								enctype[i]=Integer.valueOf(str2[0]);
							}
							for(int i=0;i<protype.length;i++){
								enc_property=protype[i];
								enc_type=enctype[i];
								int result= OP_CE_enc_dec(type,enc_property,enc_type,filePath) ; ;
								switch(result){
									case 0:
										break;
									case 1: count++;//box.setMessage("OPEART加密成功！");
										break;
									case 3:count1=1;//box.setMessage("ASPE算法只支持数字加密！");
										break;
									case 5:count++;//box.setMessage("ASPE加密成功！");
										break;
									case 7:count1=1;//box.setMessage("加解密失败！");
										break;
									default:
								}
							}
							if(count>0){// 部分加密成功

								if(count1==1){ //失败
									JOptionPane.showMessageDialog(mainPanel, "部分加密成功！");
								}
								else {
									JOptionPane.showMessageDialog(mainPanel, "加密成功！");
								}
							}
							else {
								JOptionPane.showMessageDialog(mainPanel, "加密失败！");
							}
							updateTree20();//更新本地树
						}
						else {
							JOptionPane.showMessageDialog(mainPanel, "请选择Excel文件、加密密属性、加密算法！");
						}
						String filepath=text33.getText().toString().trim();
						String fileinfo="";
						File selectedFile=new File(filepath);
						box31.removeAllItems();
						box31.addItem("请选择加解密属性");
						box31.setSelectedIndex(0);
						box32.setSelectedIndex(0);
						try {
							FileInputStream finput = new FileInputStream(selectedFile);
							POIFSFileSystem fs = new POIFSFileSystem(finput);//Excel读入系统
							HSSFWorkbook wb = new HSSFWorkbook(fs);//获取Excel文件
							HSSFSheet sheet = wb.getSheetAt(0);//获取Excel文件的第一个工作页面
							HSSFRow row = sheet.getRow(0);//第一行
							HSSFRow row2 = sheet.getRow(1);//第二行
							int rsColumns =row.getLastCellNum();//获取Sheet表中所包含的总列数
							for(int i=0;i<rsColumns;i++) {
								HSSFCell cell   = row.getCell(i);//得到第一行的各列单元格，即属性
								String getexcle=cell.getStringCellValue();
								box31.addItem(getexcle);

								HSSFCell cell2  = row2.getCell(i);
								if (cell2.getCellType() == HSSFCell.CELL_TYPE_STRING) { //单元格是字符串
									fileinfo=fileinfo+getexcle+"	字符串型       数据:"+cell2.getStringCellValue()+"...\n";
								}
								else if(cell2.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { //单元格是数字
									fileinfo=fileinfo+getexcle+"	数值型         数据:"+cell2.getNumericCellValue()+"...\n";
								}

							}
							finput.close();

						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
						text34.setText(fileinfo);
						text35.setText("");
					}
				});
				//属性解密
				button33.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String newfilepath="";
						String filePath=text33.getText();
						int type=1;//解密
						int enc_property=0;//加密属性
						int enc_type=0;//加密算法
						enc_type=box32.getSelectedIndex();
						enc_property= box31.getSelectedIndex();
						String total=text35.getText();
						System.out.println(total);
						if(!filePath.equals("") && filePath!=null){
							if((!filePath.equals("") && filePath!=null)&& (total.equals("") || total==null)){
								System.out.println("直接解密");
								newfilepath=OP_CE__dirdec(filePath);
								updateTree20();//更新本地树
							}
							else if((!filePath.equals("") && filePath!=null)&& (!total.equals("") && total!=null)){
								String[] arrselect=total.split("\n");
								int[] protype=new int[arrselect.length];
								int[] enctype=new int[arrselect.length];
								int count=0;
								int count1=0;
								for(int i=0;i<arrselect.length;i++)	{
									String[] str=arrselect[i].split(",");  // 1:工资,2:CESC算法
									String[] str2=str[0].split(":");
									protype[i]=Integer.valueOf(str2[0]);
									str2=str[1].split(":");
									enctype[i]=Integer.valueOf(str2[0]);
								}
								for(int i=0;i<protype.length;i++){
									enc_property=protype[i];
									enc_type=enctype[i];
									int result= OP_CE_enc_dec(type,enc_property,enc_type,filePath) ;
									switch(result){
										case 0:
											break;
										case 2: count++;//box.setMessage("OPEART解密成功！");
											break;
										case 4:count++;//box.setMessage("ASPE解密成功！");
											break;
										case 6:count1=1;//box.setMessage("ASPE解密失败！");
											break;
										case 7:count1=1;//box.setMessage("加解密失败！");
											break;
										default:
									}
								}
								if(count>0){// 部分加密成功
									if(count1==1){ //失败
										JOptionPane.showMessageDialog(mainPanel, "部分解密成功！");
									}
									else {
										JOptionPane.showMessageDialog(mainPanel, "解密成功！");
									}
								}
								else {
									System.out.println(count1);
									JOptionPane.showMessageDialog(mainPanel, "解密失败！");
								}

								updateTree20();//更新本地树
							}


							String filepath=text33.getText().toString().trim();
							String fileinfo="";
							File selectedFile=null;
							if(newfilepath.equals("")){//没有更改名字
								selectedFile=new File(filepath);
							}
							else{
								//JOptionPane.showMessageDialog(mainPanel, "改名了");
								text33.setText(newfilepath);
								selectedFile=new File(newfilepath);
							}


							box31.removeAllItems();
							box31.addItem("请选择加解密属性");
							box31.setSelectedIndex(0);
							box32.setSelectedIndex(0);
							try {
								FileInputStream finput = new FileInputStream(selectedFile);
								POIFSFileSystem fs = new POIFSFileSystem(finput);//Excel读入系统
								HSSFWorkbook wb = new HSSFWorkbook(fs);//获取Excel文件
								HSSFSheet sheet = wb.getSheetAt(0);//获取Excel文件的第一个工作页面
								HSSFRow row = sheet.getRow(0);//第一行
								HSSFRow row2 = sheet.getRow(1);//第二行
								int rsColumns =row.getLastCellNum();//获取Sheet表中所包含的总列数
								for(int i=0;i<rsColumns;i++) {
									HSSFCell cell   = row.getCell(i);//得到第一行的各列单元格，即属性
									String getexcle=cell.getStringCellValue();
									box31.addItem(getexcle);

									HSSFCell cell2  = row2.getCell(i);
									if (cell2.getCellType() == HSSFCell.CELL_TYPE_STRING) { //单元格是字符串
										fileinfo=fileinfo+getexcle+"	字符串型       数据:"+cell2.getStringCellValue()+"...\n";
									}
									else if(cell2.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { //单元格是数字
										fileinfo=fileinfo+getexcle+"	数值型         数据:"+cell2.getNumericCellValue()+"...\n";
									}

								}
								finput.close();

							}
							catch(Exception ex)
							{
								ex.printStackTrace();
							}
							text34.setText(fileinfo);
							text35.setText("");
						}
						else {
							JOptionPane.showMessageDialog(mainPanel, "请选Excel择文件、解密属性、解密算法！");
						}
						// text35.setText("");


					}

				});

				//清空
				button34.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){

						String serverhttp=GlobalState.server;
					/*
					  HttpClient client = new DefaultHttpClient();
			            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			            formparams.add(new BasicNameValuePair("username", username));
			            formparams.add(new BasicNameValuePair("userpassword", password));
			            formparams.add(new BasicNameValuePair("useremail",email ));
			            formparams.add(new BasicNameValuePair("client", "1"));//封装的参数
			            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");//设置编码格式
			            HttpPost httppost = new HttpPost(serverhttp + "/register3");//发起请求的网址,即   "http://202.117.10.253:8080/qcloud/login";
			            httppost.setEntity(entity);
			            HttpContext localContext = new BasicHttpContext();
			            HttpResponse response = client.execute(httppost, localContext);
			            int statusCode = response.getStatusLine().getStatusCode();


					List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		 	        formparams.add(new BasicNameValuePair("root", fb.getPath()));
		 	        HttpClient client = new DefaultHttpClient();
		 	        //UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		 	        URI uri = URIUtils.createURI("http", serverip, 8080, "/qcloud/getfiles", URLEncodedUtils.format(formparams, "UTF-8"), null);
		 	        HttpGet hg = new HttpGet(uri);
		 	        hg.setHeader("Cookie", LoginTask.cookies);
		 	        HttpResponse response = client.execute(hg);
		 	        int statusCode = response.getStatusLine().getStatusCode();
		 	        if (statusCode != 200) {
		 	        	JOptionPane.showMessageDialog(mainPanel,"服务器出错");
		 	            response.getEntity().getContent().close();
		 	            return ;
		 	        }

		 	       String pStr = URLEncoder.encode(fb1.getPath(), "UTF-8");
	     	        URL url = new URL(serverhttp + "/download?path=" + pStr);
	     	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	     	        urlConnection.setRequestProperty("Cookie", LoginTask.cookies);
	     	        urlConnection.setRequestMethod("GET");
	     	        BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
	     	        byte[] buffer = new byte[length];

	     	        */
						try{
	     	        	/*
	     	        	System.out.println("************");
	     	       String pStr = URLEncoder.encode("user1", "UTF-8");
    				URL url = new URL(serverhttp + "/submitt?path=" + pStr);
        	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        	        urlConnection.setRequestProperty("Cookie", LoginTask.cookies);
        	        urlConnection.setRequestMethod("GET");
        	        urlConnection.setDoOutput(true);
        	        urlConnection.setDoInput(true);
        	        System.out.println("************");
        	        */

							//	System.out.println("正在download:"+fb1.getPath());
	     	       /*
	     	        String pStr = URLEncoder.encode("/user1/aaa.tmp", "UTF-8");
	     	        URL url = new URL(serverhttp + "/submitt?path=" + pStr);
	     	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	     	        urlConnection.setRequestProperty("Cookie", LoginTask.cookies);
	     	        urlConnection.setRequestMethod("GET");
	     	        BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

	     	        in.close();
	  */

							text33.setText("");
							text34.setText("");
							text35.setText("");
							box31.removeAllItems();
							box31.addItem("请选择加解密属性");
							box31.setSelectedIndex(0);
							box32.setSelectedIndex(0);
						}catch(Exception ex){
							ex.printStackTrace();
						}

					}
				});
				button35.addActionListener(new ActionListener(){ //打开文件
					public void actionPerformed(ActionEvent e){
						String filePath=text33.getText();
						if(!filePath.equals("")){
							try {
								String[] cmd = new String[5];
								String url = filePath;
								cmd[0] = "cmd";
								cmd[1] = "/c";
								cmd[2] = "start";
								cmd[3] = " ";
								cmd[4] = url;
								Runtime.getRuntime().exec(cmd);
							}catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						else{
							JOptionPane.showMessageDialog(new JLabel(), "请选择Excel文件");
						}
					}
				});
				button36.addActionListener(new ActionListener(){ //打开文件目录
					public void actionPerformed(ActionEvent e){
						String filePath=text33.getText();
						if(!filePath.equals("")){
							System.out.println(filePath);
							String parentfile=filePath.substring(0,filePath.lastIndexOf("\\"));
							System.out.println(parentfile);
							try {
								String[] cmd = new String[5];
								//String url = "D:\\atest\\decrypted_file";
								String url = parentfile;
								cmd[0] = "cmd";
								cmd[1] = "/c";
								cmd[2] = "start";
								cmd[3] = " ";
								cmd[4] = url;
								Runtime.getRuntime().exec(cmd);
							}catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						else{
							File file = new File("D:" + "/" + username);
							String dir=file.toString();
							try {
								String[] cmd = new String[5];
								String url = dir;
								cmd[0] = "cmd";
								cmd[1] = "/c";
								cmd[2] = "start";
								cmd[3] = " ";
								cmd[4] = url;
								Runtime.getRuntime().exec(cmd);
							}catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				});
				break;
//			-----------------------属性关键字---------------------------------------
			case 3:


				panel.setLayout(new FlowLayout(FlowLayout.LEFT));
				JPanel jpanel40=new JPanel();
				JPanel jpanel41=new JPanel();
				JPanel jpanel42=new JPanel();
				JPanel jpanel43=new JPanel();
				JPanel jpanel44=new JPanel();
				JPanel jpanel45=new JPanel();
				JPanel jpanel451=new JPanel();
				JPanel jpanel46=new JPanel();
				JPanel jpanel47=new JPanel();
				JPanel jpanel48=new JPanel();
				JPanel jpanel49=new JPanel();
				JPanel jpanel410=new JPanel();
				JPanel jpanel411=new JPanel();
				JPanel jpanel412=new JPanel();
				JPanel jpanel4bt=new JPanel();
				JPanel jpanel4lr=new JPanel();
				JPanel jpanel4left=new JPanel();
				JPanel jpanel4right=new JPanel();

				JLabel Label40=new JLabel();
				Label40.setText("提示：请选择云端的Excel文件，进行密文的关系运算和算术运算");
				Label40.setForeground(Color.red);
				jpanel40.add(Label40);

				JLabel Label41=new JLabel();
				Label41.setText("保序加密算法用来进行关系运算(检索)，算术加密算法用来进行数值数据的加减乘除运算");
				Label41.setForeground(Color.red);
				jpanel41.add(Label41);



			/*
			JLabel Label31=new JLabel();
			Label31.setText("加解密属性：");
		    box31=new JComboBox();
			box31.addItem("请选择加解密属性");
			box31.setSelectedIndex(0);

			JLabel Label32=new JLabel();
			Label32.setText("	加解密算法：");
			box32=new JComboBox();
			box32.addItem("请选择加解密算法");
			box32.addItem("保序加密算法");
			box32.addItem("算术加密算法");
			box32.setSelectedIndex(0);

			jpanel31.add(Label31);
			jpanel31.add(box31);
			jpanel31.add(Label32);
			jpanel31.add(box32);
			*/
				JLabel Label42=new JLabel();
				Label42.setText("选择文件路径：");
				text42=new JTextField(70);
				text42.setEditable(false);

				jpanel42.add(Label42);
				jpanel42.add(text42);

				JLabel Label43=new JLabel();
				Label43.setText("选择文件信息：");
				text43=new JTextField(70);
				text43.setEditable(false);

				jpanel43.add(Label43);
				jpanel43.add(text43);

				JLabel Label44=new JLabel();
				Label44.setText("保序加密属性:   ");
				box44=new JComboBox();
				box44.addItem("请选择保序加密属性");
				box44.setSelectedIndex(0);



				jpanel44.setLayout(new BorderLayout(0, 0));
				jpanel44.add(Label44,BorderLayout.WEST);
				jpanel44.add(box44,BorderLayout.CENTER);

				JLabel Label45=new JLabel();
				Label45.setText("保序运算关键字: ");

				text45=new  JTextField(20);
				jpanel45.setLayout(new BorderLayout(0, 0));
				jpanel45.add(Label45,BorderLayout.WEST);
				jpanel45.add(text45,BorderLayout.CENTER);

				JLabel Label451=new JLabel();
				Label451.setText("下边界:         ");
				text451=new  JTextField(20);

				JLabel Label452=new JLabel();
				Label452.setText("上边界:         ");

				text452=new  JTextField(20);

				JPanel jpanel4500=new JPanel();
				jpanel4500.setLayout(new BorderLayout(0, 0));
				jpanel4500.add(Label451,BorderLayout.WEST);
				jpanel4500.add(text451,BorderLayout.CENTER);

				JPanel jpanel4501=new JPanel();
				jpanel4501.setLayout(new BorderLayout(0, 0));
				jpanel4501.add(Label452,BorderLayout.WEST);
				jpanel4501.add(text452,BorderLayout.CENTER);

				jpanel451.setLayout(new BorderLayout(0, 0));
				jpanel451.add(jpanel4500,BorderLayout.NORTH);
				jpanel451.add(jpanel4501,BorderLayout.SOUTH);


				JLabel Label46=new JLabel();
				Label46.setText("保序信息 :");
				//Label46.setFont(new java.awt.Font("宋体",   0,   13));
				texta46=new JTextArea(12,36);
				texta46.setEditable(false);
				JScrollPane scrollPane41 = new JScrollPane(texta46);
				scrollPane41.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
				scrollPane41.setAutoscrolls(true);
				scrollPane41.setWheelScrollingEnabled(true);
				scrollPane41.scrollRectToVisible(getBounds());
				scrollPane41.setName("scrollPane41");
				scrollPane41.setPreferredSize(new Dimension(250,150));

				jpanel46.setLayout(new BorderLayout(0, 0));
				jpanel46.add(Label46,BorderLayout.NORTH);
				jpanel46.add(scrollPane41,BorderLayout.SOUTH);



				JPanel jpanel4lr0=new JPanel();
				jpanel4lr0.setLayout(new BorderLayout(0, 0));
				jpanel4lr0.add(jpanel44,BorderLayout.NORTH);
				jpanel4lr0.add(jpanel45,BorderLayout.SOUTH);

				JPanel jpanel4lr1=new JPanel();
				jpanel4lr1.setLayout(new BorderLayout(0, 0));
				jpanel4lr1.add(jpanel451,BorderLayout.NORTH);
				jpanel4lr1.add(jpanel46,BorderLayout.SOUTH);

				jpanel4left.setLayout(new BorderLayout(0, 0));
				jpanel4left.add(jpanel4lr0,BorderLayout.NORTH);
				jpanel4left.add(jpanel4lr1,BorderLayout.SOUTH);




				JLabel Label47=new JLabel();
				Label47.setText("   算术加密属性: ");
				box47=new JComboBox();
				box47.addItem("请选择算术加密属性");
				box47.setSelectedIndex(0);


				jpanel47.setLayout(new BorderLayout(0, 0));

				jpanel47.add(Label47,BorderLayout.WEST);
				jpanel47.add(box47,BorderLayout.CENTER);



				JLabel Label48=new JLabel();
				Label48.setText("   运算方法:     ");
				box48=new JComboBox();
				box48.addItem("加法");
				box48.addItem("减法");
				box48.addItem("乘法");
				box48.addItem("除法");

				jpanel48.setLayout(new BorderLayout(0, 0));
				jpanel48.add(Label48,BorderLayout.WEST);
				jpanel48.add(box48,BorderLayout.CENTER);


				JLabel Label49=new JLabel();
				Label49.setText("   记录选择:     ");
				box49=new JComboBox();
				box49.addItem("全部记录");
				box49.addItem("单行记录");
				box49.addItem("保序条件记录");

				jpanel49.setLayout(new BorderLayout(0, 0));
				jpanel49.add(Label49,BorderLayout.WEST);
				jpanel49.add(box49,BorderLayout.CENTER);



				JLabel Label410=new JLabel();
				Label410.setText("   算术关键字:  ");
				text410=new  JTextField(11);



				text4101=new  JTextField(8);
				text4101.setEditable(false);
				JLabel Label4101=new JLabel();
				Label4101.setText("行 ");
				JPanel jpanel4100=new JPanel();
				jpanel4100.setLayout(new BorderLayout(0, 0));
				jpanel4100.add(Label410,BorderLayout.WEST);
				jpanel4100.add(text410,BorderLayout.EAST);

				JPanel jpanel4101=new JPanel();
				jpanel4101.setLayout(new BorderLayout(0, 0));
				jpanel4101.add(text4101,BorderLayout.WEST);
				jpanel4101.add(Label4101,BorderLayout.EAST);


				jpanel410.setLayout(new BorderLayout(0, 0));
				jpanel410.add(jpanel4100,BorderLayout.WEST);
				jpanel410.add(jpanel4101,BorderLayout.EAST);





				JLabel Label411=new JLabel();
				Label411.setText("算术信息:");
				texta411=new JTextArea(12,36);
				texta411.setEditable(false);
				JScrollPane scrollPane411 = new JScrollPane(texta411);
				scrollPane411.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
				scrollPane411.setAutoscrolls(true);
				scrollPane411.setWheelScrollingEnabled(true);
				scrollPane411.scrollRectToVisible(getBounds());
				scrollPane411.setName("scrollPane411");
				scrollPane411.setPreferredSize(new Dimension(250,150));

				jpanel411.setLayout(new BorderLayout(0, 0));
				jpanel411.add(Label411,BorderLayout.NORTH);
				jpanel411.add(scrollPane411,BorderLayout.SOUTH);


				JPanel jpanel4lr2=new JPanel();
				jpanel4lr2.setLayout(new BorderLayout(0, 0));
				jpanel4lr2.add(jpanel47,BorderLayout.NORTH);
				jpanel4lr2.add(jpanel48,BorderLayout.SOUTH);

				JPanel jpanel4lr3=new JPanel();
				jpanel4lr3.setLayout(new BorderLayout(0, 0));
				jpanel4lr3.add(jpanel49,BorderLayout.NORTH);
				jpanel4lr3.add(jpanel410,BorderLayout.SOUTH);

				JPanel jpanel4lr4=new JPanel();
				jpanel4lr4.setLayout(new BorderLayout(0, 0));
				jpanel4lr4.add(jpanel4lr2,BorderLayout.NORTH);
				jpanel4lr4.add(jpanel4lr3,BorderLayout.SOUTH);


				JLabel jptmp=new JLabel("   ");
				JPanel jpanel4lr5=new JPanel();
				jpanel4lr5.setLayout(new BorderLayout(0, 0));
				jpanel4lr5.add(jptmp,BorderLayout.WEST);
				jpanel4lr5.add(jpanel411,BorderLayout.CENTER);


				jpanel4right.setLayout(new BorderLayout(0, 0));
				jpanel4right.add(jpanel4lr4,BorderLayout.NORTH);
				jpanel4right.add(jpanel4lr5,BorderLayout.SOUTH);



				//jpanel4lr.add(jpanel4left);
				//jpanel4lr.add(jpanel4right);

				jpanel4lr.add(jpanel4left,BorderLayout.WEST);
				jpanel4lr.add(jpanel4right,BorderLayout.EAST);

				JButton button41=new JButton();
				button41.setText("保序关键字加密");
				JButton button42=new JButton();
				button42.setText("保序范围加密");
				JButton button43=new JButton();
				button43.setText("关系运算");
				JButton button44=new JButton();
				button44.setText("算术关键字加密");
				JButton button45=new JButton();
				button45.setText("算术运算");
				JButton button46=new JButton();
				button46.setText("清空");



				jpanel4bt.add(button41);
				jpanel4bt.add(button42);
				jpanel4bt.add(button43);
				jpanel4bt.add(button44);
				jpanel4bt.add(button45);
				jpanel4bt.add(button46);





				panel.add(jpanel40);
				panel.add(jpanel41);
				panel.add(jpanel42);
				panel.add(jpanel43);

				//panel.add(jpanel44);
				//panel.add(jpanel45);
				// panel.add(jpanel451);
				// panel.add(jpanel46);
				panel.add(jpanel4lr);
				panel.add(jpanel4bt);



				box49.addActionListener(new ActionListener(){ //记录选择
					public void actionPerformed(ActionEvent e){
						int porperty=box49.getSelectedIndex();
						if(porperty==1){
							text4101.setText("");
							text4101.setEditable(true);
						}
						else{
							text4101.setText("");
							text4101.setEditable(false);
						}
					}
				});




				button41.addActionListener(new ActionListener(){//OPEART关键字加密
					@Override
					public void actionPerformed(ActionEvent e) {



						String key=text45.getText().toString().trim();//关键字
						if(GlobalState.loginmodel==2){  //离线登录模式

							if(key.equals("") || key==null){
								JOptionPane.showMessageDialog(mainPanel, "请输入保序关键字");
								return;
							}

							try { //数字，excel表格自动将int转化为double，所以这里也要转化
								double doubvalue=Double.valueOf(key);
								key=""+doubvalue;
								key=head0.substring(key.length())+key;
							}
							catch(Exception ex){
								ex.printStackTrace();
							}

							try {
								OPEART op=new OPEART(opeart_k);
								String encvalue=op.encrypt(key);

								//写入excel表格中
								FileOutputStream fos = new FileOutputStream("D:\\"+user_name+"\\OPEART_ENC.xls");
								HSSFWorkbook wb2 = new HSSFWorkbook();
								HSSFSheet s = wb2.createSheet(); //表
								wb2.setSheetName(0, "first sheet"); //设置第一个表名为 first sheet
								HSSFRow row2 = s.createRow(0); //第一行
								HSSFCell cell2 = row2.createCell(0);//第一列
								cell2.setCellValue(encvalue);
								wb2.write(fos);
								fos.close();
								//System.out.println(encvalue);

								//System.out.println(baoxushuxing);
								texta46.setText(encvalue);
								JOptionPane.showMessageDialog(new JLabel(),"保序关键字加密成功,加密关键字保存在 D:\\OPEART_ENC.xls中");
							}
							catch(Exception ex) {
								ex.printStackTrace();
							}



						}
						else{//在线登录模式

							int baoxuProperty=0;//加密属性
							baoxuProperty= box44.getSelectedIndex();
							if(baoxuProperty==0 || key.equals("") || key==null){

								JOptionPane.showMessageDialog(mainPanel, "请选择保序加密属性和输入保序关键字");
								return;
							}
							String encinfo=texta46.getText().toString().trim();
							if(encinfo.contains(box44.getSelectedItem().toString())){
								JOptionPane.showMessageDialog(mainPanel, "该属性已经选择了，请选择其他属性");
								return;
							}



							try { //数字，excel表格自动将int转化为double，所以这里也要转化
								double doubvalue=Double.valueOf(key);
								key=""+doubvalue;
								key=head0.substring(key.length())+key;
							}
							catch(Exception ex){
								ex.printStackTrace();
							}
							try {


								String filepath=text42.getText();
								OPEART op=null;
								if(filepath.startsWith("/"+username)){//用户本人
									op=new OPEART(opeart_k);
								}
								else{//其他用户
									String othername=filepath;
									othername=othername.substring(othername.indexOf("/")+1);
									othername=othername.substring(0,othername.indexOf("/"));
									System.out.println("othername  "+othername);

									Cert cert=new Cert();
									String res=cert.getCert1(othername,user_name);
									if(res.equals("")){

										JOptionPane.showMessageDialog(mainPanel, "你没有获得 "+othername+"的证书");
										return ;
									}
									String keyarr[]=res.split("&");
									String opeartkey=keyarr[5];
									long value=Long.parseLong(opeartkey);
									op=new OPEART(value);
								}




								String encvalue=op.encrypt(key);

								//写入excel表格中
								FileOutputStream fos = new FileOutputStream("D:\\"+user_name+"\\OPEART_ENC.xls");
								HSSFWorkbook wb2 = new HSSFWorkbook();
								HSSFSheet s = wb2.createSheet(); //表
								wb2.setSheetName(0, "first sheet"); //设置第一个表名为 first sheet
								HSSFRow row2 = s.createRow(0); //第一行
								HSSFCell cell2 = row2.createCell(0);//第一列
								cell2.setCellValue(encvalue);
								wb2.write(fos);
								fos.close();
								//System.out.println(encvalue);
								String baoxushuxing=box44.getSelectedItem().toString();
								//System.out.println(baoxushuxing);
								texta46.append(baoxushuxing+";"+1+";"+encvalue+"\n");  //1 是保序关键字，2是双边界，3是下边界，4是上边界
								JOptionPane.showMessageDialog(new JLabel(),"保序关键字加密成功,加密关键字保存在D:\\"+user_name+"\\OPEART_ENC.xls,可以继续选择保序属性进行加密");
							}
							catch(Exception ex) {
								ex.printStackTrace();
							}
						}

					}
				});
				button42.addActionListener(new ActionListener(){//OPEART边界字加密
					@Override
					public void actionPerformed(ActionEvent e) {

						String low=text451.getText().toString().trim();//下边界
						String up=text452.getText();//上边界


						if(GlobalState.loginmodel==2){
							if(((low.equals("") || low==null) && (up.equals("") || up==null))){
								JOptionPane.showMessageDialog(mainPanel, "请输入保序边界值");
								return;
							}

							int intboudn=0;
							if(!low.equals("") && !up.equals(""))//双边界
								intboudn=3;
							else if(!low.equals("")) //有下边界
								intboudn=2;
							else if(!up.equals(""))  //上边界
								intboudn=1;
							if(intboudn>0)	 {
								if(intboudn==3){ //双边界
									try { //数字，excel表格自动将int转化为double，所以这里也要转化
										double doubvalue=Double.valueOf(low);
										low=""+doubvalue;
										low=head0.substring(low.length())+low;
									}
									catch(Exception ex){
										ex.printStackTrace();
									}
									try {
										double doubvalue=Double.valueOf(up);
										up=""+doubvalue;
										up=head0.substring(up.length())+up;
									}
									catch(Exception ex){
										ex.printStackTrace();
									}
									try {

										int tag=low.compareTo(up);
										if(tag>=0){
											JOptionPane.showMessageDialog(mainPanel, "下边界的值大于等于上边界的值，请重新输入");
											return;
										}

										OPEART op=new OPEART(opeart_k);
										String enclow=op.encrypt(low);
										String encup=op.encrypt(up);
										//写入excel表格中
										FileOutputStream fos = new FileOutputStream("D:\\"+user_name+"\\OPEART_ENC.xls");
										HSSFWorkbook wb2 = new HSSFWorkbook();
										HSSFSheet s = wb2.createSheet(); //表
										wb2.setSheetName(0, "first sheet"); //设置第一个表名为 first sheet
										HSSFRow row2 = s.createRow(0); //第一行
										HSSFCell cell2 = row2.createCell(0);//第一列
										cell2.setCellValue(enclow);
										cell2 = row2.createCell(1);//第二列
										cell2.setCellValue(encup);
										wb2.write(fos);
										fos.close();

										texta46.setText(enclow+","+encup);
										JOptionPane.showMessageDialog(new JLabel(),"保序范围加密成功,加密边界保存在 D:\\"+user_name+"\\OPEART_ENC.xls");


									}
									catch(Exception ex){
										ex.printStackTrace();
									}
								}
								else if(intboudn==2){//下边界
									try { //数字，excel表格自动将int转化为double，所以这里也要转化
										double doubvalue=Double.valueOf(low);
										low=""+doubvalue;
										low=head0.substring(low.length())+low;
									}
									catch(Exception ex){
										ex.printStackTrace();
									}
									try {
										OPEART op=new OPEART(opeart_k);
										String enclow=op.encrypt(low);
										//写入excel表格中
										FileOutputStream fos = new FileOutputStream("D:\\"+user_name+"\\OPEART_ENC.xls");
										HSSFWorkbook wb2 = new HSSFWorkbook();
										HSSFSheet s = wb2.createSheet(); //表
										wb2.setSheetName(0, "first sheet"); //设置第一个表名为 first sheet
										HSSFRow row2 = s.createRow(0); //第一行
										HSSFCell cell2 = row2.createCell(0);//第一列
										cell2.setCellValue(enclow);
										wb2.write(fos);
										fos.close();

										texta46.setText(enclow);
										JOptionPane.showMessageDialog(new JLabel(),"保序范围加密成功,加密边界保存在 D:\\"+user_name+"\\OPEART_ENC.xls");
									}
									catch(Exception ex){
										ex.printStackTrace(); }
								}
								else if(intboudn==1){//上边界
									try {
										double doubvalue=Double.valueOf(up);
										up=""+doubvalue;
										up=head0.substring(up.length())+up;
									}
									catch(Exception ex){
										ex.printStackTrace(); }
									try {
										OPEART op=new OPEART(opeart_k);
										String encup=op.encrypt(up);
										//写入excel表格中
										FileOutputStream fos = new FileOutputStream("D:\\"+user_name+"\\OPEART_ENC.xls");
										HSSFWorkbook wb2 = new HSSFWorkbook();
										HSSFSheet s = wb2.createSheet(); //表
										wb2.setSheetName(0, "first sheet"); //设置第一个表名为 first sheet
										HSSFRow row2 = s.createRow(0); //第一行
										HSSFCell cell2 = row2.createCell(1);//第二列
										cell2.setCellValue(encup);
										wb2.write(fos);
										fos.close();

										texta46.setText(encup);
										JOptionPane.showMessageDialog(new JLabel(),"保序范围加密成功,加密边界保存在D:\\"+user_name+"\\OPEART_ENC.xls");

									}
									catch(Exception ex){
										ex.printStackTrace();
									}
								}
							}


						}
						else {//在线登录


							int baoxuProperty=0;//加密属性
							baoxuProperty= box44.getSelectedIndex();
							if(baoxuProperty==0 || ((low.equals("") || low==null) && (up.equals("") || up==null))){

								JOptionPane.showMessageDialog(mainPanel, "请选择保序加密属性和输入保序边界值");
								return;
							}
							String encinfo=texta46.getText().toString().trim();
							if(encinfo.contains(box44.getSelectedItem().toString())){
								JOptionPane.showMessageDialog(mainPanel, "该属性已经选择了，请选择其他属性");
								return;
							}





							int intboudn=0;
							if(!low.equals("") && !up.equals(""))//双边界
								intboudn=3;
							else if(!low.equals("")) //有下边界
								intboudn=2;
							else if(!up.equals(""))  //上边界
								intboudn=1;
							if(intboudn>0)	 {

								String filepath=text42.getText();
								OPEART op=null;
								if(filepath.startsWith("/"+username)){//用户本人
									op=new OPEART(opeart_k);
								}
								else{//其他用户
									String othername=filepath;
									othername=othername.substring(othername.indexOf("/")+1);
									othername=othername.substring(0,othername.indexOf("/"));
									System.out.println("othername  "+othername);

									Cert cert=new Cert();
									String res=cert.getCert1(othername,user_name);
									if(res.equals("")){

										JOptionPane.showMessageDialog(mainPanel, "你没有获得 "+othername+"的证书");
										return ;
									}
									String keyarr[]=res.split("&");
									String opeartkey=keyarr[5];
									long value=Long.parseLong(opeartkey);
									op=new OPEART(value);
								}



								if(intboudn==3){ //双边界
									try { //数字，excel表格自动将int转化为double，所以这里也要转化
										double doubvalue=Double.valueOf(low);
										low=""+doubvalue;
										low=head0.substring(low.length())+low;
									}
									catch(Exception ex){
										ex.printStackTrace();
									}
									try {
										double doubvalue=Double.valueOf(up);
										up=""+doubvalue;
										up=head0.substring(up.length())+up;
									}
									catch(Exception ex){
										ex.printStackTrace();
									}
									try {
										int tag=low.compareTo(up);
										if(tag>=0){
											JOptionPane.showMessageDialog(mainPanel, "下边界的值大于等于上边界的值，请重新输入");
											return;
										}

										// OPEART op=new OPEART(opeart_k);



										String enclow=op.encrypt(low);
										String encup=op.encrypt(up);
										//写入excel表格中
										FileOutputStream fos = new FileOutputStream("D:\\"+user_name+"\\OPEART_ENC.xls");
										HSSFWorkbook wb2 = new HSSFWorkbook();
										HSSFSheet s = wb2.createSheet(); //表
										wb2.setSheetName(0, "first sheet"); //设置第一个表名为 first sheet
										HSSFRow row2 = s.createRow(0); //第一行
										HSSFCell cell2 = row2.createCell(0);//第一列
										cell2.setCellValue(enclow);
										cell2 = row2.createCell(1);//第二列
										cell2.setCellValue(encup);
										wb2.write(fos);
										fos.close();

										String baoxushuxing=box44.getSelectedItem().toString();
										//System.out.println(baoxushuxing);
										texta46.append(baoxushuxing+";"+2+";"+enclow+","+encup+"\n");
										JOptionPane.showMessageDialog(new JLabel(),"保序范围加密成功,加密边界保存在D:\\"+user_name+"\\OPEART_ENC.xls，可以继续选择保序属性进行加密");


									}
									catch(Exception ex){
										ex.printStackTrace();
									}
								}
								else if(intboudn==2){//下边界
									try { //数字，excel表格自动将int转化为double，所以这里也要转化
										double doubvalue=Double.valueOf(low);
										low=""+doubvalue;
										low=head0.substring(low.length())+low;
									}
									catch(Exception ex){
										ex.printStackTrace();
									}
									try {
										//OPEART op=new OPEART(opeart_k);
										String enclow=op.encrypt(low);
										//写入excel表格中
										FileOutputStream fos = new FileOutputStream("D:\\"+user_name+"\\OPEART_ENC.xls");
										HSSFWorkbook wb2 = new HSSFWorkbook();
										HSSFSheet s = wb2.createSheet(); //表
										wb2.setSheetName(0, "first sheet"); //设置第一个表名为 first sheet
										HSSFRow row2 = s.createRow(0); //第一行
										HSSFCell cell2 = row2.createCell(0);//第一列
										cell2.setCellValue(enclow);
										wb2.write(fos);
										fos.close();

										String baoxushuxing=box44.getSelectedItem().toString();
										//System.out.println(baoxushuxing);
										texta46.append(baoxushuxing+";"+3+";"+enclow+"\n");
										JOptionPane.showMessageDialog(new JLabel(),"保序范围加密成功,加密边界保存在D:\\"+user_name+"\\OPEART_ENC.xls，可以继续选择保序属性进行加密");


									}
									catch(Exception ex){
										ex.printStackTrace(); }
								}
								else if(intboudn==1){//上边界
									try {
										double doubvalue=Double.valueOf(up);
										up=""+doubvalue;
										up=head0.substring(up.length())+up;
									}
									catch(Exception ex){
										ex.printStackTrace(); }
									try {
										// OPEART op=new OPEART(opeart_k);
										String encup=op.encrypt(up);
										//写入excel表格中
										FileOutputStream fos = new FileOutputStream("D:\\"+user_name+"\\OPEART_ENC.xls");
										HSSFWorkbook wb2 = new HSSFWorkbook();
										HSSFSheet s = wb2.createSheet(); //表
										wb2.setSheetName(0, "first sheet"); //设置第一个表名为 first sheet
										HSSFRow row2 = s.createRow(0); //第一行
										HSSFCell cell2 = row2.createCell(1);//第二列
										cell2.setCellValue(encup);
										wb2.write(fos);
										fos.close();
										String baoxushuxing=box44.getSelectedItem().toString();
										//System.out.println(baoxushuxing);
										texta46.append(baoxushuxing+";"+4+";"+encup+"\n");
										JOptionPane.showMessageDialog(new JLabel(),"保序范围加密成功,加密边界保存在D:\\"+user_name+"\\OPEART_ENC.xls，可以继续选择保序属性进行加密");

									}
									catch(Exception ex){
										ex.printStackTrace();  }
								}
								//JOptionPane.showMessageDialog(new JLabel(),"OPEART加密"+"边界加密成功,加密结果保存在：\nD:\\OPEART_ENC.xls");
							}
							else {
								JOptionPane.showMessageDialog(new JLabel(),"保序边界加密"+"请输入边界");
							}
						}
					}
				});

				button43.addActionListener(new ActionListener(){//OPEART运算
					@Override
					public void actionPerformed(ActionEvent e) {

						if(GlobalState.loginmodel==2){
							JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持关系运算");
							return;
						}


						String encinfo=texta46.getText().toString().trim();
						if(encinfo==null || encinfo.equals("")){
							JOptionPane.showMessageDialog(new JLabel(),"请先进行保序关键字或者范围的加密");
							return;
						}
						System.out.println("encinfo  "+encinfo);
						String filename=text42.getText().toString().trim();
					/*
					String splitarr[]=encinfo.split("\n");

					System.out.println("splitarr  "+splitarr.length);
					for(int i=0;i<splitarr.length;i++){
						System.out.println(splitarr[i]);
					}
					*/

						try {

							String fileuser=filename.substring(filename.indexOf("/")+1);
							fileuser=fileuser.substring(0,fileuser.indexOf("/"));
							//String filename=filepath.substring(filepath.lastIndexOf("/")+1);
							HttpClient client = new DefaultHttpClient();
							List<NameValuePair> formparams = new ArrayList<NameValuePair>();
							formparams.add(new BasicNameValuePair("username", username));	//用户名
							formparams.add(new BasicNameValuePair("filepath", filename));//文件路径
							formparams.add(new BasicNameValuePair("encinfo", encinfo));//
							formparams.add(new BasicNameValuePair("fileuser", fileuser));//


							UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");//设置编码格式
							HttpPost httppost = new HttpPost(GlobalState.server + "/baoxuyunsuan");//发起请求的网址,即   "http://202.117.10.253:8080/qcloud/login";
							httppost.setEntity(entity);
							HttpContext localContext = new BasicHttpContext();
							HttpResponse response = client.execute(httppost, localContext);
							int statusCode = response.getStatusLine().getStatusCode();
							if (statusCode != 200) {
								JOptionPane.showMessageDialog(mainPanel,"服务器出错，请稍后再试");
								response.getEntity().getContent().close();
								return ;
							}

							Header[] header = response.getHeaders("num");
							if (header == null || header.length == 0) {
								JOptionPane.showMessageDialog(mainPanel,"关系运算失败");
								response.getEntity().getContent().close();
								return ;
							}
							else{//linjiancai
								String num= header[0].getValue().toString();
								if(fileuser.equals(username))
									JOptionPane.showMessageDialog(mainPanel,"关系运算成功，共查询到"+num+" 条记录.\n结果保存在根目录下的 Relation_search_result.xls 下，请下载解密");
								else
									JOptionPane.showMessageDialog(mainPanel,"关系运算成功，共查询到"+num+" 条记录.\n结果保存在根目录下的 Relation_search_result__"+fileuser+"__.xls 下，请下载解密");
								texta46.setText("");
								updateTree10();

							}
							response.getEntity().getContent().close();

						} catch (IOException ex) {

							JOptionPane.showMessageDialog(mainPanel,"IO错误，请检查网络连接是否正确");
							ex.printStackTrace();
							return ;
						}


					}
				});


				button44.addActionListener(new ActionListener(){//算术关键字加密
					@Override
					public void actionPerformed(ActionEvent e) {


						String key=text410.getText().toString().trim();//算术关键字
						double keyvalue=0;	//关键字

						if(GlobalState.loginmodel==2){//离线登录模式

							if(key.equals("") || key==null){
								JOptionPane.showMessageDialog(mainPanel, "请输入算术关键字");
								return;
							}
							try { //数字，
								keyvalue=Double.parseDouble(key);
							}
							catch(Exception ex){
								ex.printStackTrace();
								JOptionPane.showMessageDialog(mainPanel, "请输入数值型的算术关键字");
								return;
							}

							try{


								System.out.println("cescmc_n  "+cescmc_n);
								System.out.println("cescmc_k  "+cescmc_k);
								CESCMC cescmc=new CESCMC(cescmc_n,cescmc_k);


								double sn=keyvalue;
								double[][]en_sn=cescmc.encrypt(sn);			//被操作数加密
								String en_str="";
								for(int i=0;i<cescmc_n;i++){
									for(int k=0;k<cescmc_n;k++){
										en_str=en_str+en_sn[i][k]+",";
									}
									en_str=en_str+";";
								}
								String info="";
								info=en_str;
								texta411.setText(info);//属性#运算方法#关键字加密#记录条件#记录条件详情
								JOptionPane.showMessageDialog(new JLabel(),"算术关键字加密成功");
							}
							catch(Exception ex) {
								ex.printStackTrace();
							}
						}
						else{

							int suanshuProperty=0;//加密属性
							suanshuProperty= box47.getSelectedIndex();
							int suanshumethod=0;//运算方法
							suanshumethod=box48.getSelectedIndex();
							int suanshutype=0;//记录条件
							suanshutype=box49.getSelectedIndex();
							//double keyvalue=0;	//关键字
							int localline=0;//选择条件中第几行
							String baoxuinfo=texta46.getText().toString().trim();//

							if(suanshuProperty==0 || key.equals("") || key==null){

								JOptionPane.showMessageDialog(mainPanel, "请选择算术加密属性和输入算术关键字");
								return;
							}

							try { //数字，
								keyvalue=Double.parseDouble(key);
							}
							catch(Exception ex){
								ex.printStackTrace();
								JOptionPane.showMessageDialog(mainPanel, "请输入数值型的算术关键字");
								return;
							}

							if(suanshutype==1){//单行
								try { //数字，
									String localtmp=text4101.getText().toString().trim();//行
									if(localtmp.equals("") || key==null){
										JOptionPane.showMessageDialog(mainPanel, "请输入行值");
										return;
									}else{
										localline=Integer.parseInt(localtmp);
										if(localline> excelLine){
											JOptionPane.showMessageDialog(mainPanel, "超过云端文件的最大行数  "+excelLine+" 行");
											return;
										}
										else if(localline<=0){
											JOptionPane.showMessageDialog(mainPanel, "请输入正整数的行值");
											return;
										}
									}

								}
								catch(Exception ex){
									ex.printStackTrace();
									JOptionPane.showMessageDialog(mainPanel, "请输入数值型的行值");
									return;
								}


							}
							else if(suanshutype==2){//保序条件选择
								String localtmp=texta46.getText().toString().trim();//
								if(localtmp.equals("") || key==null){
									JOptionPane.showMessageDialog(mainPanel, "请先进行保序关键字或范围的运算");
									return;
								}
							}


							try{


								//System.out.println("cescmc_n  "+cescmc_n);
								//System.out.println("cescmc_k  "+cescmc_k);
								CESCMC cescmc=null;

								String filepath=text42.getText();

								String en_str="";
								if(filepath.startsWith("/"+username)){//用户本人
									cescmc=new CESCMC(cescmc_n,cescmc_k);
									double sn=keyvalue;
									double[][]en_sn=cescmc.encrypt(sn);			//被操作数加密

									for(int i=0;i<cescmc_n;i++){
										for(int k=0;k<cescmc_n;k++){
											en_str=en_str+en_sn[i][k]+",";
										}
										en_str=en_str+";";
									}

								}
								else{//其他用户
									String othername=filepath;
									othername=othername.substring(othername.indexOf("/")+1);
									othername=othername.substring(0,othername.indexOf("/"));
									System.out.println("othername  "+othername);

									Cert cert=new Cert();
									String res=cert.getCert1(othername,user_name);
									if(res.equals("")){

										JOptionPane.showMessageDialog(mainPanel, "你没有获得 "+othername+"的证书");
										return ;
									}
									String keyarr[]=res.split("&");
									String cescmc_k_str=keyarr[6];
									String cescmc_n_str=keyarr[7];

									int cescmc_k_int=Integer.parseInt(cescmc_k_str);
									int cescmc_n_int=Integer.parseInt(cescmc_n_str);

									cescmc=new CESCMC(cescmc_n_int,cescmc_k_int);

									double sn=keyvalue;
									double[][]en_sn=cescmc.encrypt(sn);			//被操作数加密

									for(int i=0;i<cescmc_n_int;i++){
										for(int k=0;k<cescmc_n_int;k++){
											en_str=en_str+en_sn[i][k]+",";
										}
										en_str=en_str+";";
									}

								}





								String info="";

								String suanshushuxing=box47.getSelectedItem().toString();
								info=suanshushuxing;//属性
								info=info+"#"+suanshumethod;//运算方法
								info=info+"#"+en_str;//关键字加密
								info=info+"#"+suanshutype;//记录条件

								if(suanshutype==0){//全部记录
									info=info+"#"+0;//只是为了格式一样
								}
								else if(suanshutype==1){//全部记录
									info=info+"#"+localline;//第几行
								}
								else{//保序条件选择
									info=info+"#"+baoxuinfo;//第几行
								}

								texta411.setText(info);//属性#运算方法#关键字加密#记录条件#记录条件详情
								JOptionPane.showMessageDialog(new JLabel(),"算术关键字加密成功");
							}
							catch(Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				});


				button45.addActionListener(new ActionListener(){//算术运算
					@Override
					public void actionPerformed(ActionEvent e) {

						if(GlobalState.loginmodel==2){
							JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持算术运算");
							return;
						}

						String encinfo=texta411.getText().toString().trim();
						if(encinfo==null || encinfo.equals("")){
							JOptionPane.showMessageDialog(new JLabel(),"请先进行算术关键字的加密");
							return;
						}
						//System.out.println("encinfo  "+encinfo);
						String filepath=text42.getText().toString().trim();


						try {
							HttpClient client = new DefaultHttpClient();
							List<NameValuePair> formparams = new ArrayList<NameValuePair>();
							formparams.add(new BasicNameValuePair("username", username));	//用户名
							formparams.add(new BasicNameValuePair("filepath", filepath));//文件路径
							formparams.add(new BasicNameValuePair("encinfo", encinfo));//
							UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");//设置编码格式
							HttpPost httppost = new HttpPost(GlobalState.server + "/suanshuyunsuan");//发起请求的网址,即   "http://202.117.10.253:8080/qcloud/login";
							httppost.setEntity(entity);
							HttpContext localContext = new BasicHttpContext();
							HttpResponse response = client.execute(httppost, localContext);
							int statusCode = response.getStatusLine().getStatusCode();
							if (statusCode != 200) {
								JOptionPane.showMessageDialog(mainPanel,"服务器出错，请稍后再试");
								response.getEntity().getContent().close();
								return ;
							}

							Header[] header = response.getHeaders("num");
							if (header == null || header.length == 0) {
								JOptionPane.showMessageDialog(mainPanel,"算术运算失败");
								response.getEntity().getContent().close();
								return ;
							}
							else{//linjiancai
								String num= header[0].getValue().toString();
								if(filepath.startsWith("/"+username)){//用户本人
									JOptionPane.showMessageDialog(mainPanel,"算术运算成功，共运算 "+num+" 条记录.\n结果保存在原文件中");
									texta411.setText("");
									updateTree10();
								}
								else {//不是用户本人
									JOptionPane.showMessageDialog(mainPanel,"算术运算成功，共运算 "+num+" 条记录.\n结果保存在原文件中，请检索后下载解密");
									texta411.setText("");

								}

							}
							response.getEntity().getContent().close();

						} catch (IOException ex) {

							JOptionPane.showMessageDialog(mainPanel,"IO错误，请检查网络连接是否正确");
							ex.printStackTrace();
							return ;
						}


					}
				});



				button46.addActionListener(new ActionListener(){//清空
					@Override
					public void actionPerformed(ActionEvent e) {
						text42.setText("");//Excel文件加解密	文件路径  云端
						text43.setText("");//Excel文件加解密	文件基本信息，条数，属性  云端

						box44.removeAllItems();		//Excel文件加解密	云端	保序属性
						box44.addItem("请选择保序加密属性");
						box44.setSelectedIndex(0);
						text45.setText("");//保序关键字
						text451.setText("");//下边界
						text452.setText("");	//上边界
						texta46.setText("");//Excel文件加解密	保序提示

						box47.removeAllItems();;//Excel文件加解密	云端	算术属性
						box47.addItem("请选择算术加密属性");
						box47.setSelectedIndex(0);
						box48.setSelectedIndex(0);//加减乘除
						box49.setSelectedIndex(0);//条件选择
						text410.setText("");		//算术关键字
						text4101.setText("");	//行
						texta411.setText("");//Excel文件加解密	算术提示
						text4101.setEditable(false);
					}
				});

				break;
//			------------------------证书授权--------------------------------------
			case 4:
				JPanel panel51=new JPanel();
				JPanel panel52=new JPanel();
				JPanel panel53=new JPanel();

				JPanel panel54=new JPanel();
				panel.setLayout(new FlowLayout(FlowLayout.LEFT));

				JLabel label51=new JLabel();
				label51.setText("被授权用户：        ");
				final JTextField text51=new JTextField(26);

				panel51.add(label51);
				panel51.add(text51);

				JLabel label501=new JLabel();
				label501.setText("   授权模式：");
				//单选按钮
				ButtonGroup bg = new ButtonGroup();//创建按钮组
				final JRadioButton jrb1 = new JRadioButton("读");
				final JRadioButton jrb2 = new JRadioButton("读写");
				bg.add(jrb1);
				bg.add(jrb2);
				panel51.add(label501);
				panel51.add(jrb1);
				panel51.add(jrb2);
				jrb1.setSelected(true);

				panel53.setLayout(new FlowLayout(FlowLayout.LEFT));
				JLabel label52=new JLabel();
				label52.setText("已在云端保存的证书：");
				final JComboBox jcbb51=new JComboBox();
				jcbb51.addItem("已在云端保存的授权用户");
				panel53.add(label52);
				panel53.add(jcbb51);

				//panel54.setLayout(new FlowLayout(FlowLayout.LEFT));
				//panel54.add(panel51,BorderLayout.NORTH);
				//panel54.add(panel53,BorderLayout.SOUTH);

				JButton button51=new JButton();
				button51.setText("生成证书");
				JButton button52=new JButton();
				button52.setText("删除证书");
				JButton button53=new JButton();
				button53.setText("上传证书");
				JButton button54=new JButton();
				button54.setText("清空");
				JButton button55=new JButton();
				button55.setText("打开目录");

				panel52.add(button51);
				panel52.add(button53);
				panel52.add(button52);
				panel52.add(button54);
				panel52.add(button55);
				//panel53.add(button53);

				//panel.setLayout(new FlowLayout(FlowLayout.LEFT));

				panel.add(panel51);
				panel.add(panel53);
				//panel.add(panel54);
				panel.add(panel52);
				//panel.add(scrollPaneCheckBox);
				//panel.add(panel53);





			/*
			jrb1.addActionListener(new ActionListener(){ //记录选择
					public void actionPerformed(ActionEvent e){
					  System.out.println(jrb1.getText().toString());
					}
			    });
			jrb2.addActionListener(new ActionListener(){ //记录选择
				public void actionPerformed(ActionEvent e){
					 System.out.println(jrb2.getText().toString());
				}
		    });
			*/


				if(GlobalState.loginmodel==1){//在线的登陆才可以查询
					try{
						List<NameValuePair> formparams = new ArrayList<NameValuePair>();
						formparams.add(new BasicNameValuePair("username", username));
						HttpClient client = new DefaultHttpClient();
						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
						HttpPost httppost = new HttpPost( GlobalState.getServer() + "/getcert");
						httppost.setEntity(entity);
						httppost.setHeader("Cookie", LoginTask.cookies);
						HttpResponse response = client.execute(httppost);
						int statusCode = response.getStatusLine().getStatusCode();
						if (statusCode != 200) {
							JOptionPane.showMessageDialog(mainPanel, "服务器错误，请稍后再试");
							response.getEntity().getContent().close();

						}
						else {
							Header[] header = response.getHeaders("certall");
							if (header == null || header.length == 0) {
								JOptionPane.showMessageDialog(mainPanel,"服务器错误，请稍后再试");
								response.getEntity().getContent().close();

							}
							else{//linjiancai
								String value= header[0].getValue().toString();
								if(value.equals("")){

									response.getEntity().getContent().close();

								}else{
									//System.out.println("CERTALL  "+value);
									String certsplit[]=value.split(";");
									for(int i=0;i<certsplit.length;i++){
										String tmp=certsplit[i];
										tmp=tmp.substring(tmp.indexOf("_")+1);
										tmp=tmp.substring(0,tmp.indexOf("_"));
										jcbb51.addItem(tmp);
									}


									response.getEntity().getContent().close();

								}
							}
						}
					}
					catch (Exception e1) {
						JOptionPane.showMessageDialog(mainPanel, "请检查网络连接是否正常");
						e1.printStackTrace();

					}


				}

				button51.addActionListener(new ActionListener(){//生成证书
					@Override
					public void actionPerformed(ActionEvent e) {
						String toname=text51.getText().trim();
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
						cert.produceCert(user_name,toname);
						if(jrb1.isSelected()){
							cert.produceCert0(user_name,toname,0);//0表示读的权限
							//System.out.println(jrb1.getText());
						}
						if(jrb2.isSelected()){
							cert.produceCert0(user_name,toname,1);//1表示读写的权限
							//System.out.println(jrb2.getText());

						}

						cert.produceCert1(user_name,toname,aes_key,ras_key_1,ras_key_2,opeart_k,cescmc_k,cescmc_n);

						JOptionPane.showMessageDialog(new JLabel(), "证书已生成为\nD:\\qingyunclient\\test\\client\\cert_"+toname+"_"+user_name+"_0.txt\n"+"D:\\qingyunclient\\test\\client\\cert_"+toname+"_"+user_name+"_1.txt");
						//JOptionPane.showMessageDialog(new JLabel(), "证书已生成，为D:\\qingyunclient\\test\\client\\cert_"+text51.getText()+".txt");
					}
				});
				button52.addActionListener(new ActionListener(){//删除证书
					@Override
					public void actionPerformed(ActionEvent e) {
						if(GlobalState.loginmodel==2){
							JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持删除证书");
							return;
						}

					/*//用文本框的模式
					String toname=text51.getText().trim();
					if(toname==null || toname.equals(""))
					{
						JOptionPane.showMessageDialog(new JLabel(), "用户名不能为空");
						return ;
					}
					*/
						int jcbb51selectid=jcbb51.getSelectedIndex();
						if(jcbb51selectid==0){
							JOptionPane.showMessageDialog(new JLabel(), "请先选择在云端保存的证书");
							return ;
						}
						String toname=jcbb51.getSelectedItem().toString();
						String parentpath="/"+username+"/cert";
						String certfilename="cert_"+toname+"_"+user_name+"_0.txt";
						String certfilepath=parentpath+"/"+certfilename;
						try{
							List<NameValuePair> formparams = new ArrayList<NameValuePair>();

							formparams.add(new BasicNameValuePair("username", username));
							formparams.add(new BasicNameValuePair("userrole", "1"));
							formparams.add(new BasicNameValuePair("filepath", certfilepath));
							formparams.add(new BasicNameValuePair("filename", certfilename));
							HttpClient client = new DefaultHttpClient();
							UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
							HttpPost httppost = new HttpPost( GlobalState.getServer() + "/deletecert");
							httppost.setEntity(entity);
							httppost.setHeader("Cookie", LoginTask.cookies);
							HttpResponse response = client.execute(httppost);
							int statusCode = response.getStatusLine().getStatusCode();
							if (statusCode != 200) {
								JOptionPane.showMessageDialog(mainPanel, "服务器错误，请稍后再试");
								response.getEntity().getContent().close();

							}
							else {
								Header[] header = response.getHeaders("certexist");
								if (header == null || header.length == 0) {
									JOptionPane.showMessageDialog(mainPanel,"服务器错误，请稍后再试");
									response.getEntity().getContent().close();

								}
								else{//linjiancai
									String value= header[0].getValue().toString();
									if(value.equals("0")){
										JOptionPane.showMessageDialog(mainPanel,"证书不存在，或者已经被删除");
										response.getEntity().getContent().close();



										jcbb51.removeItemAt(jcbb51selectid);







									}else{
										JOptionPane.showMessageDialog(mainPanel, "删除证书 "+ certfilename +"成功");
										response.getEntity().getContent().close();
										jcbb51.removeItemAt(jcbb51selectid);
										updateTree10();
									}
								}




							}
						}
						catch (Exception e1) {
							JOptionPane.showMessageDialog(mainPanel, "请检查网络连接是否正常");
							e1.printStackTrace();

						}

					/*
					Cert cert=new Cert();
					cert.getCert(toname);
					cert.getCert0(user_name,toname);
					cert.getCert1(user_name,toname);
					*/
					}
				});
				button53.addActionListener(new ActionListener(){//上传证书
					@Override
					public void actionPerformed(ActionEvent e) {
						if(GlobalState.loginmodel==2){
							JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持上传证书");
							return;
						}
						String toname=text51.getText().trim();
						if(toname==null || toname.equals(""))
						{
							JOptionPane.showMessageDialog(new JLabel(), "用户名不能为空");
							return ;
						}
						String certfilepath="D:\\qingyunclient\\test\\client\\cert_"+toname+"_"+user_name+"_0.txt";
						String certfilename="cert_"+toname+"_"+user_name+"_0.txt";
						File file=new File(certfilepath);
						if(!file.exists()){
							JOptionPane.showMessageDialog(new JLabel(), "请先生成证书");
							return ;
						}

						GlobalState.state = "upload";
						UploadTask upload=  new UploadTask(user_name,jTree1,mainPanel);
						try {


							//upload.upload4(parentname,parentpath,encResultsplit);//上传加密文件,有限制1m大小
							upload.uploadcert(certfilepath,certfilename);//上传证书，没有限制大小
							updateTree10();//刷新云目录
							//updateTree20();//刷新本地目录

							int itemcount=jcbb51.getItemCount();
							boolean isexist=false;
							for(int i=1;i<itemcount;i++){
								String value=jcbb51.getItemAt(i).toString();
								if(value.equals(toname)){
									System.out.println("已经有了，不更新下拉框");
									isexist=true;
									break;
								}
							}
							if(!isexist){
								jcbb51.addItem(toname);
							}

						}
						catch (Exception e1) {
							e1.printStackTrace();
						}


						GlobalState.state = "logined";


					}
				});

				button54.addActionListener(new ActionListener(){//清空
					@Override
					public void actionPerformed(ActionEvent e) {
						text51.setText("");
						jcbb51.setSelectedIndex(0);
					}
				});
				button55.addActionListener(new ActionListener(){//打开目录
					@Override
					public void actionPerformed(ActionEvent e) {


						try {

							Desktop desktop = Desktop.getDesktop();
							File file = new File("D:\\qingyunclient\\test\\client");
							desktop.open(file);
						/*
						String[] cmd = new String[5];
						String url = "D:\\qingyunclient\\test\\client";
						cmd[0] = "cmd";
						cmd[1] = "/c";
						cmd[2] = "start";
						cmd[3] = " ";
						cmd[4] = url;
						Runtime.getRuntime().exec(cmd);
				*/
						}catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});

				break;
//			------------------------系统参数设置--------------------------------------
			case 5:
				//JPanel panel61=new JPanel();
				//JPanel panel62=new JPanel();
				JPanel panel63=new JPanel();
				JPanel panel64=new JPanel();
				JPanel panel65=new JPanel();
				//JPanel panel66=new JPanel();
				//JPanel panel67=new JPanel();
				//JPanel panel68=new JPanel();
				JPanel panel69=new JPanel();
				JPanel panel610=new JPanel();
				JPanel panel611=new JPanel();
				JPanel panel612=new JPanel();

				JPanel panel613=new JPanel();

				JPanel panel614=new JPanel();

				panel.setLayout(new FlowLayout(FlowLayout.LEFT));

			/*
			//panel.setLayout(new BorderLayout(0, 0));
			panel63.setLayout(new BorderLayout(0, 0));
			panel64.setLayout(new BorderLayout(0, 0));
			panel65.setLayout(new BorderLayout(0, 0));
			panel69.setLayout(new BorderLayout(0, 0));
			panel610.setLayout(new BorderLayout(0, 0));
			panel611.setLayout(new BorderLayout(0, 0));
			panel612.setLayout(new BorderLayout(0, 0));
			*/
			/*
			JLabel label61=new JLabel();
			label61.setText("用户名");
			final JTextField text61=new JTextField(20);

			JLabel label62=new JLabel();
			label62.setText("登陆密钥");
			final JTextField text62=new JTextField(20);
		*/
				JLabel label63=new JLabel();
				label63.setText("对称加密AES密钥：              ");
				final JTextField text63=new JTextField(20);

				JLabel label64=new JLabel();
				label64.setText("非对称加密RSA密钥(两个大素数)：");
				final JTextField text64=new JTextField(20);
				final JTextField text641=new JTextField(20);

				JLabel label65=new JLabel();
				label65.setText("Bloom_Filter检索精度：         ");
				final JTextField text65=new JTextField(20);

			/*
			JLabel label66=new JLabel();
			label66.setText("CESVMC_da");
			final JTextField text66=new JTextField(20);

			JLabel label67=new JLabel();
			label67.setText("CESVMC_dm");
			final JTextField text67=new JTextField(20);

			JLabel label68=new JLabel();
			label68.setText("CESVMC_dk");
			final JTextField text68=new JTextField(20);
		*/

				JLabel label69=new JLabel();
				label69.setText("保序加密算法OPEART_k：         ");
				final JTextField text69=new JTextField(20);

				JLabel label610=new JLabel();
				label610.setText("矩阵算术运算CESCMC_k：         ");
				final JTextField text610=new JTextField(20);

				JLabel label611=new JLabel();
				label611.setText("矩阵算术运算CESCMC_n：         ");
				final JTextField text611=new JTextField(20);

				JLabel label613=new JLabel();
				label613.setText("上传文件最大值(10M)：          ");
				final JTextField text613=new JTextField(20);
				JLabel label6130=new JLabel();
				label6130.setText("M");

				JLabel label614=new JLabel();
				label614.setText("下载文件最大值(20M)：          ");
				final JTextField text614=new JTextField(20);
				JLabel label6140=new JLabel();
				label6140.setText("M                 ");

				//panel61.add(label61);
				//panel61.add(text61);
				//panel62.add(label62);
				//panel62.add(text62);
				panel63.add(label63,BorderLayout.WEST);
				panel63.add(text63,BorderLayout.EAST);
				panel64.add(label64,BorderLayout.WEST);
				panel64.add(text64,BorderLayout.CENTER);
				panel64.add(text641,BorderLayout.EAST);
				panel65.add(label65,BorderLayout.WEST);
				panel65.add(text65,BorderLayout.EAST);
				//panel66.add(label66);
				//panel66.add(text66);
				//panel67.add(label67);
				//panel67.add(text67);
				//panel68.add(label68);
				//panel68.add(text68);
				panel69.add(label69,BorderLayout.WEST);
				panel69.add(text69,BorderLayout.EAST);
				panel610.add(label610,BorderLayout.WEST);
				panel610.add(text610,BorderLayout.EAST);
				panel611.add(label611,BorderLayout.WEST);
				panel611.add(text611,BorderLayout.EAST);

				panel613.add(label613);
				panel613.add(text613);
				panel613.add(label6130);

				panel614.add(label614);
				panel614.add(text614);
				panel614.add(label6140);


				JButton button61=new JButton();
				button61.setText("保存");
				JButton button62=new JButton();
				button62.setText("清空");
				JButton button63=new JButton();
				button63.setText("重置");

				panel612.add(button61);
				panel612.add(button62);
				panel612.add(button63);


				panel.add(panel63);
				panel.add(panel64);
				panel.add(panel65);
				panel.add(panel69);
				panel.add(panel610);
				panel.add(panel611);
				panel.add(panel613);
				panel.add(panel614);
				panel.add(panel612);



			/*
			panel613.add(panel63);
			panel613.add(panel64);
			panel613.add(panel65);
			panel613.add(panel69);
			panel613.add(panel610);
			panel613.add(panel611);

			panel.add(panel613);
			panel.add(panel612);
			*/

				String[] res4=readFile(user_name,pwdd);
			/*
			text61.setText(res4[0]);//name:user1
			text62.setText(res4[1]);//key
			text63.setText(res4[2]);//AES_key
			text64.setText(res4[3]);//R_key1
			text641.setText(res4[4]);//R_key2
			text65.setText(res4[5]);//ju
			text66.setText(res4[6]);//APSE_da
			text67.setText(res4[7]);//APSE_dm
			text68.setText(res4[8]);//APSE_key
			text69.setText(res4[9]);//OPEART_key
			text610.setText(res4[10]);//CESCMC_key
			text611.setText(res4[11]);//CESCMC_n
			*/

				text63.setText(res4[0]);//AES_key
				text64.setText(res4[1]);//R_key1
				text641.setText(res4[2]);//R_key2
				text65.setText(res4[3]);//ju
				text69.setText(res4[4]);//OPEART_key
				text610.setText(res4[5]);//CESCMC_key
				text611.setText(res4[6]);//CESCMC_n

				text613.setText(res4[7]);//CESCMC_n
				text614.setText(res4[8]);//CESCMC_n



				//保存
				button61.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						//pwdd=text62.getText();

						try {
							int intuploadlimit=Integer.parseInt(text613.getText().trim());
							int intdownloadlimit=Integer.parseInt(text614.getText().trim());
							if(intuploadlimit>10 || intuploadlimit<=0){
								JOptionPane.showMessageDialog(new JLabel(), "上传文件大小最大为10M");
								return;
							}
							if(intdownloadlimit>20 || intdownloadlimit<=0){
								JOptionPane.showMessageDialog(new JLabel(), "下载文件大小最大为20M");
								return;

							}
							System.out.println("可以保存修改配置");
							aes=new AES(pwdd);
							//String name="name:"+text61.getText()+"\r\n";
							//String key="key:"+text62.getText()+"\r\n";
							String aes_keylo="AES_key:"+text63.getText()+"\r\n";
							String rsa_key1="RSA_key1:"+text64.getText()+"\r\n";
							String rsa_key2="RSA_key2:"+text641.getText()+"\r\n";
							String ppositive="Bloom_Filter_ju:"+text65.getText()+"\r\n";
							String stropeart_k="OPEART_key:"+text69.getText().trim()+"\r\n";
							String strcescmc_k="CESCMC_key:"+text610.getText().trim()+"\r\n";
							String strcescmc_n="CESCMC_n:"+text611.getText().trim()+"\r\n";
							String struploadlimit="uploadlimit:"+text613.getText().trim()+"\r\n";
							String strdownloadlimit="downloadlimit:"+text614.getText().trim()+"\r\n";

							String ss=aes_keylo+rsa_key1+rsa_key2+ppositive+stropeart_k+strcescmc_k+strcescmc_n+struploadlimit+strdownloadlimit;
							//String ss=name2+key2+aes_key+rsa_key1+rsa_key2+ppositive+strda+strdm+straspe_k+stropeart_k+strcescmc_k+strcescmc_n;
							//String ss=aes_key+rsa_key1+rsa_key2+ppositive+stropeart_k+strcescmc_k+strcescmc_n;
							System.out.println(ss);
							String res3=aes.encrypt(ss);


							writeFile(res3,user_name);

							aes_key=text63.getText();
							aes=new AES(aes_key);
							ras_key_1=text64.getText();
							ras_key_2=text641.getText();

							//System.out.println(aes_key+", "+rsa_key1+", "+rsa_key2+", "+positive);
							rsa=new unpadded_RSA(text64.getText(),text641.getText());
							Bloom_Filter_ju=Double.parseDouble(text65.getText());
							kk=(int)(Math.log(Double.parseDouble(text65.getText()))/Math.log(0.5))+1;   //hash个数
							nn=50;                                         //kw个数
							mm=744;                                        //BF位数



							long intopeart_k=Long.parseLong(text69.getText().trim());
							int intcescmc_k=Integer.parseInt(text610.getText().trim());
							int intcescmc_n=Integer.parseInt(text611.getText().trim());



							opeart_k=intopeart_k;
							cescmc_k=intcescmc_k;
							cescmc_n=intcescmc_n;

							uploadlimit=Integer.parseInt(text613.getText().trim());
							downloadlimit=Integer.parseInt(text614.getText().trim());
							GlobalState.basicsize=uploadlimit*1024*1024;
							GlobalState.downbasicsize=downloadlimit*1024*1024;
							System.out.println("GlobalState.basicsize  "+GlobalState.basicsize);
							System.out.println("GlobalState.downloadlimit  "+GlobalState.downbasicsize);
							JOptionPane.showMessageDialog(new JLabel(), "参数修改成功");

						}


						catch(Exception ex){
							JOptionPane.showMessageDialog(new JLabel(), "请输入正确的整数参数");

						}
					}
				});
				//清空
				button62.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						//text61.setText("");
						//text62.setText("");
						text63.setText("");
						text64.setText("");
						text641.setText("");
						text65.setText("");
						//text66.setText("");
						//text67.setText("");
						//text68.setText("");
						text69.setText("");
						text610.setText("");
						text611.setText("");
						text613.setText("");
						text614.setText("");
					}
				});
				//重置
				button63.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						String[] res4=readFile(user_name,pwdd);
						text63.setText(res4[0]);//AES_key
						text64.setText(res4[1]);//R_key1
						text641.setText(res4[2]);//R_key2
						text65.setText(res4[3]);//ju
						text69.setText(res4[4]);//OPEART_key
						text610.setText(res4[5]);//CESCMC_key
						text611.setText(res4[6]);//CESCMC_n
						text613.setText(res4[7]);//uploadlimint
						text614.setText(res4[8]);//downloadlimint

						String key=res4[0];

						aes_key=key;
						ras_key_1=res4[1];
						ras_key_2=res4[2];

						aes=new AES(key);	  //System.out.println(res[2]+"----------------2");
						rsa=new unpadded_RSA(res4[1],res4[2]);//RSA的两个密钥p和q
						Bloom_Filter_ju=Double.parseDouble(res4[3]);	//BF检索中用户允许的最大误差
						opeart_k=Long.parseLong(res4[4]);
						cescmc_k=Integer.parseInt(res4[5]);
						cescmc_n=Integer.parseInt(res4[6]);
						//影响Bloom Filter的误检率p的三个参数分别是哈希函数的个数k，位数组的大小m和集合的元素个数n(一个文件名中关键字的最多个数),
						kk=(int)(Math.log(Bloom_Filter_ju)/Math.log(0.5))+1;   //hash个数

						nn=50;                                         //kw个数
						mm=744;

						uploadlimit=Integer.parseInt(res4[7]);
						downloadlimit=Integer.parseInt(res4[8]);
						GlobalState.basicsize=uploadlimit*1024*1024;
						GlobalState.downbasicsize=downloadlimit*1024*1024;
						System.out.println("GlobalState.basicsize  "+GlobalState.basicsize);
						System.out.println("GlobalState.downloadlimit  "+GlobalState.downbasicsize);
					}

				});

				break;
			default:
		}
		return panel;
	}




	static Color colors[] = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN};
	static void add(JTabbedPane tabbedPane, String label, int mnemonic,JPanel panel) {
		int count = tabbedPane.getTabCount();
		//JPanel panel =new JPanel();
		panel.setForeground(colors[count]);
		//panel.setBackground(colors[count]);
		tabbedPane.addTab(label, panel);
		//tabbedPane.setMnemonicAt(count, mnemonic);
	}

	/*
	 * 获取云端目录，并将云端文件下载到本地
	 */
//	public void drawTree(DefaultMutableTreeNode node1){
//		ExpandTask expand;
//		 expand= new ExpandTask(mainPanel,node1, jTree1);
//		 expand.expand();
//		 for (int i = 0; i < node1.getChildCount(); i++) {
//			 DefaultMutableTreeNode node3= (DefaultMutableTreeNode)node1.getChildAt(i);
//			 FileBean fb= (FileBean)node3.getUserObject();
//			 if(fb!=null){
//				 File dir = new File("D:" + fb.getPath());
//				 if (fb.getFileType()=="FOLDER"){
//					 if(dir.exists()){
//		                   System.out.println(fb.getName()+"文件已经存在");
//		               }
//					 else{
//						 dir.mkdirs();//创建文件夹
//					 }
//					 getfilechildren1(node3);
//					 }
//				 else{
//					 DownloadTask download= new DownloadTask(node3, jTree2);
//					 download.download();
//					 }
//				 }
//			 }
//	}


	/*linjiancai 包含了 updateTree10  和  updateTree20 的功能
	 * 更新云端文件列表和本地文件列表，并显示
	 */
	void updateTree(){
		File dir = new File("D:" + "/" + username);
		DefaultMutableTreeNode node1=(DefaultMutableTreeNode)jTree1.getModel().getRoot();
		DefaultMutableTreeNode node2=(DefaultMutableTreeNode)jTree2.getModel().getRoot();
		FileBean filebean=(FileBean)node1.getUserObject();

		/*//原来
		deleteAllTree1(node1);
		deleteAllTree1(node2);
		getfilechildren1(node1);
		getfilechildren2(node2,dir);
		*/
		//linjiancai

		if(GlobalState.loginmodel==1){
			deleteAllTree10(node1);
			getfilechildren1(node1);
		}

		deleteAllTree20(node2);//linjiancai

		getfilechildren2(node2,dir,"D:" + "/" + username);//File dir = new File("D:" + "/" + username);

		jTree1.expandPath(new  TreePath(node1.getPath()));
		jTree2.expandPath(new  TreePath(node2.getPath()));
	}



	//linjiancai 更新云端
	public void updateTree10(){
		DefaultMutableTreeNode node1=(DefaultMutableTreeNode)jTree1.getModel().getRoot();
		deleteAllTree10(node1);
		getfilechildren1(node1);
		jTree1.expandPath(new  TreePath(node1.getPath()));

	}
	//linjiancai  更新本地的
	void updateTree20(){
		File dir = new File("D:" + "/" + username);
		DefaultMutableTreeNode node2=(DefaultMutableTreeNode)jTree2.getModel().getRoot();
		deleteAllTree20(node2);//linjiancai
		getfilechildren2(node2,dir,"D:" + "/" + username);//File dir = new File("D:" + "/" + username);
		jTree2.expandPath(new TreePath(node2.getPath()));
	}


	/*
	 * 创建云端目录，并将云端文件下载到本地
	 */

	public void drawTree1(String username) {
		//jTree1.removeAll();//能够实现网页版文件修改后的云端目录自动更新，可能是因为使用了递归调用的原因
		//jTree1.getModel().
		DefaultMutableTreeNode dmtnRoot = new DefaultMutableTreeNode(username, true);
		FileBean file=new FileBean(false, username, "/" + username);//根节点，设置了路径和名称和是否文件
		file.setFileType(FileType.HOME);//设置了图标类型   与 jTree1.setCellRenderer(new MyRenderer());匹配，自动获得图标
		dmtnRoot.setUserObject(file);//设置根节点的UserObject类型为FileBean   共有   路径和名称和是否文件 和 图标类型这些属性

		DefaultTreeModel dtm = new DefaultTreeModel(dmtnRoot);//创建树的数据模型，后面的子节的类型，包括UserObject都是FileBean的
		jTree1.setModel(dtm);
		dtm.setAsksAllowsChildren(true);
		GlobalState.state = "logined";
		GlobalState.time = new Date().getTime();
		jTree1.setCellRenderer(new MyRenderer());//描绘器，即图标的显示类型
		jTree1.putClientProperty("JTree.lineStyle", "Angled");		 //   jTree1.setEditable(true);
		jTree1.setShowsRootHandles(true);	 //设置JTree将显示根节点的控制图标
		MyJPopupListener1 mjpl1 = new MyJPopupListener1();
		jTree1.addMouseListener(mjpl1);//监听鼠标事件
		MyPopMenuActionListener1 mpmal1 = new MyPopMenuActionListener1();//创建菜单项动作事件监听器对象
		for (int i = 0; i < jMenuItem1.length; i++) {//添加右键菜单项的事件监听
			jMenuItem1[i].addActionListener(mpmal1);
		}
		//获取云端文件列表，并将文档下载到本地
		System.out.println("drawTree1");

		if(GlobalState.loginmodel==1){
			getfilechildren1(dmtnRoot);
		}



		jTree1.expandPath(new  TreePath(dmtnRoot.getPath()));


	}

	/*
	 * 获取云端文件列表，并将文档下载到本地
	 */
	private void getfilechildren1(DefaultMutableTreeNode node){
		//System.out.println("Expand");
		ExpandTask expand;
		expand= new ExpandTask(mainPanel,node, jTree1);//展开，从云端获取数据
		//expand.expand();//原来的
		expand.expand2();//linjiancai
		for (int i = 0; i < node.getChildCount(); i++) {
			DefaultMutableTreeNode node1= (DefaultMutableTreeNode)node.getChildAt(i);
			//node1= (DefaultMutableTreeNode)node.getChildAt(i);
			FileBean fb= (FileBean)node1.getUserObject();
			if(fb!=null){
				File dir = new File("D:" + fb.getPath());
				if (fb.getFileType()=="FOLDER"){
					if(dir.exists()){
						System.out.println(fb.getName()+"文件已经存在");
					}
					else{
						dir.mkdirs();//创建文件夹
					}
					getfilechildren1(node1);
				}
				else{

					//DownloadTask download= new DownloadTask(node1, jTree2);
					//download.download();
					 /*
					 Thread  thr=new Thread(new Runnable(){

				        	public void run(){ //继续计时，前面的  thr=new Thread(new Runnable() 已经停止了，如果不在这里写run，则不会再计时

				        		try{
				        			 DownloadTask download= new DownloadTask(node1, jTree2);
									 download.download();
				        		}
				        		catch(Exception e){
				        		e.printStackTrace();
				        		}
				        	}
				        }

				        );
				        thr.start();
					 */

				}
			}
		}
	}

	/*
	 * 仅更新云端文件列表，并显示
	 */
	void updateTree1(){
		DefaultMutableTreeNode node=(DefaultMutableTreeNode)jTree1.getModel().getRoot();
		deleteAllTree1(node);
		getfilechildrenlist1(node);
	}



	/*
	 * 仅获取云端文件列表
	 */
	private void getfilechildrenlist1(DefaultMutableTreeNode node){
		ExpandTask expand;
		expand= new ExpandTask(mainPanel,node, jTree1);
		expand.expand();
		for (int i = 0; i < node.getChildCount(); i++) {
			DefaultMutableTreeNode node1= (DefaultMutableTreeNode)node.getChildAt(i);
			FileBean fb= (FileBean)node1.getUserObject();
			if (fb.getFileType()=="FOLDER"){
				//getfilechildren1(node1)
				getfilechildrenlist1(node1);
			}
		}
	}


	/*
	 * 	删除云端目录
	 */
	void deleteAllTree1(DefaultMutableTreeNode node){
		if(node.getChildCount()>0){
			for (int i = 0; i < node.getChildCount(); i++) {
				DefaultMutableTreeNode node1= (DefaultMutableTreeNode)node.getChildAt(i);
				deleteAllTree1(node1);
				((DefaultTreeModel) jTree1.getModel()).removeNodeFromParent(node1);
				node1=null;
			}
		}
	}

	//linjiancai
	void deleteAllTree10(DefaultMutableTreeNode node){
		if(node.getChildCount()>0){
			System.out.println();
			while (node.getChildCount() > 0) {
				DefaultMutableTreeNode node1= (DefaultMutableTreeNode)node.getFirstChild();
				deleteAllTree10(node1);
				((DefaultTreeModel) jTree1.getModel()).removeNodeFromParent(node1);
				node1=null;
				//node.removeAllChildren();//删除node的所有子节点
			}
		}
	}
	//linjiancai
	void deleteAllTree20(DefaultMutableTreeNode node){
		//System.out.println(node.getChildCount());
		if(node.getChildCount()>0){
			//System.out.println();
			while (node.getChildCount() > 0) {
				DefaultMutableTreeNode node1= (DefaultMutableTreeNode)node.getFirstChild();
				deleteAllTree20(node1);
				((DefaultTreeModel) jTree2.getModel()).removeNodeFromParent(node1);
				node1=null;
				//node.removeAllChildren();//删除node的所有子节点
			}
		}
	}
	/*
	 * 	创建本地目录
	 */
	void drawTree2(){
		File dir = new File("D:" + "/" + username);
		if (!(dir.exists() || dir.mkdirs())) {
			System.out.println("本地目录不存在");
			return;
		}


		DefaultMutableTreeNode dmtnRoot2 = new DefaultMutableTreeNode(username, true);
		//FileBean file2=new FileBean(false, username, dir.toString());
		//FileBean file2=new FileBean(false, username, "/" + username);
		FileBean file2=new FileBean(false, username, "D:" +"/" + username);
		file2.setFileType(FileType.HOME);
		dmtnRoot2.setUserObject(file2);
		getfilechildren2(dmtnRoot2, dir,"D:" + "/" + username);

		//创建树的数据模型
		DefaultTreeModel dtm2 = new DefaultTreeModel(dmtnRoot2);
		jTree2.setModel(dtm2);
		jTree2.setCellRenderer(new MyRenderer());
		jTree2.putClientProperty("JTree.lineStyle", "Angled");
		jTree2.setShowsRootHandles(true);
		MyJPopupListener2 mjpl2 = new MyJPopupListener2(); //添加鼠标右键响应
		jTree2.addMouseListener(mjpl2);//树添加鼠标右键事件

       /*//原来的
       MyPopMenuActionListener2 mpmal2 = new MyPopMenuActionListener2();//创建菜单项动作事件监听器对象
       for (int i = 0; i < jMenuItem2.length; i++) {//添加右键菜单项的事件监听
           jMenuItem2[i].addActionListener(mpmal2);
       }*/
		//linjiancai
		MyPopMenuActionListener4 mpmal4 = new MyPopMenuActionListener4();//创建菜单项动作事件监听器对象  鼠标右键的子选项的响应事件
		for (int i = 0; i < jMenuItem2.length; i++) {//添加右键菜单项的事件监听
			jMenuItem2[i].addActionListener(mpmal4);//添加鼠标右键的子选项的响应事件
		}


       /*
       //为本地文件添加监听函数
       //ListenTask listen = new ListenTask(jTree2,(DefaultMutableTreeNode) ((DefaultTreeModel) jTree2.getModel()).getRoot(), dir.toString());
       FileBean fb=(FileBean)dmtnRoot2.getUserObject();
       System.out.println("fb path " + fb.getPath());
       listen = new ListenTask2(jTree2,(DefaultMutableTreeNode) ((DefaultTreeModel) jTree2.getModel()).getRoot(), fb.getPath());
       try {
           listen.addWatch();
       } catch (Exception ex) {
    	   ex.printStackTrace();
       }
       */

		jTree2.expandPath(new  TreePath(dmtnRoot2.getPath()));
	}
	/*
	 * 获取本地目录的孩子节点
	 */
	private void getfilechildren2(DefaultMutableTreeNode father, File fatherdir,String dir) {
		String[] d = fatherdir.list();
		System.out.println(d);
		for (String cp : d) {
        	/*
        	//换成/的形式比较好
            String childpath = fatherdir.toString() + "\\" + cp;//绝对路径
            File cf = new File(childpath);	//boolean isFile, String name, String path,double length
            FileBean childfile = new FileBean(cf.isFile(), childpath.substring(childpath.lastIndexOf("\\") + 1, childpath.length()), childpath,cf.length());
            childfile.setFileType(FileUtil.getFileType(childfile));

            //DefaultMutableTreeNode child = new DefaultMutableTreeNode(childfile, !cf.isFile());
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(childpath.substring(childpath.lastIndexOf("\\") + 1, childpath.length()), !cf.isFile());
            child.setUserObject(childfile);

            ((DefaultTreeModel) jTree2.getModel()).insertNodeInto(child, father, father.getChildCount());//linjiancai，这个没有问题
            if (cf.list() != null) {//cf是目录时，再递归调用本函数
                getfilechildren2(child, cf);
            }

           // father.add(child);//原来的，有点问题
              */
			//换成/的形式比较好
			String childpath = dir + "/" + cp;//绝对路径

			File cf = new File(childpath);	//boolean isFile, String name, String path,double length
			FileBean childfile = new FileBean(cf.isFile(), childpath.substring(childpath.lastIndexOf("/") + 1, childpath.length()), childpath,cf.length());
			childfile.setFileType(FileUtil.getFileType(childfile));

			//DefaultMutableTreeNode child = new DefaultMutableTreeNode(childfile, !cf.isFile());
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(childpath.substring(childpath.lastIndexOf("/") + 1, childpath.length()), !cf.isFile());
			child.setUserObject(childfile);
			((DefaultTreeModel) jTree2.getModel()).insertNodeInto(child, father, father.getChildCount());//linjiancai，这个没有问题
			if (cf.list() != null) {//cf是目录时，再递归调用本函数
				getfilechildren2(child, cf,childpath);
			}


		}
	}
	/*
	 * 	重绘本地目录
	 */
	void updateTree2(){
		DefaultMutableTreeNode node=(DefaultMutableTreeNode)jTree2.getModel().getRoot();
		File dir = new File("D:" + "/" + username);
		deleteAllTree2(node);
		getfilechildren2(node,dir,"D:" + "/" + username);
		//jTree2.repaint();
	}
	/*
	 * 	删除本地目录
	 */
	void deleteAllTree2(DefaultMutableTreeNode node){
		if(node.getChildCount()>0){
			for (int i = 0; i < node.getChildCount(); i++) {
				DefaultMutableTreeNode node1= (DefaultMutableTreeNode)node.getChildAt(i);
				deleteAllTree2(node1);
				((DefaultTreeModel) jTree2.getModel()).removeNodeFromParent(node1);
				node1=null;
			}
		}
	}


	class MyJPopupListener1 extends MouseAdapter {

		public MyJPopupListener1() {//构造器,初始化弹出式菜单
			for (int i = 0; i < jMenuItem1.length; i++) {
				jPopupMenu1.add(jMenuItem1[i]);//将菜单项添加到弹出式菜单中
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				Point point = e.getPoint();
				TreePath path = jTree1.getClosestPathForLocation(point.x, point.y);
				System.out.println("mousePressed"+path.toString());
				System.out.println("mousePressed"+point.x+" "+point.y);
				if (path != null) {
					jTree1.getSelectionModel().setSelectionPath(path);
					TreeNode node = (TreeNode) path.getLastPathComponent();
					tempNode = (DefaultMutableTreeNode) node;
					jPopupMenu1.show(jTree1, point.x, point.y);
				}
			}
			else if (e.getClickCount() == 1)
			{
				//单击
				System.out.println("云端单击");
				try{

					TreePath[] patharr =jTree1.getSelectionPaths();
					tempNodearr=null;
					if(patharr!=null){

						tempNodearr=new DefaultMutableTreeNode[patharr.length];
						System.out.println("patharr.length "+patharr.length);
						for(int i=0;i<patharr.length;i++)
							System.out.println(patharr[i].toString());
						if (patharr != null) {
							for(int i=0;i<patharr.length;i++){
								//jTree1.getSelectionModel().setSelectionPath(path);//设置树的选择节点
								//jTree1.getSelectionModel().setSelectionPath(patharr[i]);//设置树的选择节点
								TreeNode node = (TreeNode) patharr[i].getLastPathComponent();//获得被选择节点
								DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) node;//转化节点的类型
								tempNodearr[i]=tempNode;

							}
						}
					}
				}
				catch(Exception ex){
					ex.printStackTrace();
				}


				if(tempNodearr==null)
					return;

			} else if (e.getClickCount() == 2)//linjiancai
			{
				//双击，获取excel表格的属性和内容
				System.out.println("云端双击");

				try{

					TreePath[] patharr =jTree1.getSelectionPaths();
					tempNodearr=null;
					if(patharr!=null){
						tempNodearr=new DefaultMutableTreeNode[patharr.length];
						System.out.println("patharr.length "+patharr.length);
						for(int i=0;i<patharr.length;i++)
							System.out.println(patharr[i].toString());
						if (patharr != null) {
							for(int i=0;i<patharr.length;i++){
								//jTree1.getSelectionModel().setSelectionPath(path);//设置树的选择节点
								//jTree1.getSelectionModel().setSelectionPath(patharr[i]);//设置树的选择节点
								TreeNode node = (TreeNode) patharr[i].getLastPathComponent();//获得被选择节点
								DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) node;//转化节点的类型
								tempNodearr[i]=tempNode;

							}
						}
					}
				}
				catch(Exception ex){
					ex.printStackTrace();
				}


				if(tempNodearr==null)
					return;


			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {


	        		 /*//选择单个节点
	        		Point point = e.getPoint();//根据点的位置找最近的节点
	                TreePath path = jTree1.getClosestPathForLocation(point.x, point.y);


	                //System.out.println("mouseReleased"+path.toString());
	                //System.out.println("mouseReleased"+point.x+" "+point.y);
	                if (path != null) {
	                    jTree1.getSelectionModel().setSelectionPath(path);//设置树的选择节点
	                    TreeNode node = (TreeNode) path.getLastPathComponent();//获得被选择节点
	                    tempNode = (DefaultMutableTreeNode) node;//转化节点的类型
	                    jPopupMenu1.show(jTree1, point.x, point.y);//在jTree1 的范围内有右键行为，弹出jPopupMenu1对话框
	                }
	                */

	        		  /*//获取选取的节点  TreePath类管理着一个Object引用序列。当拥有一个树路径时，通常只需要知道其终端结点，该结点可以通过getLastPathComponent方法得到。
	        		  TreePath selectionPath = tree.getSelectionPath();
	        		  DefaultMutableTreeNode selectedNode1 =
	        		  (DefaultMutableTreeNode) selectionPath .getLastPathComponent();

	        		  或者通过下面方法可立即返回给定的结点。
	        		  DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
	        		                       .getLastSelectedPathComponent();
	        		  */

				System.out.println("云端释放");
				//linjiancai，选择多个节点
				try{
					Point point = e.getPoint();//根据点的位置找最近的节点
					TreePath[] patharr =jTree1.getSelectionPaths();
					tempNodearr=null;
					if(patharr!=null){
						tempNodearr=new DefaultMutableTreeNode[patharr.length];
						System.out.println("patharr.length "+patharr.length);
						for(int i=0;i<patharr.length;i++)
							System.out.println(patharr[i].toString());
						if (patharr != null) {
							for(int i=0;i<patharr.length;i++){
								//jTree1.getSelectionModel().setSelectionPath(path);//设置树的选择节点
								//jTree1.getSelectionModel().setSelectionPath(patharr[i]);//设置树的选择节点
								TreeNode node = (TreeNode) patharr[i].getLastPathComponent();//获得被选择节点
								DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) node;//转化节点的类型
								tempNodearr[i]=tempNode;
								System.out.println(tempNodearr[i].getPath());
								jPopupMenu1.show(jTree1, point.x, point.y);//在jTree1 的范围内有右键行为，弹出jPopupMenu1对话框
							}
						}
					}
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}




	class MyJLableMouseListen extends MouseAdapter {

		public MyJLableMouseListen() {//构造器,初始化弹出式菜单

		}

		@Override
		public void mouseEntered(MouseEvent e) {//鼠标进入
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		@Override
		public  void mouseExited(MouseEvent e) {//鼠标移除
			setCursor(Cursor.getDefaultCursor());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getSource().equals(Label100)){
				System.out.println("云端 close");
				DefaultMutableTreeNode root1=(DefaultMutableTreeNode)((DefaultTreeModel) jTree1.getModel()).getRoot();
				//TreeNode node = (TreeNode) jTree2.getModel().getRoot();
				//expandAll(jTree2, new TreePath(node), false);
				expandAll(jTree1, new TreePath(root1.getPath()), false);
				jTree1.expandPath(new  TreePath(root1.getPath()));
			}
			else if(e.getSource().equals(Label101)){
				System.out.println("云端 open");
				DefaultMutableTreeNode root1=(DefaultMutableTreeNode)((DefaultTreeModel) jTree1.getModel()).getRoot();
				//TreeNode node = (TreeNode) jTree2.getModel().getRoot();
				//expandAll(jTree2, new TreePath(node), false);
				expandAll(jTree1, new TreePath(root1.getPath()), true);
			}
			else if(e.getSource().equals(Label102)){
				System.out.println("本地close");
				DefaultMutableTreeNode root1=(DefaultMutableTreeNode)((DefaultTreeModel) jTree2.getModel()).getRoot();
				//TreeNode node = (TreeNode) jTree2.getModel().getRoot();
				//expandAll(jTree2, new TreePath(node), false);
				expandAll(jTree2, new TreePath(root1.getPath()), false);
				jTree2.expandPath(new  TreePath(root1.getPath()));
			}

			else if(e.getSource().equals(Label103)){
				System.out.println("本地open");
				DefaultMutableTreeNode root1=(DefaultMutableTreeNode)((DefaultTreeModel) jTree2.getModel()).getRoot();
				//TreeNode node = (TreeNode) jTree2.getModel().getRoot();
				//expandAll(jTree2, new TreePath(node), true);
				expandAll(jTree2, new TreePath(root1.getPath()), true);

			}
		}

		private void expandAll(JTree tree, TreePath parent, boolean expand) {
			TreeNode node = (TreeNode) parent.getLastPathComponent();

			if (node.getChildCount() > 0) {
				for (Enumeration e = node.children(); e.hasMoreElements();) {
					TreeNode n = (TreeNode) e.nextElement();
					TreePath path = parent.pathByAddingChild(n);
					expandAll(tree, path, expand);
				}
			}
			if (expand) {
				tree.expandPath(parent);
			} else {
				tree.collapsePath(parent);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {


			}
		}
	}

	class MyJPopupListener2 extends MouseAdapter {

		public MyJPopupListener2() {//构造器,初始化弹出式菜单
			for (int i = 0; i < jMenuItem2.length; i++) {
				jPopupMenu2.add(jMenuItem2[i]);//将菜单项添加到弹出式菜单中
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				System.out.println("本地MyJPopupListener2  mousePressed");
				Point point = e.getPoint();
				TreePath path = jTree2.getClosestPathForLocation(point.x, point.y);
				if (path != null) {
					jTree2.getSelectionModel().setSelectionPath(path);
					TreeNode node = (TreeNode) path.getLastPathComponent();
					tempNode = (DefaultMutableTreeNode) node;
					jPopupMenu2.show(jTree2, point.x, point.y);//在jTree1 的范围内有右键行为，弹出jPopupMenu1对话框
				}
			}
			else if (e.getClickCount() == 1)
			{
				//单击
				System.out.println("单击");
			} else if (e.getClickCount() == 2)//linjiancai
			{
				//双击
				System.out.println("双击");

				try{

					TreePath[] patharr =jTree2.getSelectionPaths();
					tempNodearrlocal=null;
					if(patharr!=null){
						tempNodearrlocal=new DefaultMutableTreeNode[patharr.length];
						System.out.println("patharr.length "+patharr.length);
						for(int i=0;i<patharr.length;i++)
							System.out.println(patharr[i].toString());
						if (patharr != null) {
							for(int i=0;i<patharr.length;i++){
								//jTree1.getSelectionModel().setSelectionPath(path);//设置树的选择节点
								//jTree1.getSelectionModel().setSelectionPath(patharr[i]);//设置树的选择节点
								TreeNode node = (TreeNode) patharr[i].getLastPathComponent();//获得被选择节点
								DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) node;//转化节点的类型
								tempNodearrlocal[i]=tempNode;

							}
						}
					}
				}
				catch(Exception ex){
					ex.printStackTrace();
				}

				Desktop desktop = Desktop.getDesktop();
				if(tempNodearrlocal==null)
					return;
				for(int i=0;i<tempNodearrlocal.length;i++){
					File file = new File(((FileBean) ((DefaultMutableTreeNode) tempNodearrlocal[i]).getUserObject()).getPath());
					System.out.println(((FileBean) ((DefaultMutableTreeNode) tempNodearrlocal[i]).getUserObject()).getPath());
					if (!file.exists()) {
						JOptionPane.showMessageDialog(mainPanel, "该文件已经被删除!");
						e.setSource(null);
						//return;
					}
					else{
						try {
							if(((FileBean) ((DefaultMutableTreeNode) tempNodearrlocal[i]).getUserObject()).isIsFile()){//是文件是才打开，不写这行则文件夹也打开
								//desktop.open(file);
								String[] cmd = new String[5];
								String url = ((FileBean) ((DefaultMutableTreeNode) tempNodearrlocal[i]).getUserObject()).getPath();
								cmd[0] = "cmd";
								cmd[1] = "/c";
								cmd[2] = "start";
								cmd[3] = " ";
								cmd[4] = url;
								Runtime.getRuntime().exec(cmd);
							}

						}
						catch (Exception ex) {
							JOptionPane.showMessageDialog(mainPanel, "请检查文件是否为可识别文件，或者是否被某应用程序占用,!");
							e.setSource(null);
							ex.printStackTrace();
							//return;
						}
					}
				}
				//
			}
		}



		public void mouseReleased(MouseEvent e) {//当鼠标释放时执行的动作
			if (e.isPopupTrigger()) {
				System.out.println("本地MyJPopupListener2  mouseReleased");
	        		 /*//选择单个节点
	        		Point point = e.getPoint();
	                TreePath path = jTree2.getClosestPathForLocation(point.x, point.y);
	                if (path != null) {
	                    jTree2.getSelectionModel().setSelectionPath(path);
	                    TreeNode node = (TreeNode) path.getLastPathComponent();
	                    tempNode = (DefaultMutableTreeNode) node;
	                    jPopupMenu2.show(jTree2, point.x, point.y);
	                }
	                */


				//linjiancai
				try{
					Point point = e.getPoint();//根据点的位置找最近的节点
					TreePath[] patharr =jTree2.getSelectionPaths();
					tempNodearrlocal=null;
					if(patharr!=null){
						tempNodearrlocal=new DefaultMutableTreeNode[patharr.length];
						System.out.println("patharr.length "+patharr.length);
						for(int i=0;i<patharr.length;i++)
							System.out.println(patharr[i].toString());
						if (patharr != null) {
							for(int i=0;i<patharr.length;i++){
								//jTree1.getSelectionModel().setSelectionPath(path);//设置树的选择节点
								//jTree1.getSelectionModel().setSelectionPath(patharr[i]);//设置树的选择节点
								TreeNode node = (TreeNode) patharr[i].getLastPathComponent();//获得被选择节点
								DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) node;//转化节点的类型
								tempNodearrlocal[i]=tempNode;
								System.out.println(tempNodearrlocal[i].getPath());
								jPopupMenu2.show(jTree2, point.x, point.y);//在jTree1 的范围内有右键行为，弹出jPopupMenu1对话框
							}
						}
					}
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}

	//用来作为菜单项动作事件监听器的内部类
	//云端目录弹出菜单监听器
	class MyPopMenuActionListener1 extends MouseAdapter implements ActionListener {

		public void actionPerformed(ActionEvent e) {	//"刷新","下载","删除"

			//原来的删除
	        	/*
	        	DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNode.getParent();//获得选择节点的父节点
	            if (e.getSource() == jMenuItem1[2]) {//删除
	                if (node == null) {
	                    JOptionPane.showMessageDialog( mainPanel,"根目录不能被删除!");
	                    return;
	                } else {
	                	//删除单个文件
	                	DeleteTask delete= new DeleteTask(mainPanel,tempNode, jTree1);
	                	delete.delete();

	                	//linjiancai 删除多个文件
	                	//DeleteTask delete= new DeleteTask(mainPanel,tempNodearr, jTree1);
	                	//delete.delete2();

	                	}
	                 System.out.println("delete cloud file");
	            }
	            */
			if (e.getSource() == jMenuItem1[6]) {//属性
				if(GlobalState.loginmodel==2){
					JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持属性查询");
					return;
				}

				if(tempNodearr==null){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个云端文件");
					return;
				}

				if(tempNodearr.length==0){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个云端文件");
					return;
				}
				if(tempNodearr.length>1){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个云端文件");
					return;
				}

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNodearr[0];

				FileBean fb=(FileBean)node.getUserObject();
				String path=fb.getPath();
				String name=fb.getName();
				boolean isfile=fb.isIsFile();



				double length=fb.getLength();

				String showMessage="位置："+path+"\n";
				if(isfile)
					showMessage=showMessage+"文件名："+name+"\n";
				else
					showMessage=showMessage+"文件夹名："+name+"\n";

				DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) node.getParent();
				if (node2 == null) {

					try{
						List<NameValuePair> formparams = new ArrayList<NameValuePair>();
						formparams.add(new BasicNameValuePair("username", username));
						HttpClient client = new DefaultHttpClient();
						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
						HttpPost httppost = new HttpPost( GlobalState.getServer() + "/getcontent");
						httppost.setEntity(entity);
						httppost.setHeader("Cookie", LoginTask.cookies);
						HttpResponse response = client.execute(httppost);
						int statusCode = response.getStatusLine().getStatusCode();
						if (statusCode != 200) {
							JOptionPane.showMessageDialog(mainPanel, "服务器错误，请稍后再试");
							response.getEntity().getContent().close();

						}
						else {
							Header[] header = response.getHeaders("content");
							if (header == null || header.length == 0) {
								JOptionPane.showMessageDialog(mainPanel,"服务器错误，请稍后再试");
								response.getEntity().getContent().close();

							}
							else{//linjiancai
								String value= header[0].getValue().toString();
								if(value.equals("")){

									response.getEntity().getContent().close();

								}else{
									//System.out.println("CERTALL  "+value);
									double doublevalue=Double.parseDouble(value);
									DecimalFormat df=new DecimalFormat("#.00");
									//value=df.format(doublevalue);
									doublevalue=doublevalue*1024*1024;
									if(doublevalue==0.0)
										showMessage=showMessage+"占用空间："+"0.00 B\n";
									else if(doublevalue>1024*1024*1024)
										showMessage=showMessage+"占用空间："+df.format(doublevalue/(1024*1024*1024))+" GB\n";
									else if(doublevalue>1024*1024)
										showMessage=showMessage+"占用空间："+df.format(doublevalue/(1024*1024))+" MB\n";
									else if(doublevalue>1024)
										showMessage=showMessage+"占用空间："+df.format(doublevalue/(1024))+" KB\n";
									else
										showMessage=showMessage+"占用空间："+df.format(doublevalue)+" B\n";

									//showMessage=showMessage+"占用空间："+value+" MB\n";
									//JOptionPane.showMessageDialog(mainPanel, "您一共使用了 "+value+"M的空间");
									response.getEntity().getContent().close();

								}
							}
						}
					}
					catch (Exception e1) {
						JOptionPane.showMessageDialog(mainPanel, "请检查网络连接是否正常");
						e1.printStackTrace();

					}

				}

				else{
					DecimalFormat df=new DecimalFormat("#.00");
					length=length*1024*1024;
					if(length==0.0)
						showMessage=showMessage+"占用空间："+"0.00 B\n";
					else if(length>1024*1024*1024)
						showMessage=showMessage+"占用空间："+df.format(length/(1024*1024*1024))+" GB\n";
					else if(length>1024*1024)
						showMessage=showMessage+"占用空间："+df.format(length/(1024*1024))+" MB\n";
					else if(length>1024)
						showMessage=showMessage+"占用空间："+df.format(length/(1024))+" KB\n";
					else
						showMessage=showMessage+"占用空间："+df.format(length)+" B\n";
					//showMessage=showMessage+"占用空间："+length+" MB\n";
				}



				JOptionPane.showMessageDialog( mainPanel,showMessage);






			}

			else if(e.getSource() == jMenuItem1[5]) {//文件重命名
				if(GlobalState.loginmodel==2){
					JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持云端文件重命名");
					return;
				}

				if(tempNodearr==null){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个云端文件");
					return;
				}

				if(tempNodearr.length==0){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个云端文件");
					return;
				}
				if(tempNodearr.length>1){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个云端文件");
					return;
				}

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNodearr[0];

				FileBean fb=(FileBean)node.getUserObject();
				String path=fb.getPath();
				String name=fb.getName();
				boolean isfile=fb.isIsFile();
				if(!fb.isIsFile()){
					JOptionPane.showMessageDialog( mainPanel,"您选择的是文件夹，请选择1个云端文件，暂不支持文件夹重命名");
					return;
				}
				DefaultMutableTreeNode parentNode=(DefaultMutableTreeNode)node.getParent();

				System.out.println("path  "+path+"  name  "+name);


				try {

					Rename rename=new Rename(1,node,parentNode,jTree1,name,path);//创建本地目录
					rename.rename();
					//updateTree10();

				}
				catch (Exception e1) {
					e1.printStackTrace();
				}


			}

			else if (e.getSource() == jMenuItem1[4]) {//excel文件选择


				if(GlobalState.loginmodel==2){
					JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持云端excel文件选择");
					return;
				}


				if(tempNodearr==null){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个云端Excel文件");
					return;
				}

				if(tempNodearr.length==0){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个云端Excel文件");
					return;
				}
				if(tempNodearr.length>1){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个云端Excel文件");
					return;
				}

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNodearr[0];
				FileBean fb=(FileBean)node.getUserObject();
				if(!fb.isIsFile()){
					JOptionPane.showMessageDialog( mainPanel,"您选择的是文件夹，请选择1个云端Excel文件");
					return;
				}

				String path=fb.getPath();
				String name=fb.getName();
				System.out.println("path  "+path+"  name  "+name);
				if(!(name.endsWith(".xls")|| name.endsWith(".xlsx"))){
					JOptionPane.showMessageDialog( mainPanel,"您选择的不是Excel文件，请选择1个云端Excel文件");
					return;
				}
				try {

					HttpClient client = new DefaultHttpClient();//HttpClient不支持流输出
					List<NameValuePair> formparams = new ArrayList<NameValuePair>();
					formparams.add(new BasicNameValuePair("username", LoginView.username));
					formparams.add(new BasicNameValuePair("userrole", "1"));
					formparams.add(new BasicNameValuePair("path",path)); //文件路径
					formparams.add(new BasicNameValuePair("name",name));//文件名
					formparams.add(new BasicNameValuePair("name",name));//文件名
					formparams.add(new BasicNameValuePair("fileuser",LoginView.username));//文件的所有者
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");//设置编码格式
					HttpPost httppost = new HttpPost(GlobalState.server+ "/getExcelInfo");//发起请求的网址,即   "http://202.117.10.253:8080/qcloud/login";
					httppost.setHeader("Cookie", LoginTask.cookies);
					httppost.setEntity(entity);
					HttpContext localContext = new BasicHttpContext();
					HttpResponse response = client.execute(httppost, localContext);
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == 200) {
						//response.getEntity().getContent().close();
						// JOptionPane.showMessageDialog(new JLabel(), "新建文件夹  "+ foldename+" 成功");




						Header[] header = response.getHeaders("info");
						if (header == null || header.length == 0) {
							JOptionPane.showMessageDialog(mainPanel,"获取Excel文件信息失败");
							//response.getEntity().getContent().close()


						}
						else{//linjiancai
							String value= header[0].getValue().toString();
							value=URLDecoder.decode(value,"UTF-8");
							//System.out.println("value "+value);
							if(value.equals("")|| value==null  || value.equals("null")){
								JOptionPane.showMessageDialog(mainPanel,"获取Excel文件信息失败");
							}else{

								String[] infoarr=value.split(";");
								excelLine=Integer.parseInt(infoarr[infoarr.length-1].substring(0,infoarr[infoarr.length-1].indexOf("行")));//行数
								System.out.println("excelLine "+excelLine);

								text42.setText(path);
								text43.setText(value);//Excel文件加解密	文件基本信息，条数，属性  云端

								box44.removeAllItems();		//Excel文件加解密	云端	保序属性
								box44.addItem("请选择保序加密属性");
								box44.setSelectedIndex(0);

								box47.removeAllItems();;//Excel文件加解密	云端	算术属性
								box47.addItem("请选择算术加密属性");
								box47.setSelectedIndex(0);


								for(int i=0;i<infoarr.length;i++){
									String tmp=infoarr[i];//1@#ID
									if(tmp.startsWith("1@#")){
										box44.addItem(""+i+":"+tmp.substring(3));
									}
									else if(tmp.startsWith("2@#")){
										box47.addItem(""+i+":"+tmp.substring(3));
									}
								}





								text45.setText("");//保序关键字
								text451.setText("");//下边界
								text452.setText("");	//上边界
								texta46.setText("");//Excel文件加解密	保序提示


								box48.setSelectedIndex(0);//加减乘除
								box49.setSelectedIndex(0);//条件选择
								text410.setText("");		//算术关键字
								text4101.setText("");	//行
								texta411.setText("");//Excel文件加解密	算术提示

								tabbedPane.setSelectedIndex(3);
							}
						}

					}
					else{
						System.out.println(response.getStatusLine().getStatusCode());
						JOptionPane.showMessageDialog(new JLabel(), "服务器出错，请稍后再试");
					}
					response.getEntity().getContent().close();


				} catch (Exception ex) {
					JOptionPane.showMessageDialog(new JLabel(), "请检查网络连接是否正常");
					ex.printStackTrace();
				}






			}
			else if (e.getSource() == jMenuItem1[3]) {//创建目录
				if(GlobalState.loginmodel==2){
					JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持云端创建目录");
					return;
				}

				if(tempNodearr==null){
					JOptionPane.showMessageDialog( mainPanel,"请选择云端父目录");
					return;
				}

				if(tempNodearr.length==0){
					JOptionPane.showMessageDialog( mainPanel,"请选择云端父目录");
					return;
				}
				if(tempNodearr.length>1){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个云端父目录");
					return;
				}

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNodearr[0];
				FileBean fb=(FileBean)node.getUserObject();
				if(fb.isIsFile()){
					JOptionPane.showMessageDialog( mainPanel,"您选择的是文件，请选择1个云端父目录");
					return;
				}
				String parentpath=fb.getPath();
				String parentname=fb.getName();
				System.out.println("parentpath  "+parentpath+"  parentname  "+parentname);

				try {

					createFolder createf=new createFolder(1,node,jTree1);//创建云端目录
					createf.create();
					//updateTree20();

				}
				catch (Exception e1) {
					e1.printStackTrace();
				}


			}

			else if (e.getSource() == jMenuItem1[2]) {//删除


				if(GlobalState.loginmodel==2){
					JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持云端目录删除");
					return;
				}

				for(int i=0;i<tempNodearr.length;i++){
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNodearr[i].getParent();
					if (node == null) {
						JOptionPane.showMessageDialog( mainPanel,"根目录不能被删除!");
						return;
					}
				}

				//删除单个文件
				//DeleteTask delete= new DeleteTask(mainPanel,tempNode, jTree1);
				//delete.delete();

				//linjiancai 删除多个文件
				DeleteTask delete= new DeleteTask(mainPanel,tempNodearr, jTree1);
				delete.delete2();


				System.out.println("delete cloud file");

			}
			else if (e.getSource() == jMenuItem1[1]) {//下载
	            	/*
	            	 * 在这里判断是否需要同步，即最后修改时间的比较
	            	 * File dir = new File("D:" + ((FileBean)tempNode.getUserObject()).getPath());
                	if (!(dir.exists() || dir.mkdirs())) {
                	}
	            	 */

				if(GlobalState.loginmodel==2){
					JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持下载");
					return;
				}


				GlobalState.state = "download";

	            	/*
	            	//linjiancai  单个文件的下载
	            	DownloadTask download= new DownloadTask(tempNode, jTree2);
	            	download.download();
	            	*/

				//多个文件的下载
				//for(int i=0;i<tempNodearr.length;i++){
				//DefaultMutableTreeNode tmpNode=tempNodearr[i];
				DownloadTask download= new DownloadTask(tempNodearr, jTree2);
				//download.download2(tempNodearr);//download2和download3都可以下载
				download.download3(tempNodearr);

				//}


				//drawTree2();//这两个都可以
				updateTree20();//这两个都可以
				GlobalState.state = "logined";
				//updateTree2(); //刷新本地目录
				//jTree2.repaint();
				System.out.println("download cloud file");
			} else if (e.getSource() == jMenuItem1[0]) {//刷新
				//getfilechildren1(root1);//刷新云目录
				updateTree();//刷新云目录
				//updateTree2();//刷新本地目录
			}




			e.setSource(null);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				jPopupMenu1.setVisible(false);
			}
		}
	}

	class MyPopMenuActionListener2 extends MouseAdapter  implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			//DefaultMutableTreeNode root1=(DefaultMutableTreeNode)((DefaultTreeModel) jTree1.getModel()).getRoot();
			//DefaultMutableTreeNode root2=(DefaultMutableTreeNode)((DefaultTreeModel) jTree2.getModel()).getRoot();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNode.getParent();
			//File dir = new File("D:" + "/" + username);
			Desktop desktop = Desktop.getDesktop();
			if (e.getSource() == jMenuItem2[0]) {//打开
				File file = new File(((FileBean) ((DefaultMutableTreeNode) tempNode).getUserObject()).getPath());
				if (!file.exists()) {
					JOptionPane.showMessageDialog(mainPanel, "该文件已经被删除!");
					e.setSource(null);
					return;
				}
				try {
					desktop.open(file);
				}
				catch (IOException ex) {
					JOptionPane.showMessageDialog(mainPanel, "请检查文件是否被某应用程序占用!");
					e.setSource(null);
					ex.printStackTrace();
					return;
				}
			}
			else if (e.getSource() == jMenuItem2[1]) {//上传
				//jPopupMenu2.setVisible(false);
				GlobalState.state = "upload";
				UploadTask upload=  new UploadTask(tempNode,username);
				try {
					upload.upload();
					updateTree1();//刷新云目录
					updateTree2();//刷新本地目录
				}
				catch (Exception e1) {
					e1.printStackTrace();

					return;
				}
				e.setSource(null);
				GlobalState.state = "logined";
				System.out.println("submit local");
				// JOptionPane.showMessageDialog(mainPanel, "Upload successfully!");
			}
			else if (e.getSource() == jMenuItem2[2]) {//删除
				if (node == null) {
					System.out.println("根目录不能被删除");
					JOptionPane.showMessageDialog(mainPanel, "根目录不能被删除!");//若没有父节点说明该节点是根节点，警告不能删除
				}else if (tempNode.getChildCount() > 0)	{
					JOptionPane.showMessageDialog(mainPanel, "请先删除该文件夹下的文件!");//有子节点的节点不能删除
				}else {
					GlobalState.state = "delete";
					String path = ((FileBean) tempNode.getUserObject()).getPath();
					File file = new File(path);
					file.delete();
					jTree2.repaint();
					((DefaultTreeModel) jTree2.getModel()).removeNodeFromParent(tempNode);
					//tempNode = null;
					GlobalState.state = "logined";
					System.out.println("delete local file");
				}
			}
			e.setSource(null);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				jPopupMenu1.setVisible(false);
			}
		}
	}


	class MyPopMenuActionListener4 extends MouseAdapter  implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			//DefaultMutableTreeNode root1=(DefaultMutableTreeNode)((DefaultTreeModel) jTree1.getModel()).getRoot();
			//DefaultMutableTreeNode root2=(DefaultMutableTreeNode)((DefaultTreeModel) jTree2.getModel()).getRoot();
			// DefaultMutableTreeNode node=new DefaultMutableTreeNode(); //= (DefaultMutableTreeNode) tempNode.getParent();
			//File dir = new File("D:" + "/" + username);
			Desktop desktop = Desktop.getDesktop();
			if(e.getSource() == jMenuItem2[0]) {//刷新
				updateTree20();
			}
			else if (e.getSource() == jMenuItem2[1]) {//打开
				for(int i=0;i<tempNodearrlocal.length;i++){
					File file = new File(((FileBean) ((DefaultMutableTreeNode) tempNodearrlocal[i]).getUserObject()).getPath());
					System.out.println(((FileBean) ((DefaultMutableTreeNode) tempNodearrlocal[i]).getUserObject()).getPath());
					if (!file.exists()) {
						JOptionPane.showMessageDialog(mainPanel, "该文件已经被删除!");
						e.setSource(null);
						//return;
					}
					else{
						try {
							//desktop.open(file);

							String[] cmd = new String[5];
							String url = ((FileBean) ((DefaultMutableTreeNode) tempNodearrlocal[i]).getUserObject()).getPath();
							cmd[0] = "cmd";
							cmd[1] = "/c";
							cmd[2] = "start";
							cmd[3] = " ";
							cmd[4] = url;
							Runtime.getRuntime().exec(cmd);

						}
						catch (IOException ex) {
							JOptionPane.showMessageDialog(mainPanel, "请检查文件是否被某应用程序占用!");
							e.setSource(null);
							ex.printStackTrace();
							//return;
						}
					}
				}


			}
			else if (e.getSource() == jMenuItem2[2]) {//上传
				//jPopupMenu2.setVisible(false);

	        		/*//原来单个上传
	        		GlobalState.state = "upload";
	        		UploadTask upload=  new UploadTask(tempNode,username);
	        		try {
	        			upload.upload();
	        			updateTree1();//刷新云目录
	        			updateTree2();//刷新本地目录
	        			}
	        		catch (Exception e1) {
	        			e1.printStackTrace();
	        			return;
	        			}
	        		e.setSource(null);
	        		GlobalState.state = "logined";
	        		System.out.println("submit local");
	        		*/

				//linjiancai 多个上传
				if(GlobalState.loginmodel==2){
					JOptionPane.showMessageDialog(new JPanel(),"离线登录模式下不支持本地文件上传");
					return;
				}



				GlobalState.state = "upload";
				UploadTask upload=  new UploadTask(username,jTree1,mainPanel);
				try {
					//upload.upload2(tempNodearrlocal);//有限制1M的大小
					upload.uploadunlimit(tempNodearrlocal);//不限制大小，用另外一种方式进行上传
					//upload.upload3();//upload3有点问题
					updateTree10();//刷新云目录
					//updateTree20();//刷新本地目录
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}

				e.setSource(null);
				GlobalState.state = "logined";
				System.out.println("submit local");


				// JOptionPane.showMessageDialog(mainPanel, "Upload successfully!");
			}


			else if (e.getSource() == jMenuItem2[3]) {//删除

				System.out.println("tempNodearrlocal.length "+tempNodearrlocal.length);
				for(int i=0;i<tempNodearrlocal.length;i++){
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNodearrlocal[i].getParent();
					if (node == null) {
						JOptionPane.showMessageDialog( mainPanel,"根目录不能被删除!");
						return;
					}
				}
				try{
					GlobalState.state = "delete";
					int filenum=tempNodearrlocal.length;
					FileBean[] fb =new FileBean[filenum];
					for(int i=0;i<filenum;i++){
						fb[i]=(FileBean) tempNodearrlocal[i].getUserObject();
					}
					for(int i=0;i<filenum;i++){
						boolean isFile=fb[i].isIsFile();
						String name=fb[i].getName();
						String path=fb[i].getPath();
						File file = new File(path);
						if(isFile){//是文件
							if(file.exists()){
								try{
									//判断是否打开着
									if(file.renameTo(file)){
										//文件没被使用
										file.delete();
										JOptionPane.showMessageDialog(mainPanel, "删除成功!");
										System.out.println("meidakai");
									}else{
										//文件在用
										JOptionPane.showMessageDialog(mainPanel, "请检查文件是否被某应用程序占用!");
										System.out.println("dakai");
									}


								}catch(Exception ex2){
									JOptionPane.showMessageDialog(mainPanel, "请检查文件是否被某应用程序占用!");
									ex2.printStackTrace();
								}
							}else{
								System.out.println("已经删除了");
							}
						}else {//是目录
							if(file.exists()){
								try{
									delFolder(path);
									JOptionPane.showMessageDialog(mainPanel, "删除成功!");
								}catch(Exception ex2){
									JOptionPane.showMessageDialog(mainPanel, "删除异常");
									ex2.printStackTrace();
								}
							}
							else{
								System.out.println("已经删除了");
							}

						}
					}
					GlobalState.state = "logined";
				}catch(Exception ex){
					ex.printStackTrace();
				}


				//就是updateTree20的功能
				DefaultMutableTreeNode root=(DefaultMutableTreeNode) jTree2.getModel().getRoot();
				deleteAllTree20(root);
				File dir = new File("D:" + "/" + username);
				getfilechildren2(root,dir,"D:" + "/" + username);//
				jTree2.expandPath(new  TreePath(root.getPath()));
				//drawTree2();
				tempNodearrlocal=null;
				//GlobalState.state = "delete";
				System.out.println("delete local file");
				e.setSource(null);
			}

			else if (e.getSource() == jMenuItem2[4]) {//创建本地目录




				if(tempNodearrlocal==null){
					JOptionPane.showMessageDialog( mainPanel,"请选择本地父目录");
					return;
				}

				if(tempNodearrlocal.length==0){
					JOptionPane.showMessageDialog( mainPanel,"请选择本地父目录");
					return;
				}
				if(tempNodearrlocal.length>1){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个本地父目录");
					return;
				}

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNodearrlocal[0];
				FileBean fb=(FileBean)node.getUserObject();
				if(fb.isIsFile()){
					JOptionPane.showMessageDialog( mainPanel,"您选择的是文件，请选择1个本地父目录");
					return;
				}
				String parentpath=fb.getPath();
				String parentname=fb.getName();
				System.out.println("parentpath  "+parentpath+"  parentname  "+parentname);

				try {

					createFolder createf=new createFolder(0,node,jTree2);//创建本地目录
					createf.create();
					//updateTree20();

				}
				catch (Exception e1) {
					e1.printStackTrace();
				}




				//updateTree20();
			}

			else if (e.getSource() == jMenuItem2[5]) {//BF选择文件

				if(tempNodearrlocal==null || tempNodearrlocal.length==0){
					return ;
				}
				text1.setText("");

				for(int i=0;i<tempNodearrlocal.length;i++){
					FileBean fb=(FileBean) ((DefaultMutableTreeNode) tempNodearrlocal[i]).getUserObject();
					File file = new File(((FileBean) ((DefaultMutableTreeNode) tempNodearrlocal[i]).getUserObject()).getPath());
					String path=fb.getPath();
					System.out.println(path);
					if (!file.exists()) {
						JOptionPane.showMessageDialog(mainPanel, "该文件已经被删除!");
						e.setSource(null);
						//return;
					}
					else{
						try {
							//desktop.open(file);
							if(!file.isFile()){
								System.out.println("是文件夹");
								//if(!fb.isIsFile())
								//	System.out.println("是文件夹");
								continue;
							}

							StringBuffer strb=new StringBuffer();
							for(int j=0;j<path.length();j++){
								if(path.charAt(j)=='/')
									strb.append("\\");
								else
									strb.append(path.charAt(j));
								//str.replace(oldChar, newChar)
							}
							path=strb.toString();
							text1.append(path+"\n");


						}
						catch (Exception ex) {
							JOptionPane.showMessageDialog(mainPanel, "BF选择文件异常!");
							e.setSource(null);
							ex.printStackTrace();
							//return;
						}
					}
				}
				tabbedPane.setSelectedIndex(0);

			}

			else if (e.getSource() == jMenuItem2[6]) {//Excel选择文件


				if(tempNodearrlocal==null){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个本地Excel文件");
					return;
				}

				if(tempNodearrlocal.length==0){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个本地Excel文件");
					return;
				}
				if(tempNodearrlocal.length>1){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个本地Excel文件");
					return;
				}

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNodearrlocal[0];
				FileBean fb=(FileBean)node.getUserObject();
				if(!fb.isIsFile()){
					JOptionPane.showMessageDialog( mainPanel,"您选择的是文件夹，请选择1个本地Excel文件");
					return;
				}

				String path=fb.getPath();
				String name=fb.getName();
				System.out.println("path  "+path+"  name  "+name);
				if(!(name.endsWith(".xls")|| name.endsWith(".xlsx"))){
					JOptionPane.showMessageDialog( mainPanel,"您选择的不是Excel文件，请选择1个本地Excel文件");
					return;
				}

				File selectedFile = new File(path);
				if (!selectedFile.exists()) {
					JOptionPane.showMessageDialog(mainPanel, "该文件已经被删除!");
					e.setSource(null);
					//return;
				}
				else{

					box31.removeAllItems();
					box31.addItem("请选择加解密属性");
					box31.setSelectedIndex(0);
					box32.setSelectedIndex(0);

					StringBuffer strb=new StringBuffer();
					for(int j=0;j<path.length();j++){
						if(path.charAt(j)=='/')
							strb.append("\\");
						else
							strb.append(path.charAt(j));
						//str.replace(oldChar, newChar)
					}
					path=strb.toString();

					text33.setText(path);
					String fileinfo="";
					try{


						FileInputStream finput = new FileInputStream(selectedFile);
						POIFSFileSystem fs = new POIFSFileSystem(finput);//Excel读入系统
						HSSFWorkbook wb = new HSSFWorkbook(fs);//获取Excel文件
						HSSFSheet sheet = wb.getSheetAt(0);//获取Excel文件的第一个工作页面
						HSSFRow row = sheet.getRow(0);//第一行
						HSSFRow row2 = sheet.getRow(1);//第二行
						int rsColumns =row.getLastCellNum();//获取Sheet表中所包含的总列数
						for(int i=0;i<rsColumns;i++) {
							HSSFCell cell   = row.getCell(i);//得到第一行的各列单元格，即属性
							String getexcle=cell.getStringCellValue();
							box31.addItem(getexcle);

							HSSFCell cell2  = row2.getCell(i);
							if (cell2.getCellType() == HSSFCell.CELL_TYPE_STRING) { //单元格是字符串
								fileinfo=fileinfo+getexcle+"	字符串型       数据:"+cell2.getStringCellValue()+"...\n";
							}
							else if(cell2.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { //单元格是数字
								fileinfo=fileinfo+getexcle+"	数值型         数据:"+cell2.getNumericCellValue()+"...\n";
							}

						}
						finput.close();

					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
					text34.setText(fileinfo);
					text35.setText("");
					tabbedPane.setSelectedIndex(2);
				}


			}
			else if (e.getSource() == jMenuItem2[7]) {//新建文件

				if(tempNodearrlocal==null){
					JOptionPane.showMessageDialog( mainPanel,"请选择本地父目录");
					return;
				}

				if(tempNodearrlocal.length==0){
					JOptionPane.showMessageDialog( mainPanel,"请选择本地父目录");
					return;
				}
				if(tempNodearrlocal.length>1){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个本地父目录");
					return;
				}

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNodearrlocal[0];
				FileBean fb=(FileBean)node.getUserObject();
				if(fb.isIsFile()){
					JOptionPane.showMessageDialog( mainPanel,"您选择的是文件，请选择1个本地父目录");
					return;
				}
				String parentpath=fb.getPath();
				String parentname=fb.getName();
				System.out.println("parentpath  "+parentpath+"  parentname  "+parentname);

				try {

					createFile createf=new createFile(node,jTree2);//创建本地目录
					createf.create();
					//updateTree20();

				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			else if (e.getSource() == jMenuItem2[8]) {//重命名

				if(tempNodearrlocal==null){
					JOptionPane.showMessageDialog( mainPanel,"请选择本地文件或者文件夹");
					return;
				}

				if(tempNodearrlocal.length==0){
					JOptionPane.showMessageDialog( mainPanel,"请选择本地文件或者文件夹");
					return;
				}
				if(tempNodearrlocal.length>1){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个本地文件或者文件夹");
					return;
				}


				//updateTree20();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNodearrlocal[0];
				FileBean fb=(FileBean)node.getUserObject();
				String path=fb.getPath();
				String name=fb.getName();
				boolean isfile=fb.isIsFile();
				if(name.equals(username)){
					JOptionPane.showMessageDialog( mainPanel,"根目录不能重命名");
					return;
				}

				System.out.println("path  "+path+"  name  "+name);


				try {
					Rename rename=new Rename(0,node,jTree2,name,path,isfile);//创建本地目录
					rename.rename();
					//createFile createf=new createFile(node,jTree2);//创建本地目录
					//createf.create();
					//updateTree20();

				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			else if (e.getSource() == jMenuItem2[9]) {//文件属性

				if(tempNodearrlocal==null){
					JOptionPane.showMessageDialog( mainPanel,"请选择本地文件或者文件夹");
					return;
				}

				if(tempNodearrlocal.length==0){
					JOptionPane.showMessageDialog( mainPanel,"请选择本地文件或者文件夹");
					return;
				}
				if(tempNodearrlocal.length>1){
					JOptionPane.showMessageDialog( mainPanel,"请选择1个本地文件或者文件夹");
					return;
				}


				//updateTree20();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tempNodearrlocal[0];
				FileBean fb=(FileBean)node.getUserObject();
				String path=fb.getPath();
				String name=fb.getName();
				boolean isfile=fb.isIsFile();


				File file=new File(path);
				if(file.exists()){
					double length=file.length();

					String showMessage="位置："+path+"\n";
					if(isfile)
						showMessage=showMessage+"文件名："+name+"\n";
					else
						showMessage=showMessage+"文件夹名："+name+"\n";


					DecimalFormat df=new DecimalFormat("#.00");
					if(!isfile){
						try{
							length=(double)getFileSize(file);
						}
						catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					if(length==0.0)
						showMessage=showMessage+"占用空间："+"0.00 B\n";
					else if(length>1024*1024*1024)
						showMessage=showMessage+"占用空间："+df.format(length/(1024*1024*1024))+" GB\n";
					else if(length>1024*1024)
						showMessage=showMessage+"占用空间："+df.format(length/(1024*1024))+" MB\n";
					else if(length>1024)
						showMessage=showMessage+"占用空间："+df.format(length/(1024))+" KB\n";
					else
						showMessage=showMessage+"占用空间："+df.format(length)+" B\n";

					String getTime = null;
					if(isfile){//是文件
						try {
							Process p = Runtime.getRuntime().exec(
									"cmd.exe /c dir " + file.getAbsolutePath() + " /tc");
							InputStream is = p.getInputStream();
							BufferedReader br = new BufferedReader(new InputStreamReader(is));
							String result;

							while ((result = br.readLine()) != null) {
								String[] str = result.split(" ");
								for (int i = str.length - 1; i >= 0; i--) {
									if (str[i].equals(name)) {
										getTime = str[0] + " " + str[2];
									}
								}
							}

							if(!(getTime==null || getTime.equals("null") || getTime.equals("")))
								showMessage=showMessage+"创建时间："+getTime+"\n";

						} catch (java.io.IOException exc) {
							exc.printStackTrace();
						}
					}
					if(getTime==null || getTime.equals("null") || getTime.equals("")){


						try {
							Process ls_proc = Runtime.getRuntime().exec(

									"cmd.exe /c dir " + file.getAbsolutePath() + " /tc");

							BufferedReader br = new BufferedReader(new InputStreamReader(ls_proc.getInputStream()));
							for (int i = 0; i < 5; i++) {
								br.readLine();
							}
							String stuff = br.readLine();
							StringTokenizer st = new StringTokenizer(stuff);
							String dateC = st.nextToken();
							String time = st.nextToken();
							String datetime = dateC.concat(time);
							br.close();
							String tmp=datetime.substring(0,datetime.lastIndexOf("/")+3);
							String tmp2=datetime.substring(datetime.lastIndexOf("/")+3);
							showMessage=showMessage+"创建时间："+tmp+" "+tmp2+"\n";
						}
						catch (Exception e1) {
							e1.printStackTrace();
						}

					}

					long time = file.lastModified();//返回文件最后修改时间，是以个long型毫秒数
					String ctime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(time));
					showMessage=showMessage+"最后修改时间："+ctime+"\n";
					JOptionPane.showMessageDialog( mainPanel,showMessage);
				}

			}



			e.setSource(null);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				jPopupMenu1.setVisible(false);
			}
		}

		// 递归   获取文件夹的大小
		public   long  getFileSize(File f)
				throws  Exception
		{
			long  size =  0 ;
			File flist[] = f.listFiles();
			for  ( int  i =  0 ; i < flist.length; i++)
			{
				if  (flist[i].isDirectory())
				{
					size = size + getFileSize(flist[i]);
				} else
				{
					size = size + flist[i].length();
				}
			}
			return  size;
		}


	}

	//删除文件夹
	//param folderPath 文件夹完整绝对路径

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); //删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); //删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//linjiancai
	//删除指定文件夹下所有文件
	//param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);//再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	//linjiancai


	public  boolean fileEncrypt(final String[] filedir){   //加密文件内容和名字，将加密后的文件存放在相应的位置
		BFencresult="";
		isallempty=true;//全部为空
		try{


			Shell shella=new Shell(SWT.CLOSE|SWT.MAX); //整个视图窗口
			shella.setText("加密");
			shella.setSize(400,300);
			IRunnableWithProgress run=new IRunnableWithProgress(){
				public void run(IProgressMonitor monitor) throws InterruptedException{
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
					monitor.beginTask("开始执行",filedir.length);

					for(int i=0;i<filedir.length;i++){
						if(monitor.isCanceled()){
							return;
						}

						try{
							File f=new File(filedir[i]);//对文件名进行加密
							System.out.println("f.length()  "+f.length());
							if(f.length()==0){
								System.out.println("文件内容为空，不加密");
								//JOptionPane.showMessageDialog( mainPanel,"文件  "+filedir[i]+"内容为空，不加密");
								continue;
							}
							isallempty=false;
							String name=f.getName();  //System.out.println(name);

							en_name[i]=aes.encrypt(name);	 //加密的文件名

							content=allF.readFile(filedir[i]);
							String en_content=aes.encrypt(content); //加密的文件内容

							String[] kw=kwAbstract(name);      //文件关键字的BF	 。是对明文进行加密，不是经过AES加密过的  ，在这个函数里面，对明文是经过RSA加密的 。
							//与检索参数的加密是一样的，一个关键字串对应一个加密值
							BF[i]=kwBF(kw,0.001,rand[i]);	//每个rand[i]都不一样，随机的，加在关键字后面进行混淆，kw是一个文件名的明文经过RSA加密过的，为检索服务，保存在0.txt文件中


							f = new File("D:\\qingyunclient\\atest\\encrypted_file\\"+username+"\\"+en_name[i]); //加密的文件内容直接写入文件，文件名和文件内容通过AES加密。
							if (!f.exists()) {
								f.createNewFile();
							}
							BufferedWriter output = new BufferedWriter(new FileWriter(f));
							output.write(en_content);
							output.close();

							BFencresult=BFencresult+"D:\\qingyunclient\\atest\\encrypted_file\\"+username+"\\"+en_name[i]+"\n";
						}catch(Throwable el){}
						monitor.setTaskName("一共"+filedir.length+"个文件，正在加密第"+(i+1)+"个文件");
						monitor.worked(1);

					}

					if(isallempty)
					{
						//JOptionPane.showMessageDialog( mainPanel,"文件全部为空，加密结束");
						return;

					}

					try{
						File f=new File("D:\\qingyunclient\\atest\\encrypted_file\\"+username+"\\0.txt");
						if (!f.exists()) {
							f.createNewFile();
						}
						String en_info="";
						for(int i=0;i<sfile;i++){
							en_info=en_info+"filename:"+en_name[i]+"\r\n"; //AES 加密过的文件名，作为检索参数使用
							en_info=en_info+"random:"+rand[i]+"\r\n";//随机值，与文件名关键字进行运算，进行混淆作用
							en_info=en_info+"BF:"+BF[i]+"\r\n";//BF映射的结果
						}
						BufferedWriter output1 = new BufferedWriter(new FileWriter(f));
						output1.write(en_info);
						output1.close();
						BFencresult="D:\\qingyunclient\\atest\\encrypted_file\\"+username+"\\0.txt\n"+BFencresult;


						monitor.done();
					}catch(Exception e2){
						e2.printStackTrace();
					}


				}
			};
			try{
				new ProgressMonitorDialog(shella).run(true,true,run);
				//new ProgressMonitorDialog(shell).run(true,true,run);
			}catch(Exception e2){
				e2.printStackTrace();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return isallempty;

	}



	public static void fileEncrypt(final String filedir){   //加密文件内容和名字，将加密后的文件存放在相应的位置

		deleteAllFile("D:\\qingyunclient\\atest\\encrypted_file");
		int rand=(int)(Math.random()*10000);
		String BF;
		String en_name;
		AllFile allF=new AllFile();
		String content="";
		File dirFile = new  File("D:\\qingyunclient\\atest\\encrypted_file");
		if (! (dirFile.exists())&& ! (dirFile.isDirectory())) dirFile.mkdirs();
		File f=new File(filedir);
		String name=f.getName();  //System.out.println(name);

		aes= new AES();
		en_name=aes.encrypt(name);	 //加密的文件名

		content=allF.readFile(filedir);
		String en_content=aes.encrypt(content); //加密的文件内容

		String[] kw=kwAbstract(name);      //文件关键字的BF
		BF=kwBF(kw,0.001,rand);

		f = new File("D:\\qingyunclient\\atest\\encrypted_file\\"+en_name);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BufferedWriter output;
		try {
			output = new BufferedWriter(new FileWriter(f));
			output.write(en_content);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			File f1=new File("D:\\qingyunclient\\atest\\encrypted_file\\0.txt");
			if (!f1.exists()) {
				try {
					f1.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
			String en_info="";
			en_info=en_info+"filename:"+en_name+"\r\n";
			en_info=en_info+"random:"+rand+"\r\n";
			en_info=en_info+"BF:"+BF+"\r\n";
			BufferedWriter output1;
			output1 = new BufferedWriter(new FileWriter(f1));
			output1.write(en_info);
			output1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public  void fileDecrypt(final String[] filedir){   //加密文件内容和名字，将加密后的文件存放在相应的位置
		BFdecresult="";
		Shell shella=new Shell(SWT.CLOSE|SWT.MAX); //整个视图窗口
		shella.setText("解密");
		shella.setSize(400,300);
		IRunnableWithProgress run=new IRunnableWithProgress(){
			public void run(IProgressMonitor monitor) throws InterruptedException{
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
				monitor.beginTask("开始执行",filedir.length);
				for(int i=0;i<filedir.length;i++){
					if(monitor.isCanceled()){
						return;
					}
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

								}
								//System.out.println("---------------name------------"+name);
								//System.out.println("---------------fromother------------"+fromother);
								if(fromother.equals("") || fromother.equals(username)){//自己的

									//System.out.println("##############AES key "+aes_key);
									aes=new AES(aes_key);
									de_name1[i]=aes.decrypt(name);	 //解密的文件名


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


								monitor.setTaskName("一共"+unzipfiledir.length+"个文件，正在解密第"+(j+1)+"个文件");
								monitor.worked(1);
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
								aes=new AES(aes_key);
								de_name1[i]=aes.decrypt(name);	 //解密的文件名


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
							monitor.setTaskName("一共"+filedir.length+"个文件，正在解密第"+(i+1)+"个文件");
							monitor.worked(1);
						}
					}catch(Throwable el){}
				}
				monitor.done();
			}
		};
		try{

			new ProgressMonitorDialog(shella).run(true,true,run);
		}catch(Exception e2){
			e2.printStackTrace();
		}

	}

	public static void fileDecrypt(final String filedir){   //加密文件内容和名字，将加密后的文件存放在相应的位置

		deleteAllFile("D:\\qingyunclient\\atest\\decrypted_file");
		unZip zips=new unZip();
		String de_name1;
		String content1;
		AllFile allF=new AllFile();
		File dirFile = new  File("D:\\qingyunclient\\atest\\decrypted_file");
		if (! (dirFile.exists())&& ! (dirFile.isDirectory())) dirFile.mkdirs();
		String pt=filedir;
		pt=pt.substring(pt.indexOf(".")+1);
		System.out.println("pt="+pt.compareTo("zip"));
		if(pt.compareTo("zip")==0){
			deleteAllFile("D:\\qingyunclient\\atest\\zip"+user_name);
			zips.extZipFileList(filedir,user_name);
			String[] unzipfiledir=zips.readFloder(user_name);
			int sfile=unzipfiledir.length;
			String[] de_name=new String[sfile];
			String[] content=new String[sfile];
			for(int j=0;j<unzipfiledir.length;j++){
				File f=new File(unzipfiledir[j]);
				String name=f.getName();
				String owner_name=name.substring(name.indexOf("__")+1);
				name=name.substring(0,name.indexOf("__"));
				//String[][] owner
				//System.out.println("name="+name+"----"+aes.password);
				try {
					de_name[j]=aes.decrypt(name);
				} catch (IllegalBlockSizeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (BadPaddingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	 //解密的文件名
				//System.out.println(de_name[i]);
				try{
					content[j]="";
					InputStreamReader read = new InputStreamReader(new FileInputStream(f));
					BufferedReader reader=new BufferedReader(read);
					String line="";
					while ((line = reader.readLine()) != null) {
						content[j]=content[j]+line;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				String de_content;
				try {
					de_content = aes.decrypt(content[j]);
					String url="D:\\qingyunclient\\atest\\decrypted_file\\"+de_name[j];
					allF.writeFile(de_content, url);
				} catch (IllegalBlockSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //解密的文件内容
			}
		}
		else{
			System.out.println("------------------------------"+filedir);
			File f=new File(filedir);
			String name=f.getName();
			//String owner_name=name.substring(name.indexOf("__")+1);
			if(name.indexOf("__")!=-1) name=name.substring(0,name.indexOf("__"));
			System.out.println("---------------?------------"+name);

			try {
				de_name1=aes.decrypt(name);
				try{
					content1="";
					InputStreamReader read = new InputStreamReader(new FileInputStream(f));
					BufferedReader reader=new BufferedReader(read);
					String line="";
					while ((line = reader.readLine()) != null) {
						content1=content1+line;
					}
					String de_content=aes.decrypt(content1); //解密的文件内容
					String url="D:\\qingyunclient\\atest\\decrypted_file\\"+de_name1;
					allF.writeFile(de_content, url);
				}catch(Exception e){
					e.printStackTrace();
				}

			} catch (IllegalBlockSizeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	 //解密的文件名



		}


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
			rs=bf.ran_encrypt_para(result[i],rsa.n.toString());
			result[i]=rs.toString();
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

	public  String OP_CE__dirdec(String filePath) { //文件直接解密
		String newfilepath="";
		try{

			FileInputStream finput = new FileInputStream(filePath);
			POIFSFileSystem fs = new POIFSFileSystem(finput);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);

			HSSFRow row0 = sheet.getRow(0);//处理第一行，加密的时候添加加密信息，解密的时候消除解密信息
			int rsColumns =row0.getLastCellNum();//获取Sheet表中所包含的总列数
			int enctype[]=new int[rsColumns];
			boolean hasenc=false;
			HSSFCell cell0;
			for(int i=0;i<rsColumns;i++){
				enctype[i]=0;
				try{
					cell0=row0.getCell(i);
					String firstrow="";
					if (cell0.getCellType() == HSSFCell.CELL_TYPE_STRING) { //单元格是字符串
						firstrow=cell0.getStringCellValue();
					}
					else if(cell0.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { //单元格是数字
						firstrow=""+cell0.getNumericCellValue();
					}
					if((firstrow.charAt(1)=='@') && (firstrow.charAt(2)=='#')){//有经过加密的
						hasenc=true;
						enctype[i]=Integer.parseInt(firstrow.substring(0,1));

					}

				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
			if(!hasenc){
				JOptionPane.showMessageDialog(mainPanel, "该Excel文件还没有经过加密");
				return newfilepath;
			}
			boolean isdecok=true;
			for(int i=0;i<rsColumns;i++){
				//System.out.println("enctype  "+ enctype[i]);
				if(enctype[i]==1){ //OP加密过
					System.out.println("opeart_k  "+opeart_k);




					OPEART op=null;
					if(filePath.endsWith("__.xls") || filePath.endsWith("__.xlsx")){//其他用户的文件

						String othername=filePath;
						othername=othername.substring(0,othername.lastIndexOf("__"));
						othername=othername.substring(othername.lastIndexOf("__")+2);
						Cert cert=new Cert();
						String res=cert.getCert1(othername,user_name);
						if(res.equals("")){

							JOptionPane.showMessageDialog(mainPanel, "你没有获得 "+othername+"的证书");
							return newfilepath;
						}

						String keyarr[]=res.split("&");
						String opeartkey=keyarr[5];
						long value=Long.parseLong(opeartkey);
						op=new OPEART(value);
					}
					else {
						op=new OPEART(opeart_k);
					}
					cell0=row0.getCell(i);
					String firstrow="";
					if (cell0.getCellType() == HSSFCell.CELL_TYPE_STRING) { //单元格是字符串
						firstrow=cell0.getStringCellValue();
					}
					else if(cell0.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { //单元格是数字
						firstrow=""+cell0.getNumericCellValue();
					}
					firstrow=firstrow.substring(3);//去掉前缀
					row0.createCell(i).setCellValue(firstrow);


					int rsRows = sheet.getLastRowNum();//获取Sheet表中所包含的总行数 ,从0开始
					HSSFRow row ;
					HSSFCell cell;
					int str_num=0;//判断解密后的excel是字符串还是数字,0是数字，1是字符串
					for(int j=1;j<=rsRows;j++) {
						row = sheet.getRow(j);
						cell   = row.getCell(i);
						String getexcle="";
						String result="";
						if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) { //单元格是字符串
							getexcle=cell.getStringCellValue();
						}
						else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { //单元格是数字
							getexcle=""+cell.getNumericCellValue();

							// System.out.println(getexcle);
						}
						//解密
						result=op.decrypt(getexcle);
						try {
							if(str_num==0) { //数字,明文是数字的，转化为数字
								double double_result=Double.valueOf(result);
								row.createCell(i).setCellValue(double_result);
							}
							else { //字符串
								row.createCell(i).setCellValue(result);
							}
						}
						catch(Exception ex) {
							str_num=1; //改为字符串
							row.createCell(i).setCellValue(result); //修改第一个字符串
							ex.printStackTrace();
						}
					}

					OutputStream fos = new FileOutputStream(filePath);
					wb.write(fos);
					fos.close();
					finput.close();


				}
				else if(enctype[i]==2){//CE加密过
					cell0=row0.getCell(i);
					String firstrow="";
					if (cell0.getCellType() == HSSFCell.CELL_TYPE_STRING) { //单元格是字符串
						firstrow=cell0.getStringCellValue();
					}
					else if(cell0.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { //单元格是数字
						firstrow=""+cell0.getNumericCellValue();
					}
					firstrow=firstrow.substring(3);//去掉前缀
					row0.createCell(i).setCellValue(firstrow);

					// System.out.println("cescmc_n  "+cescmc_n+"  cescmc_k  "+cescmc_k);
					// CESCMC cescmc=new CESCMC(cescmc_n,cescmc_k);
					CESCMC cescmc=null;
					if(filePath.endsWith("__.xls") || filePath.endsWith("__.xlsx")){//其他用户的文件

						String othername=filePath;
						othername=othername.substring(0,othername.lastIndexOf("__"));
						othername=othername.substring(othername.lastIndexOf("__")+2);
						Cert cert=new Cert();
						String res=cert.getCert1(othername,user_name);
						if(res.equals("")){

							JOptionPane.showMessageDialog(mainPanel, "你没有获得 "+othername+"的证书");
							return newfilepath;
						}

						String keyarr[]=res.split("&");
						String cescmc_k_str=keyarr[6];
						String cescmc_n_str=keyarr[7];

						int cescmc_k_int=Integer.parseInt(cescmc_k_str);
						int cescmc_n_int=Integer.parseInt(cescmc_n_str);

						cescmc=new CESCMC(cescmc_n_int,cescmc_k_int);
					}
					else {
						cescmc=new CESCMC(cescmc_n,cescmc_k);
					}





					int rsRows = sheet.getLastRowNum();//获取Sheet表中所包含的总行数 ,从0开始
					HSSFRow row ;
					HSSFCell cell;

					for(int j=1;j<=rsRows;j++) {
						row = sheet.getRow(j);
						cell   = row.getCell(i);
						String getexcle="";
						if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) { //单元格是字符串
							getexcle=cell.getStringCellValue();
						}
						else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { //单元格是数字
							getexcle=""+cell.getNumericCellValue();

							// System.out.println(getexcle);
						}
						//解密
						String[] st=getexcle.split(";");

						//System.out.println("st.length"+st.length);
						double[][]en_sn=new double[st.length][st.length];
						for(int l=0;l<st.length;l++){
							String[] st2=st[l].split(",");
							try{
								for(int k=0;k<st2.length;k++){
									en_sn[l][k]=Double.valueOf(st2[k]);
								}
							}
							catch(Exception e){
								e.printStackTrace();
							}

						}
						double de_sn=cescmc.decrypt(en_sn);
						row.createCell(i).setCellValue(de_sn);
					}

					OutputStream fos = new FileOutputStream(filePath);
					wb.write(fos);
					fos.close();
					finput.close();
				}
			}

			if(filePath.endsWith("__.xls") || filePath.endsWith("__.xlsx")){//其他用户的文件，更改文件名

				String filetype=filePath.substring(filePath.lastIndexOf("."));
				String tonewname=filePath;
				tonewname=tonewname.substring(0,tonewname.lastIndexOf("__"));
				tonewname=tonewname.substring(0,tonewname.lastIndexOf("__"));
				tonewname=tonewname+filetype;
				System.out.println("tonewname "+tonewname);
				File file=new File(filePath);
				File newfile =   new  File(tonewname);
				if(newfile.exists()){//存在的话先删除
					newfile.delete();
				}
				file.renameTo(newfile);
				newfilepath=tonewname;
			}


			JOptionPane.showMessageDialog(mainPanel, "Excel文件解密成功");
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(mainPanel, "Excel文件解密失败");
			ex.printStackTrace();
		}


		return newfilepath;
	}

	public  int OP_CE_enc_dec(int type,int enc_property,int enc_type,String filePath) {

		try {
			if(enc_type==1) { //OPEART加密算法
				System.out.println("opeart_k  "+opeart_k);
				OPEART op=new OPEART(opeart_k);
				FileInputStream finput = new FileInputStream(filePath);
				POIFSFileSystem fs = new POIFSFileSystem(finput);
				HSSFWorkbook wb = new HSSFWorkbook(fs);
				HSSFSheet sheet = wb.getSheetAt(0);

				HSSFRow row0 = sheet.getRow(0);//处理第一行，加密的时候添加加密信息，解密的时候消除解密信息
				HSSFCell cell0  = row0.getCell(enc_property-1);
				try{
					String firstrow="";
					if (cell0.getCellType() == HSSFCell.CELL_TYPE_STRING) { //单元格是字符串
						firstrow=cell0.getStringCellValue();
					}
					else if(cell0.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { //单元格是数字
						firstrow=""+cell0.getNumericCellValue();
					}
					if(type==0){//加密

						// firstrow=enc_type+"@#"+firstrow;//加前缀

						if((firstrow.charAt(1)=='@') && (firstrow.charAt(2)=='#')){//有经过加密的
							JOptionPane.showMessageDialog(mainPanel, "该属性已经经过保序加密！");
							return 7;
							// hasenc=true;
							//enctype[i]=Integer.parseInt(firstrow.substring(0,1));

						}
						firstrow=enc_type+"@#"+firstrow;//加前缀
					}//解密
					else{
						if(!((firstrow.charAt(1)=='@') && (firstrow.charAt(2)=='#'))){//没有经过加密的
							JOptionPane.showMessageDialog(mainPanel, "该属性还没经过保序加密！");
							return 7;

						}
						firstrow=firstrow.substring(3);//去掉前缀
					}
					row0.createCell(enc_property-1).setCellValue(firstrow);

				}
				catch(Exception ex){
					ex.printStackTrace();
				}

				HSSFRow row = sheet.getRow(1);
				HSSFCell cell  = row.getCell(enc_property-1);
				//  System.out.println(cell.getCellType()); //数字是0，字符串是1
				int rsRows = sheet.getLastRowNum();//获取Sheet表中所包含的总行数 ,从0开始
				//    System.out.println(rsRows); //数字是0，字符串是1
				int str_num=0;//判断解密后的excel是字符串还是数字,0是数字，1是字符串
				for(int j=1;j<=rsRows;j++) {
					row = sheet.getRow(j);
					cell   = row.getCell(enc_property-1);
					String getexcle="";
					String result="";
					if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) { //单元格是字符串
						//String result= op.encrypt(cell.getStringCellValue());
						getexcle=cell.getStringCellValue();
					}
					else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { //单元格是数字
						getexcle=""+cell.getNumericCellValue();
						if(getexcle.length()<head0.length()) //如果原来的长度小于head0，才在前面添加0，否则不用添加0
							getexcle=head0.substring(getexcle.length())+getexcle;
						// System.out.println(getexcle);
					}
					if(type==0) { //加密
						result=op.encrypt(getexcle);
						row.createCell(enc_property-1).setCellValue(result);
					}
					else //解密
					{



						// op=null;
						if(filePath.endsWith("__.xls") || filePath.endsWith("__.xlsx")){//其他用户的文件

							String othername=filePath;
							othername=othername.substring(0,othername.lastIndexOf("__"));
							othername=othername.substring(othername.lastIndexOf("__")+2);
							Cert cert=new Cert();
							String res=cert.getCert1(othername,user_name);
							if(res.equals("")){

								JOptionPane.showMessageDialog(mainPanel, "你没有获得 "+othername+"的证书");
								return 0;
							}
							String keyarr[]=res.split("&");
							String opeartkey=keyarr[5];
							long value=Long.parseLong(opeartkey);
							op=new OPEART(value);
						}
						// else {
						//	op=new OPEART(opeart_k);
						// }



						result=op.decrypt(getexcle);
						try {
							if(str_num==0) { //数字,明文是数字的，转化为数字
								double double_result=Double.valueOf(result);
								row.createCell(enc_property-1).setCellValue(double_result);
							}
							else { //字符串
								row.createCell(enc_property-1).setCellValue(result);
							}
						}
						catch(Exception ex) {
							str_num=1; //改为字符串
							row.createCell(enc_property-1).setCellValue(result); //修改第一个字符串
							ex.printStackTrace();
						}
					}
				}
				OutputStream fos = new FileOutputStream(filePath);
				wb.write(fos);
				fos.close();
				finput.close();
				if(type==0)
					return 1;//OPEART加密完成
				else
					return 2;//OPEART解密完成
			}

			else if(enc_type==2){ //CESCMC加密算法


				FileInputStream finput = new FileInputStream(filePath);
				POIFSFileSystem fs = new POIFSFileSystem(finput);
				HSSFWorkbook wb = new HSSFWorkbook(fs);
				HSSFSheet sheet = wb.getSheetAt(0);

				HSSFRow row0 = sheet.getRow(0);//处理第一行，加密的时候添加加密信息，解密的时候消除解密信息
				HSSFCell cell0  = row0.getCell(enc_property-1);
				try{
					String firstrow="";
					if (cell0.getCellType() == HSSFCell.CELL_TYPE_STRING) { //单元格是字符串
						firstrow=cell0.getStringCellValue();
					}
					else if(cell0.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { //单元格是数字
						firstrow=""+cell0.getNumericCellValue();
					}
					if(type==0){//加密
						if((firstrow.charAt(1)=='@') && (firstrow.charAt(2)=='#')){//有经过加密的
							JOptionPane.showMessageDialog(mainPanel, "该属性已经经过算术加密！");
							return 7;
							// hasenc=true;
							//enctype[i]=Integer.parseInt(firstrow.substring(0,1));

						}
						firstrow=enc_type+"@#"+firstrow;//加前缀
						// firstrow=enc_type+"@#"+firstrow;//加前缀
					}
					else{
						if(!((firstrow.charAt(1)=='@') && (firstrow.charAt(2)=='#'))){//没有经过加密的
							JOptionPane.showMessageDialog(mainPanel, "该属性还没经过算术加密！");
							return 7;

						}
						firstrow=firstrow.substring(3);//去掉前缀
						//firstrow=firstrow.substring(3);//去掉前缀
					}
					row0.createCell(enc_property-1).setCellValue(firstrow);

				}
				catch(Exception ex){
					ex.printStackTrace();
				}


				HSSFRow row = sheet.getRow(1);
				HSSFCell cell  = row.getCell(enc_property-1);
				//     System.out.println(cell.getCellType()); //数字是0，字符串是1
				int rsRows = sheet.getLastRowNum();//获取Sheet表中所包含的总行数 ,从0开始

				if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING && type==0) { //字符串  加密
					finput.close();
					return 3;//CESCMC算法只支持数字加密
				}
				else  if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING && type==1) { //字符串  解密


					//CESCMC cescmc=new CESCMC(cescmc_n,cescmc_k);
					CESCMC cescmc=null;
					if(filePath.endsWith("__.xls") || filePath.endsWith("__.xlsx")){//其他用户的文件

						String othername=filePath;
						othername=othername.substring(0,othername.lastIndexOf("__"));
						othername=othername.substring(othername.lastIndexOf("__")+2);
						Cert cert=new Cert();
						String res=cert.getCert1(othername,user_name);
						if(res.equals("")){

							JOptionPane.showMessageDialog(mainPanel, "你没有获得 "+othername+"的证书");
							return 0;
						}
						String keyarr[]=res.split("&");
						String cescmc_k_str=keyarr[6];
						String cescmc_n_str=keyarr[7];

						int cescmc_k_int=Integer.parseInt(cescmc_k_str);
						int cescmc_n_int=Integer.parseInt(cescmc_n_str);

						cescmc=new CESCMC(cescmc_n_int,cescmc_k_int);
					}
					else {
						cescmc=new CESCMC(cescmc_n,cescmc_k);
					}



					for(int j=1;j<=rsRows;j++) {

						row = sheet.getRow(j);
						cell   = row.getCell(enc_property-1);
						String getexcle=cell.getStringCellValue();
						String[] st=getexcle.split(";");

						//System.out.println("st.length"+st.length);
						double[][]en_sn=new double[st.length][st.length];
						for(int i=0;i<st.length;i++){
							String[] st2=st[i].split(",");
							try{
								for(int k=0;k<st2.length;k++){
									en_sn[i][k]=Double.valueOf(st2[k]);
								}
							}
							catch(Exception e){
								e.printStackTrace();
							}

						}
						double de_sn=cescmc.decrypt(en_sn);
						row.createCell(enc_property-1).setCellValue(de_sn);
					}
					OutputStream fos = new FileOutputStream(filePath);
					wb.write(fos);
					fos.close();
					finput.close();

					return 4;//CESCMC解密完成
				}
				else if(cell.getCellType() ==  HSSFCell.CELL_TYPE_NUMERIC && type==0 ) {	//数字	 加密
					CESCMC cescmc=new CESCMC(cescmc_n,cescmc_k);

					for(int j=1;j<=rsRows;j++) {
						row = sheet.getRow(j);
						cell   = row.getCell(enc_property-1);
						double sn=cell.getNumericCellValue();
						double[][]en_sn=cescmc.encrypt(sn);			//被操作数加密
						String en_str="";

						for(int i=0;i<cescmc_n;i++){
							for(int k=0;k<cescmc_n;k++){
								en_str=en_str+en_sn[i][k]+",";
							}
							en_str=en_str+";";
						}
						row.createCell(enc_property-1).setCellValue(en_str);
					}
					OutputStream fos = new FileOutputStream(filePath);
					wb.write(fos);
					fos.close();
					finput.close();
					return 5;//CESCMC加密完成
				}
				else if(cell.getCellType() ==  HSSFCell.CELL_TYPE_NUMERIC && type==1 ) { //数字解密
					finput.close();
					return 6;//CESCMC解密失败
				}
			}

			return 0;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return 7;//加解密失败
		}
	}

	public static String[][] readFile1(){
		FileInputStream fin=null;
		String[][] result=null;
		try{
			fin=new FileInputStream("D:\\qingyunclient\\test\\client\\public_key.txt");
			int bt=0;
			String tmp="";
			while((bt=fin.read())!=-1)	tmp=tmp+(char)bt;
			int i=0;
			String s=tmp;
			while(s.indexOf("\r\n")!=-1){
				s=s.substring(s.indexOf("\r\n")+2);
				i++;
			}
			s=tmp;
			result=new String[i][2];
			for(int j=0;j<i;j++){
				String ss=s.substring(0,s.indexOf("\r\n"));
				result[j][0]=ss.substring(0,ss.indexOf(":"));
				result[j][1]=ss.substring(ss.indexOf(":")+1);
				s=s.substring(s.indexOf("\r\n")+2);
				System.out.println(result[j][0]+": "+result[j][1]);
			}
			fin.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
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
	//创建文件夹
	class createFolder{
		//通讯相关参数
		private JPanel mainPanel;


		JFrame jframe;
		JTextField jfoldename;
		JButton sureBtn;
		JButton cancelBtn;
		JPanel btnPanel;	//面板
		Box box;	//盒子
		int type;//本地创建还是云端创建
		//信息输入框
		JLabel foldenameLabel;
		DefaultMutableTreeNode node;
		JTree jtree;
		boolean tag;

		public createFolder(int type,DefaultMutableTreeNode node,JTree jtree){
			//this.mainPanel=new JPanel();
			this.type = type;
			this.node =node;
			this.jtree=jtree;
		}
		public boolean create() {

			jframe=new JFrame();
			jframe.setTitle("新建文件夹");
			jframe.setSize(400, 120);
			btnPanel = new JPanel();
			box = Box.createVerticalBox();
			foldenameLabel = new JLabel("新建文件夹名:");
			jfoldename = new JTextField(20);
			//按钮
			sureBtn=new JButton("确    定");
			cancelBtn = new JButton("取     消");

			btnPanel.add(sureBtn);
			btnPanel.add(cancelBtn);

			//添加到盒子
			box.add(new InputPanel(foldenameLabel,jfoldename));
			box.add(btnPanel);

			//把面板添加到组件
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	//获取屏幕大小,定位窗体
			jframe.add(box);
			jframe.setResizable(false);
			jframe.setLocation((int)((screenSize.getWidth()-jframe.getWidth())/2),(int)(screenSize.getHeight()-jframe.getHeight())/2);
			jframe.setVisible(true);

			//确认按钮
			sureBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {


					try {
						String foldename=jfoldename.getText().toString().trim();
						if(foldename==null || foldename.equals("")){
							JOptionPane.showMessageDialog(mainPanel,"文件名不能为空");
							return;
						}
						int childcount=node.getChildCount();
						System.out.println("childcount "+childcount);
						for(int i=0;i<childcount;i++){
							DefaultMutableTreeNode childnode = (DefaultMutableTreeNode)node.getChildAt(i);
							FileBean fb2=(FileBean) childnode.getUserObject();
							if(fb2.isIsFile()){//是文件，查询下一个
								continue;
							}
							if(fb2.getName().equals(foldename)){//找到同名的，不能创建
								JOptionPane.showMessageDialog(mainPanel,"已经存在同名文件夹");
								return;
							}
						}
						System.out.println("可以创建 ");
						//可以创建
						FileBean fb=(FileBean)node.getUserObject();
						String parentpath=fb.getPath();
						System.out.println("parentpath  "+parentpath);





						if(type==0){//本地目录
							File newfiledir=new File(parentpath +"/"+foldename);
							if(newfiledir.exists()){
								System.out.println(newfiledir+"文件夹已经存在");
							}
							else{
								newfiledir.mkdirs();//创建文件夹

								//updateTree20();
								//System.out.println("newfiledir.length()  "+newfiledir.length());
								//File f = new File(localpath);
		    						       /*
	    									 FileBean tmp = new FileBean(newfiledir.isFile(), foldename, parentpath +"/"+foldename,newfiledir.length());
		    						         tmp.setFileType(FileUtil.getFileType(tmp));

		    						         // DefaultMutableTreeNode temp = new DefaultMutableTreeNode(foldename, !newfiledir.isFile());
		    						         DefaultMutableTreeNode temp = new DefaultMutableTreeNode(foldename, true);
		    						          temp.setUserObject(tmp);
		    						          ((DefaultTreeModel) jtree.getModel()).insertNodeInto(temp, node, node.getChildCount());
		    						          */

								JOptionPane.showMessageDialog(mainPanel,"创建文件夹："+ foldename+" 成功");
								updateTree20();//上面那个也可以
							}
						}

						else if(type==1){//云端目录


							System.out.println("创建云端目录文件夹");
							try {
								HttpClient client = new DefaultHttpClient();//HttpClient不支持流输出
								List<NameValuePair> formparams = new ArrayList<NameValuePair>();
								formparams.add(new BasicNameValuePair("username", LoginView.username));
								formparams.add(new BasicNameValuePair("userrole", "1"));
								formparams.add(new BasicNameValuePair("path",parentpath));//父目录的路径
								formparams.add(new BasicNameValuePair("name",foldename));


								UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");//设置编码格式
								HttpPost httppost = new HttpPost(GlobalState.server+ "/submit3");//发起请求的网址,即   "http://202.117.10.253:8080/qcloud/login";
								httppost.setHeader("Cookie", LoginTask.cookies);
								httppost.setEntity(entity);
								HttpContext localContext = new BasicHttpContext();
								HttpResponse response = client.execute(httppost, localContext);
								int statusCode = response.getStatusLine().getStatusCode();
								if (statusCode == 200) {
									response.getEntity().getContent().close();
									JOptionPane.showMessageDialog(new JLabel(), "新建文件夹  "+ foldename+" 成功");

									updateTree();//这样会比较慢，但是可以避免同时选中 目录和子目录造成的双重上传
								}
								else{
									System.out.println(response.getStatusLine().getStatusCode());
									JOptionPane.showMessageDialog(new JLabel(), "服务器出错，请稍后再试");
								}
								response.getEntity().getContent().close();


							} catch (Exception ex) {
								JOptionPane.showMessageDialog(new JLabel(), "请检查网络连接是否正常");
								ex.printStackTrace();
							}

						}



					} catch (Exception ex) {
						tag=false;
						JOptionPane.showMessageDialog(mainPanel,"创建文件夹异常");
						ex.printStackTrace();
					}

				}
			});
			//取消按钮
			cancelBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jframe.dispose();
					tag=true;
				}
			});

			return tag;

		}



	}


	//创建文件夹
	class Rename{
		//通讯相关参数
		private JPanel mainPanel;


		JFrame jframe;
		JTextField jfoldename;
		JButton sureBtn;
		JButton cancelBtn;
		JPanel btnPanel;	//面板
		Box box;	//盒子
		int type;//本地创建还是云端创建
		//信息输入框
		JLabel foldenameLabel;
		DefaultMutableTreeNode node;
		DefaultMutableTreeNode parentNode;
		JTree jtree;
		boolean tag;
		String oldname;
		String mypath;
		boolean isfile;
		public Rename(int type,DefaultMutableTreeNode node,JTree jtree,String oldname,String mypath,boolean isfile){
			//this.mainPanel=new JPanel();
			this.type = type;
			this.node =node;
			this.jtree=jtree;
			this.oldname=oldname;
			this.mypath=mypath;
			this.isfile=isfile;

		}

		public Rename(int type,DefaultMutableTreeNode node,DefaultMutableTreeNode parentNode,JTree jtree,String oldname,String mypath){
			//this.mainPanel=new JPanel();
			this.type = type;
			this.node =node;
			this.jtree=jtree;
			this.oldname=oldname;
			this.mypath=mypath;
			this.parentNode=parentNode;

		}


		public boolean rename() {

			jframe=new JFrame();
			jframe.setTitle("重命名");
			jframe.setSize(400, 120);
			btnPanel = new JPanel();
			box = Box.createVerticalBox();
			JLabel oldnamelab=new JLabel();
			oldnamelab.setText("原文件(夹)名："+oldname);
			foldenameLabel = new JLabel("新的文件(夹)名:");
			jfoldename = new JTextField(20);
			//按钮
			sureBtn=new JButton("确    定");
			cancelBtn = new JButton("取     消");

			btnPanel.add(sureBtn);
			btnPanel.add(cancelBtn);
			JPanel jptmp=new JPanel();
			jptmp.add(oldnamelab);
			//添加到盒子
			box.add(jptmp);
			box.add(new InputPanel(foldenameLabel,jfoldename));
			box.add(btnPanel);

			//把面板添加到组件
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	//获取屏幕大小,定位窗体
			jframe.add(box);
			jframe.setResizable(false);
			jframe.setLocation((int)((screenSize.getWidth()-jframe.getWidth())/2),(int)(screenSize.getHeight()-jframe.getHeight())/2);
			jframe.setVisible(true);

			//确认按钮
			sureBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {


					try {


						String foldename=jfoldename.getText().toString().trim();
						if(foldename==null || foldename.equals("")){
							JOptionPane.showMessageDialog(mainPanel,"新文件(夹)名不能为空");
							return;
						}

						if(type==0){//本地重命名


							if(isfile){//是文件
								String realyname=oldname;
								if(oldname.contains(".")){
									realyname=oldname.substring(0,oldname.lastIndexOf("."));
								}
								if(foldename.equals(realyname)){
									JOptionPane.showMessageDialog(mainPanel,"与原文件同名，请重新输入新名称");
									return;
								}
							}
							else{//是文件夹
								if(foldename.equals(oldname)){
									JOptionPane.showMessageDialog(mainPanel,"与原文件夹同名，请重新输入新名称");
									return;
								}
							}


							String parentpath=mypath.substring(0,mypath.lastIndexOf("/"));

	    							 /*//判断重命名的文件是否存在的一种方法
	    							 File filetmp=new File(parentpath);
    								 String[] filelist=filetmp.list();

	    							 if(isfile){//是文件
	    								 String houzhui="";
	    								 if(oldname.contains("."))
	    									 houzhui=oldname.substring(oldname.lastIndexOf("."));

	    								 foldename=foldename+houzhui;
	    								 for(int i=0;i<filelist.length;i++){
		    								if(filelist[i].equals(foldename+houzhui)){
		    									JOptionPane.showMessageDialog(mainPanel,"与原文件同名，请重新输入新名称");
		    									return;
		    								}
	    								 }

	    							 }
	    							 else{//是文件夹
	    								for(int i=0;i<filelist.length;i++){
	    									if(filelist[i].equals(foldename)){
	    										 JOptionPane.showMessageDialog(mainPanel,"已经存在同名的文件夹，请重新输入新名称");
			    								 return;
	    									}
	    								}
	    							 }*/


							if(isfile){//是文件
								String houzhui="";
								if(oldname.contains("."))
									houzhui=oldname.substring(oldname.lastIndexOf("."));

								foldename=foldename+houzhui;
							}
							// System.out.println("可以修改 ");
							//可以创建


							File newfile=new File(parentpath +"/"+foldename);
							File oldfile=new File(parentpath +"/"+oldname);
							if(!oldfile.exists()){
								JOptionPane.showMessageDialog(mainPanel,"原文件（夹）"+oldfile+"不存在");
							}
							else{
								if(newfile.exists()){
									JOptionPane.showMessageDialog(mainPanel,"已经存在同名的文件(夹)，请重新输入新名称");
								}
								else{
									oldfile.renameTo(newfile);
									JOptionPane.showMessageDialog(mainPanel,"原文件（夹）"+ oldname+" 重命名为 "+foldename);
									updateTree20();//上面那个也可以
								}
							}
						}
						else {//云端重命名
							String realyname=oldname;
							if(oldname.contains(".")){
								realyname=oldname.substring(0,oldname.lastIndexOf("."));
							}
							if(foldename.equals(realyname)){
								JOptionPane.showMessageDialog(mainPanel,"与原文件同名，请重新输入新名称");
								return;
							}

							String houzhui="";
							if(oldname.contains("."))
								houzhui=oldname.substring(oldname.lastIndexOf("."));

							foldename=foldename+houzhui;




							int childCount=parentNode.getChildCount();
							String parentpath=mypath.substring(0,mypath.lastIndexOf("/"));
							for(int i=0;i<childCount;i++){
								DefaultMutableTreeNode child=(DefaultMutableTreeNode) parentNode.getChildAt(i);
								FileBean fb=(FileBean) child.getUserObject();
								String childfilename=fb.getName();
								if(childfilename.equals(foldename)){
									JOptionPane.showMessageDialog(mainPanel,"已经存在同名的文件，请重新输入新文件名");
									return;
								}
							}

							HttpClient client = new DefaultHttpClient();//HttpClient不支持流输出
							List<NameValuePair> formparams = new ArrayList<NameValuePair>();
							formparams.add(new BasicNameValuePair("username", LoginView.username));
							formparams.add(new BasicNameValuePair("parentpath",parentpath));//父目录的路径
							formparams.add(new BasicNameValuePair("oldname",oldname));//父目录的路径
							formparams.add(new BasicNameValuePair("newname",foldename));


							UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");//设置编码格式
							HttpPost httppost = new HttpPost(GlobalState.server+ "/changefilename");//发起请求的网址,即   "http://202.117.10.253:8080/qcloud/login";
							httppost.setHeader("Cookie", LoginTask.cookies);
							httppost.setEntity(entity);
							HttpContext localContext = new BasicHttpContext();
							HttpResponse response = client.execute(httppost, localContext);
							int statusCode = response.getStatusLine().getStatusCode();
							if (statusCode == 200) {
								response.getEntity().getContent().close();
								JOptionPane.showMessageDialog(new JLabel(), "修改文件名  "+ oldname+" 为 "+ foldename +" 成功");

								updateTree10();//这样会比较慢，但是可以避免同时选中 目录和子目录造成的双重上传
							}
							else{
								//System.out.println(response.getStatusLine().getStatusCode());
								JOptionPane.showMessageDialog(new JLabel(), "服务器出错，请稍后再试");
							}
							response.getEntity().getContent().close();


						}

					} catch (Exception ex) {
						tag=false;
						JOptionPane.showMessageDialog(mainPanel,"重命名异常");
						ex.printStackTrace();
					}

				}
			});
			//取消按钮
			cancelBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jframe.dispose();
					tag=true;
				}
			});

			return tag;

		}
	}

	//信息输入面板
	@SuppressWarnings("serial")
	class InputPanel extends JPanel{
		public InputPanel(JLabel label,JTextField textField){
			this.add(label);
			this.add(textField);
		}
		public InputPanel(JLabel label1,JTextField textField1,JLabel label2,JTextField textField2){
			this.setPreferredSize(new Dimension(390,100));
			this.add(label1);
			this.add(textField1);
			this.add(label2);
			this.add(textField2);
		}
	}




}
