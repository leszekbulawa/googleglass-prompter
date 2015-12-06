package serverapp.frame;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.UIManager;

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