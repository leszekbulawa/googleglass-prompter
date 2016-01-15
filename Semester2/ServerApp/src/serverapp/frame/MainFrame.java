package serverapp.frame;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import serverapp.server.DiscoveryThread;
import serverapp.server.LocalHost;
import javax.swing.JCheckBox;

public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel  contentPane;
	private JButton btnStartServer;
	private JButton btnStopServer;
	private JButton btnMinimizeServer;
	private JButton btnExit;
	private JButton btnOpenLogFile;
	private JButton btnClearLogFile;
	private JButton btnOpenPresentation;
	private JButton btnManual;
	private JButton btnResetTimer;
	
	private JRadioButton arial16RadioBtn;
	private JRadioButton arial18RadioBtn;
	private JRadioButton calibri18RadioBtn;
	private JCheckBox timerEnabled;
	
	private JLabel  lblCurrentStatus;
	private JLabel  lblCurrentCompName;
	private JLabel  lblCurrentIP;
	private ButtonGroup btnFontGroup;
	
	private JPanel mainPanel;
	
	private static final String APPLICATION_NAME = "MyPrompter Server";
	private static final String IMAGE_PATH = "res/ic_launcher.png";
	private static final String STATUS_RUNNING = "Running";
	private static final String STATUS_STOPPED = "Stopped";
	private static final String BTN_START = "Start";
	private static final String BTN_STOP = "Stop";
	private static final String BTN_MINIMIZE = "Minimize";
	private static final String BTN_EXIT = "Exit";
	private static final String BTN_LOG = "Open log";
	private static final String BTN_CLEAR_LOG = "Clear log";
	private static final String BTN_MANUAL = "Manual"; //change
	private static final String BTN_OPEN = "Open presentation";
	private static final String LBL_STATUS = "Status:";
	private static final String LBL_COMPUTER_NAME = "Computer Name:";
	private static final String LBL_IP_ADDRESS = "IP Address:";
	private static final String LBL_CONNECTION_TYPE = "Connection Type:";
	private static final String LBL_WIFI = "Wifi";
	
	private static final Logger log = Logger.getLogger(MainFrame.class.getName());
	private HideFrame hideFrame;
	private ManualFrame manualFrame;
	private Extractor extractor;
	private File file;
	private Desktop desktop;
	
	DiscoveryThread discThread;
	private String[] notes;
	private JPanel timerPanel;
	
	public MainFrame() {
		discThread = DiscoveryThread.getInstance();
		discThread.getFrame(this);	
		initFrame();
	}

	private void initFrame() {
		setTitle(APPLICATION_NAME);
		setIconImage(Toolkit.getDefaultToolkit().getImage(IMAGE_PATH));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 413, 310);
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		mainPanel = new JPanel();
		mainPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.BLACK, Color.GRAY));
		mainPanel.setBounds(10, 11, 234, 231);
		contentPane.add(mainPanel);
		mainPanel.setLayout(null);
		
		btnFontGroup = new ButtonGroup();
		
		createButtons();	
		createLabels();
		initStartSettings();
	}

	private void createButtons(){
		btnStartServer = new JButton(BTN_START);
		btnStartServer.setBounds(10, 253, 89, 23);
		btnStartServer.addActionListener(this);
		contentPane.add(btnStartServer);
		
		btnStopServer = new JButton(BTN_STOP);
		btnStopServer.setBounds(109, 253, 89, 23);
		btnStopServer.addActionListener(this);
		contentPane.add(btnStopServer);
		
		btnMinimizeServer = new JButton(BTN_MINIMIZE);
		btnMinimizeServer.setBounds(208, 253, 89, 23);
		btnMinimizeServer.addActionListener(this);
		contentPane.add(btnMinimizeServer);
		
		btnExit = new JButton(BTN_EXIT);
		btnExit.setBounds(307, 253, 89, 23);
		btnExit.addActionListener(this);
		contentPane.add(btnExit);
		
		btnOpenLogFile = new JButton(BTN_LOG);
		btnOpenLogFile.setBounds(10, 163, 100, 23);
		btnOpenLogFile.addActionListener(this);
		mainPanel.add(btnOpenLogFile);
		
		btnClearLogFile = new JButton(BTN_CLEAR_LOG);
		btnClearLogFile.setBounds(120, 163, 100, 23);
		btnClearLogFile.addActionListener(this);
		mainPanel.add(btnClearLogFile);	
		
		btnOpenPresentation = new JButton(BTN_OPEN);
		btnOpenPresentation.setBounds(10, 129, 210, 23);
		btnOpenPresentation.addActionListener(this);
		btnOpenPresentation.setEnabled(false);
		mainPanel.add(btnOpenPresentation);
		
		JPanel panel = new JPanel();
		panel.setBounds(254, 11, 125, 114);
		contentPane.add(panel);
		panel.setLayout(null);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.BLACK, Color.GRAY));
		
		arial16RadioBtn = new JRadioButton("Arial 16");
		arial16RadioBtn.setSelected(true);
		arial16RadioBtn.setBounds(9, 32, 101, 23);
		arial16RadioBtn.addActionListener(this);
		btnFontGroup.add(arial16RadioBtn);
		panel.add(arial16RadioBtn);
		
		arial18RadioBtn = new JRadioButton("Arial 18");
		arial18RadioBtn.setBounds(9, 58, 101, 23);
		arial18RadioBtn.addActionListener(this);
		btnFontGroup.add(arial18RadioBtn);
		panel.add(arial18RadioBtn);
		
		calibri18RadioBtn = new JRadioButton("Calibri 18");
		calibri18RadioBtn.setBounds(9, 84, 101, 23);
		calibri18RadioBtn.addActionListener(this);
		btnFontGroup.add(calibri18RadioBtn);
		panel.add(calibri18RadioBtn);
		
		JLabel setFontLbl = new JLabel();
		setFontLbl.setText("Set Font:");
		setFontLbl.setBounds(9, 11, 101, 14);
		panel.add(setFontLbl);
		
		timerPanel = new JPanel();
		timerPanel.setLayout(null);
		timerPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.BLACK, Color.GRAY));
		timerPanel.setBounds(254, 136, 125, 106);
		contentPane.add(timerPanel);
		
		timerEnabled = new JCheckBox("Enabled");
		timerEnabled.setBounds(10, 38, 97, 23);
		timerPanel.add(timerEnabled);
		timerEnabled.setSelected(true);
		
		btnResetTimer = new JButton("Reset");
		btnResetTimer.setBounds(10, 72, 93, 23);
		timerPanel.add(btnResetTimer);
		
		JLabel timerLbl = new JLabel();
		timerLbl.setBounds(10, 11, 101, 14);
		timerPanel.add(timerLbl);
		timerLbl.setText("Timer:");
		btnResetTimer.addActionListener(this);
		timerEnabled.addActionListener(this);
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
		
		JLabel lblWifi = new JLabel(LBL_WIFI);
		lblWifi.setBounds(119, 86, 129, 14);
		mainPanel.add(lblWifi);
		
		lblCurrentStatus = new JLabel();
		lblCurrentStatus.setBounds(119, 11, 101, 14);
		mainPanel.add(lblCurrentStatus);
		
		lblCurrentCompName = new JLabel();
		lblCurrentCompName.setBounds(119, 36, 129, 14);
		mainPanel.add(lblCurrentCompName);
		
		lblCurrentIP = new JLabel();
		lblCurrentIP.setBounds(119, 61, 129, 14);
		mainPanel.add(lblCurrentIP);
		
		btnManual = new JButton("Manual");
		btnManual.setBounds(10, 197, 210, 23);
		mainPanel.add(btnManual);
		btnManual.addActionListener(this);		
	}

	private void initStartSettings() {	
		lblCurrentStatus.setText(STATUS_STOPPED);
		btnStopServer.setEnabled(false);	
		extractor = new Extractor();
		manualFrame = new ManualFrame();

		if(!SystemTray.isSupported()){
			btnMinimizeServer.setEnabled(false);
			log.info("System tray is not supported!");
		} else {
			log.info("System tray supported!");
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
			} else if (btnClicked == btnClearLogFile) {
				clearLog();
			} else if (btnClicked == btnManual){
				manual();
			} else if (btnClicked == btnOpenPresentation){
				openPresentation();
			} else if (btnClicked == arial16RadioBtn){
				if(discThread.isConnected())
					discThread.sendFont(getCurrentFont());
			} else if (btnClicked == arial18RadioBtn){
				if(discThread.isConnected())
					discThread.sendFont(getCurrentFont());
			} else if (btnClicked == calibri18RadioBtn){
				if(discThread.isConnected())
					discThread.sendFont(getCurrentFont());
			}
	}

	private void startServer() {
		lblCurrentStatus.setText(STATUS_RUNNING);
		Thread thread = new Thread(discThread);
		thread.start();
		btnStopServer.setEnabled(true);
		btnStartServer.setEnabled(false);
	}
	
	private void stopServer() {
		lblCurrentStatus.setText(STATUS_STOPPED);
		discThread.stopListening();
		btnStopServer.setEnabled(false);
		btnStartServer.setEnabled(true);
	}
	
	private void minimizeFrame() {
		hideFrame = new HideFrame(this);
		this.setVisible(false);
	}
	
	private void exitServer(){
		discThread.stopListening();
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	private void openLog(){
		ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "log/log.out");
		try {
			pb.start();
		} catch (IOException e) {
			log.error("Open log error: "+e.getMessage());
		}
	}
	
	private void clearLog(){
		try {
			FileOutputStream writer = new FileOutputStream("log/log.out");
			writer.close();
		} catch (IOException e) {
			log.error("Could not find log file: " + e.getMessage());
		}	
	}
	
	private void manual(){
		manualFrame.setVisible(true);
	}
	
	private void openPresentation(){
		String path = null;
		String input = null;
		int ret;

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File(""));
		chooser.setDialogTitle("Select pptx presentation file");
		chooser.setFileFilter(new FileNameExtensionFilter("PPTX", "pptx"));
		
		ret = chooser.showOpenDialog(getParent());

		if(ret == JFileChooser.APPROVE_OPTION){
			input = chooser.getSelectedFile().getPath();
			System.out.println(input);
			path = input.replaceAll("\\\\", "\\\\\\\\");
			System.out.println(path);
			extractor.openPresentation(path);
			
			extractor.getSlides();
			notes = extractor.getNotes();
			
			discThread.sendFont(getCurrentFont());
			
			discThread.sendText(notes);
			
			desktop = Desktop.getDesktop();
			file = new File(input);
			try {
				desktop.open(file);
			} catch (IOException e) {
				e.printStackTrace();
				log.info(e.getMessage());
			}
		}
	}

	/**
	 * @return
	 */
	private boolean[] getCurrentFont() {
		boolean font[] = {arial16RadioBtn.isSelected(), arial18RadioBtn.isSelected(), calibri18RadioBtn.isSelected()};
		return font;
	}

	private void setIpAndHostName() {
		LocalHost localHost = LocalHost.getInstance();
		lblCurrentCompName.setText(localHost.getHostName());
		log.info("Host name: "+lblCurrentCompName.getText());
		lblCurrentIP.setText(localHost.getHostIPString());
		log.info("Host IP: "+lblCurrentIP.getText());
	}

	public void changeLabel(String str) {
		lblCurrentStatus.setText(str);	
		if(str.equalsIgnoreCase("Connected"))
			btnOpenPresentation.setEnabled(true);
	}
}

