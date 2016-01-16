package serverapp.frame;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.apache.log4j.Logger;

/**
 * @author Tom
 *
 * @created on 6 gru 2015
 *
 */
public class HideFrame implements ActionListener, MouseListener {

	private TrayIcon trayIcon;
	private SystemTray systemTray;
	private MainFrame frame;
	private PopupMenu popUp;
	private static final Logger log = Logger.getLogger(HideFrame.class.getName());
	private MenuItem enableTimer;
	private MenuItem resetTimerItem;
	
	public HideFrame(MainFrame frame) {
		this.frame = frame;	
		createPopUpMenu();
		
		systemTray=SystemTray.getSystemTray();
		
		trayIcon = new TrayIcon(this.frame.getIconImage(), "Tooltip", popUp);
		trayIcon.addMouseListener(this);
		trayIcon.setImageAutoSize(true);
		
		try {
			systemTray.add(trayIcon);
		} catch (AWTException e) {
			log.error("System tray error: "+e.getMessage());
		}		
	}
	
	private void createPopUpMenu(){
		popUp = new PopupMenu();
		
		MenuItem openMenuItem = new MenuItem("Open");
		openMenuItem.addActionListener(this);
		popUp.add(openMenuItem);
		
		popUp.addSeparator();	
		MenuItem font_1 = new MenuItem("Arial 16");
		font_1.addActionListener(this);
		popUp.add(font_1);
		
		MenuItem font_2 = new MenuItem("Arial 18");
		font_2.addActionListener(this);
		popUp.add(font_2);
		
		MenuItem font_3 = new MenuItem("Calibri 18");
		font_3.addActionListener(this);
		popUp.add(font_3);
		popUp.addSeparator();	
		
		enableTimer = new MenuItem("");
		enableTimer.addActionListener(this);
		popUp.add(enableTimer);
		popUp.addSeparator();
		
		resetTimerItem = new MenuItem("Reset Timer");
		resetTimerItem.addActionListener(this);
		popUp.add(resetTimerItem);
		popUp.addSeparator();
		
		if(frame.isTimerEnabled()){
			enableTimer.setLabel("Disable Timer");
			resetTimerItem.setEnabled(true);
		} else {
			enableTimer.setLabel("Enable Timer");
			resetTimerItem.setEnabled(false);
		}
		
		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.addActionListener(this);		
		popUp.add(exitMenuItem);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String itemClicket = e.getActionCommand();
		if(itemClicket == "Open") {
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
			systemTray.remove(trayIcon);
		} else if (itemClicket == "Arial 16") {
			frame.clickButton(itemClicket);
		} else if (itemClicket == "Arial 18") {
			frame.clickButton(itemClicket);
		} else if (itemClicket == "Calibri 18") {
			frame.clickButton(itemClicket);
		} else if (itemClicket == "Enable Timer") {
			enableTimer.setLabel("Disable Timer");
			resetTimerItem.setEnabled(true);
			frame.clickButton("Enabled");
		} else if (itemClicket == "Disable Timer") {
			enableTimer.setLabel("Enable Timer");
			resetTimerItem.setEnabled(false);
			frame.clickButton("Enabled");
		} else if (itemClicket == "Reset Timer") {
			frame.clickButton("Reset");
		} else if (itemClicket == "Exit") {
			systemTray.remove(trayIcon);
			System.exit(0);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2){
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
			systemTray.remove(trayIcon);
		}	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}	
}