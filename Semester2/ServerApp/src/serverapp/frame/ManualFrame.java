package serverapp.frame;

import java.awt.Color;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;

public class ManualFrame extends JFrame implements ActionListener {
	
	private JPanel  contentPane;
	
	private JButton btnClose;
	private JButton btnNextSlide;
	private JButton btnPreviousSlide;
	private JButton btnTest;
	
	private JLabel  lblCurrentState;
	private JLabel  lblCurrentSlideNumber;
	private JLabel  lblCurrentNote;
	
	private JPanel mainPanel;
	
	private static final String WINDOW_NAME = "MyPrompter Manual";
	private static final String IMAGE_PATH = "res/ic_launcher.png";
	private static final String BTN_CLOSE = "Close";
	private static final String BTN_NEXT = "Next";
	private static final String BTN_PREVIOUS = "Previous";
	private static final String BTN_TEST = "Test";
	private static final String LBL_STATE = "State:";
	private static final String LBL_SLIDENO = "Slide number:";
	private static final String LBL_NOTE = "Note:";
	
	private static final String STATE_RUNNING = "Presentation on";
	private static final String STATE_NOTRUNNING = "Presentation off";
	Automation auto;
	
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
		
		btnTest = new JButton(BTN_TEST);
		btnTest.setBounds(10, 228, 89, 23);
		btnTest.addActionListener(this);
		contentPane.add(btnTest);
		
		btnNextSlide = new JButton(BTN_NEXT);
		btnNextSlide.setBounds(208, 228, 89, 23);
		btnNextSlide.addActionListener(this);
		contentPane.add(btnNextSlide);
		
		btnPreviousSlide = new JButton(BTN_PREVIOUS);
		btnPreviousSlide.setBounds(109, 228, 89, 23);
		btnPreviousSlide.addActionListener(this);
		contentPane.add(btnPreviousSlide);
		
		btnClose = new JButton(BTN_CLOSE);
		btnClose.setBounds(307, 228, 89, 23);
		btnClose.addActionListener(this);
		contentPane.add(btnClose);
		
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
		lblCurrentSlideNumber.setBounds(119, 36, 129, 14);
		mainPanel.add(lblCurrentSlideNumber);
		
		lblCurrentNote = new JLabel();
		lblCurrentNote.setBounds(119, 61, 129, 14);
		mainPanel.add(lblCurrentNote);
		
	}
	
	private void initStartSettings() {
		auto = new Automation();
		lblCurrentState.setText(STATE_NOTRUNNING);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object btnClicked = e.getSource();
			if(btnClicked == btnTest) {
				test();
			} else if (btnClicked == btnNextSlide) {
				nextSlide();
			} else if (btnClicked == btnPreviousSlide) {
				previousSlide();
			} else if (btnClicked == btnClose){
				close();
			}
	}
	
	private void close(){
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	private void nextSlide(){
		System.out.println("Next slide not implemented yet");
	}
	
	private void test(){
		//Automation auto = new Automation();
		if(auto.checkActive()==true){
			lblCurrentState.setText(STATE_RUNNING);
			System.out.println("Presentation is running.");
		} else if (auto.checkActive()==false){
			lblCurrentState.setText(STATE_NOTRUNNING);
		}
	}
	
	private void previousSlide(){
		System.out.println("Previous slide not implemented yet");
	}

}
