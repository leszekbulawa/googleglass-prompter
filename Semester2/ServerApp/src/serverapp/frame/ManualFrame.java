package serverapp.frame;

import java.awt.Color;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import org.apache.log4j.Logger;
import javax.swing.SwingConstants;

public class ManualFrame extends JFrame implements ActionListener {
	
	private JPanel  contentPane;
	
	private JButton btnClose;
	private JButton btnNotes;
	private JButton btnSlides;
	private JButton btnOpen;
	private JButton btnPrevious;
	private JButton btnNext;
	
	private JLabel  lblCurrentState;
	private JLabel  lblCurrentSlideNumber;
	private JLabel  lblCurrentNote;
	
	private JPanel mainPanel;
	
	private File file;
	private Desktop desktop;
	
	private static final String WINDOW_NAME = "MyPrompter Manual";
	private static final String IMAGE_PATH = "res/ic_launcher.png";
	private static final String BTN_CLOSE = "Close";
	private static final String BTN_NOTES = "Notes";
	private static final String BTN_SLIDES = "Slides";
	private static final String BTN_OPEN = "Open";
	private static final String LBL_STATE = "State:";
	private static final String LBL_SLIDENO = "Slide number:";
	private static final String LBL_NOTE = "Notes:";
	
	private static final String STATE_RUNNING = "Loaded";
	private static final String STATE_NOTRUNNING = "Not loaded";

	private Extractor ex;
	
	private static final Logger log = Logger.getLogger(ManualFrame.class.getName());
	
	private int slideNumber;
	private int slideAmount;
	private String [] notes;
	private String html1;
	private String html2;

	
	public ManualFrame(){
		initFrame();
	}
	
	private void initFrame(){
		
		setTitle(WINDOW_NAME);
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
		
		btnOpen = new JButton(BTN_OPEN);
		btnOpen.setBounds(10, 228, 89, 23);
		btnOpen.addActionListener(this);
		contentPane.add(btnOpen);
		
		btnNotes = new JButton(BTN_NOTES);
		btnNotes.setBounds(208, 228, 89, 23);
		btnNotes.addActionListener(this);
		contentPane.add(btnNotes);
		
		btnSlides = new JButton(BTN_SLIDES);
		btnSlides.setBounds(109, 228, 89, 23);
		btnSlides.addActionListener(this);
		contentPane.add(btnSlides);
		
		btnClose = new JButton(BTN_CLOSE);
		btnClose.setBounds(307, 228, 89, 23);
		btnClose.addActionListener(this);
		contentPane.add(btnClose);
		
		btnPrevious = new JButton("<");
		btnPrevious.setBounds(200, 32, 50, 20);
		btnPrevious.addActionListener(this);
		mainPanel.add(btnPrevious);
		
		btnNext = new JButton(">");
		btnNext.setBounds(260, 32, 50, 20);
		btnNext.addActionListener(this);
		mainPanel.add(btnNext);
		
	}
	
	private void createLabels(){
		JLabel lblStatus = new JLabel(LBL_STATE);
		lblStatus.setBounds(10, 11, 46, 14);
		mainPanel.add(lblStatus);
		
		JLabel lblSlideNumber = new JLabel(LBL_SLIDENO);
		lblSlideNumber.setBounds(10, 36, 99, 14);
		mainPanel.add(lblSlideNumber);
		
		JLabel lblNote = new JLabel(LBL_NOTE);
		lblNote.setBounds(10, 61, 99, 14);
		mainPanel.add(lblNote);
		
		lblCurrentState = new JLabel();
		lblCurrentState.setBounds(119, 11, 129, 14);
		mainPanel.add(lblCurrentState);
		
		lblCurrentSlideNumber = new JLabel();
		lblCurrentSlideNumber.setBounds(119, 36, 65, 14);
		mainPanel.add(lblCurrentSlideNumber);
		
		lblCurrentNote = new JLabel();
		lblCurrentNote.setVerticalAlignment(SwingConstants.TOP);
		lblCurrentNote.setBounds(119, 61, 260, 128);
		mainPanel.add(lblCurrentNote);
				
	}
	
	private void initStartSettings() {
		ex = new Extractor();
		lblCurrentState.setText(STATE_NOTRUNNING);
		lblCurrentNote.setText("Empty");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object btnClicked = e.getSource();
			if(btnClicked == btnOpen) {
				open();
			} else if (btnClicked == btnNotes) {
				note();
			} else if (btnClicked == btnSlides) {
				slide();
			} else if (btnClicked == btnClose){
				close();
			} else if (btnClicked == btnPrevious){
				previous();
			} else if (btnClicked == btnNext){
				next();
			}
	}
	
	private void close(){
		//this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		this.setVisible(false);
	}
		
	private void open(){
		
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
			ex.openPresentation(path);
			lblCurrentState.setText(STATE_RUNNING);
			
			desktop = Desktop.getDesktop();
			file = new File(input);
			try {
				desktop.open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void slide(){
		ex.getSlides();
		slideNumber = 0;
		slideAmount = ex.getSlides();
		lblCurrentSlideNumber.setText(""+ slideNumber + "/"+ slideAmount);
		html1 = "<html><body style='width: 200 px'>";
		html2 = "px'>";
	}
	
	private void note(){
		slideNumber = 1;
		if(slideAmount!=0){
			ex.getNotes();
			notes = ex.getNotes();
			lblCurrentSlideNumber.setText(""+ slideNumber + "/"+ slideAmount);
			lblCurrentNote.setText(notes[slideNumber-1]);
		}
	}
	
	private void previous(){
		if (slideNumber>1){
			slideNumber = slideNumber - 1;
		}
		lblCurrentSlideNumber.setText(""+ slideNumber + "/"+ slideAmount);
		lblCurrentNote.setText(html1 + notes[slideNumber-1] + "</html>");
	}
	
	private void next(){
		if (slideNumber<slideAmount){
			slideNumber = slideNumber + 1;
		}
		lblCurrentSlideNumber.setText(""+ slideNumber + "/"+ slideAmount);
		lblCurrentNote.setText(html1+ notes[slideNumber-1] + "</html>");
	}
}
