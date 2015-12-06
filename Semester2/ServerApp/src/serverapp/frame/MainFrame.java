package serverapp.frame;

import java.awt.Color;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;

import serverapp.server.DiscoveryThread;
import serverapp.server.LocalHost;

public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel  contentPane;
	private JButton btnStartServer;
	private JButton btnStopServer;
	private JButton btnMinimizeServer;
	private JButton btnExit;
	private JButton btnOpenLogFile;
	
	private JLabel  lblCurrentStatus;
	private JLabel  lblCurrentCompName;
	private JLabel  lblCurrentIP;
	
	private JPanel mainPanel;
	
	private static final String APPLICATION_NAME = "MyPrompter Server";
	private static final String IMAGE_PATH = "res/ic_launcher.png";
	private static final String STATUS_RUNNING = "Running";
	private static final String STATUS_STOPPED = "Stopped";
	private static final String BTN_START = "Start";
	private static final String BTN_STOP = "Stop";
	private static final String BTN_MINIMIZE = "Minimize";
	private static final String BTN_EXIT = "Exit";
	private static final String BTN_WIFI = "Wifi";
	private static final String BTN_BT = "Bluetooth";
	private static final String BTN_LOG = "Open log file";
	private static final String LBL_STATUS = "Status:";
	private static final String LBL_COMPUTER_NAME = "Computer Name:";
	private static final String LBL_IP_ADDRESS = "IP Address:";
	private static final String LBL_CONNECTION_TYPE = "Connection Type:";
	
	private static final Logger log = Logger.getLogger(MainFrame.class.getName());
	

	public MainFrame() {
		initFrame();

	}

	private void initFrame() {
		setTitle(APPLICATION_NAME);
		setIconImage(Toolkit.getDefaultToolkit().getImage(IMAGE_PATH));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 300);
		setLocationRelativeTo(null);
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		mainPanel = new JPanel();
		mainPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.BLACK, Color.GRAY));
		mainPanel.setBounds(10, 11, 384, 200);
		contentPane.add(mainPanel);
		mainPanel.setLayout(null);
		
		createButtons();	
		createLabels();
		initStartSettings();
	}

	private void createButtons(){
		btnStartServer = new JButton(BTN_START);
		btnStartServer.setBounds(10, 228, 89, 23);
		btnStartServer.addActionListener(this);
		contentPane.add(btnStartServer);
		
		btnStopServer = new JButton(BTN_STOP);
		btnStopServer.setBounds(109, 228, 89, 23);
		btnStopServer.addActionListener(this);
		contentPane.add(btnStopServer);
		
		btnMinimizeServer = new JButton(BTN_MINIMIZE);
		btnMinimizeServer.setBounds(208, 228, 89, 23);
		btnMinimizeServer.addActionListener(this);
		contentPane.add(btnMinimizeServer);
		
		btnExit = new JButton(BTN_EXIT);
		btnExit.setBounds(305, 228, 89, 23);
		btnExit.addActionListener(this);
		contentPane.add(btnExit);
		
		btnOpenLogFile = new JButton(BTN_LOG);
		btnOpenLogFile.setBounds(10, 166, 99, 23);
		btnOpenLogFile.addActionListener(this);
		mainPanel.add(btnOpenLogFile);
		
		JRadioButton rdbtnWifi = new JRadioButton(BTN_WIFI);
		rdbtnWifi.setBounds(115, 82, 50, 23);
		mainPanel.add(rdbtnWifi);
		
		JRadioButton rdbtnBluetooth = new JRadioButton(BTN_BT);
		rdbtnBluetooth.setBounds(167, 82, 81, 23);
		mainPanel.add(rdbtnBluetooth);
		
		ButtonGroup radioBtnGroup = new ButtonGroup();
		radioBtnGroup.add(rdbtnWifi);
		radioBtnGroup.add(rdbtnBluetooth);	
		
	}
	
	private void createLabels(){
		JLabel lblStatus = new JLabel(LBL_STATUS);
		lblStatus.setBounds(10, 11, 46, 14);
		mainPanel.add(lblStatus);
		
		JLabel lblCompName = new JLabel(LBL_COMPUTER_NAME);
		lblCompName.setBounds(10, 36, 99, 14);
		mainPanel.add(lblCompName);
		
		JLabel lblIPAddress = new JLabel(LBL_IP_ADDRESS);
		lblIPAddress.setBounds(10, 61, 99, 14);
		mainPanel.add(lblIPAddress);
		
		JLabel lblConnectionType = new JLabel(LBL_CONNECTION_TYPE);
		lblConnectionType.setBounds(10, 86, 99, 14);
		mainPanel.add(lblConnectionType);
		
		lblCurrentStatus = new JLabel();
		lblCurrentStatus.setBounds(119, 11, 129, 14);
		mainPanel.add(lblCurrentStatus);
		
		lblCurrentCompName = new JLabel();
		lblCurrentCompName.setBounds(119, 36, 129, 14);
		mainPanel.add(lblCurrentCompName);
		
		lblCurrentIP = new JLabel();
		lblCurrentIP.setBounds(119, 61, 129, 14);
		mainPanel.add(lblCurrentIP);
	}

	private void initStartSettings() {	
		lblCurrentStatus.setText(STATUS_STOPPED);
		btnStopServer.setEnabled(false);	
		
		if(!SystemTray.isSupported()){
			btnMinimizeServer.setEnabled(false);
		}
		setIpAndHostName();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object btnClicked = e.getSource();
			if(btnClicked == btnStartServer) {
				startServer();
			} else if (btnClicked == btnStopServer) {
				stopServer();
			} else if (btnClicked == btnMinimizeServer) {
				minimizeFrame();
			} else if (btnClicked == btnExit) {
				exitServer();
			} else if (btnClicked == btnOpenLogFile) {
				openLog();
			}
	}

	private void startServer() {
		lblCurrentStatus.setText(STATUS_RUNNING);
		Thread discThread = new Thread(DiscoveryThread.getInstance());
		discThread.start();
		btnStopServer.setEnabled(true);
		btnStartServer.setEnabled(false);
	}
	
	private void stopServer() {
		lblCurrentStatus.setText(STATUS_STOPPED);
		btnStopServer.setEnabled(false);
		btnStartServer.setEnabled(true);
	}
	
	private void minimizeFrame() {
		if(!SystemTray.isSupported()){
			btnMinimizeServer.setEnabled(false);
		}
	}
	
	private void exitServer(){
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	private void openLog(){
		ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "log/log.out");
		try {
			pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setIpAndHostName() {
		LocalHost localHost = LocalHost.getInstance();
		lblCurrentCompName.setText(localHost.getHostName());
		log.info("Host name: "+lblCurrentCompName.getText());
		lblCurrentIP.setText(localHost.getHostIPString());
		log.info("Host IP: "+lblCurrentIP.getText());
	}
	
}
