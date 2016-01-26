package serverapp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class FirewallException {

	private Logger log = Logger.getLogger(DiscoveryThread.class.getName());
	private static String ADD_TCP_COMMAND = "netsh advfirewall firewall add rule name=\"Open Port 8888\" dir=in action=allow protocol=TCP localport=8888";
	private static String ADD_UDP_COMMAND = "netsh advfirewall firewall add rule name=\"Open Port 8888\" dir=in action=allow protocol=UDP localport=8888";
	private static String REMOVE_TCP_COMMAND = "netsh advfirewall firewall delete rule name=\"Open Port 8888\" dir=in protocol=TCP localport=8888";
	private static String REMOVE_UDP_COMMAND = "netsh advfirewall firewall delete rule name=\"Open Port 8888\" dir=in protocol=UDP localport=8888";
	
	public FirewallException() {
		
	}
	
	public void addUDPException(){
		try {
			Process p = Runtime.getRuntime().exec(ADD_UDP_COMMAND);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = reader.readLine();
			while (line != null) {
				log.info(line);
			}
			

		} catch (IOException e1) {
			log.error(e1.getMessage());
			
		} catch (InterruptedException e2) {
			log.error(e2.getMessage());
		}
	}
	
	public void addTCPException(){
		try {
			Process p = Runtime.getRuntime().exec(ADD_TCP_COMMAND);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = reader.readLine();
			while (line != null) {
				log.info(line);
			}
			

		} catch (IOException e1) {
			log.error(e1.getMessage());
			
		} catch (InterruptedException e2) {
			log.error(e2.getMessage());
		}
	}
	
	public void removeUDPException(){
		try {
			Process p = Runtime.getRuntime().exec(REMOVE_UDP_COMMAND);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = reader.readLine();
			while (line != null) {
				log.info(line);
			}

		} catch (IOException e1) {
			log.error(e1.getMessage());
			
		} catch (InterruptedException e2) {
			log.error(e2.getMessage());
		}
	}
	
	public void removeTCPException(){
		try {
			Process p = Runtime.getRuntime().exec(REMOVE_TCP_COMMAND);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = reader.readLine();
			while (line != null) {
				log.info(line);
			}

		} catch (IOException e1) {
			log.error(e1.getMessage());
			
		} catch (InterruptedException e2) {
			log.error(e2.getMessage());
		}
	}

}
