package serverapp.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.log4j.Logger;

/**
 * @author Tom
 *
 * @created on 6 gru 2015
 *
 */

public class DiscoveryThread implements Runnable {

	private static final Logger log = Logger.getLogger(DiscoveryThread.class.getName());
	
	
	DatagramSocket socket;
	@Override
	public void run() {
		try {
			//creating socket
			socket = new DatagramSocket(8888, LocalHost.getInstance().getHostAddress());
			log.info("Server listening on: "+LocalHost.getInstance().getHostAddress().toString());
			socket.setBroadcast(true);
			
			while(true){
				log.info("Server ready to receive broadcast packets");
				
				//receiving packet
				byte[] recvBuf = new byte[150000];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet);
				
				//Packet received
				log.info("Discovery received from: "+ packet.getAddress().getHostAddress());
				
				//Check packet
				String message = new String(packet.getData()).trim();
				if(message.equals("DISCOVER_REQUEST")){
					byte[] sendData = "DISCOVER_RESPONSE".getBytes();
				
					//respond
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
					socket.send(sendPacket);
					
					log.info("Response send to host: "+sendPacket.getAddress().getHostAddress());			
				}
				
			}
		} catch (Exception e) {
			log.info("Server error: " + e.getLocalizedMessage());
		}
		
	}
	
	public static DiscoveryThread getInstance(){
		return DiscoveryThreadHolder.INSTANCE;
	}
	
	private static class DiscoveryThreadHolder{
		private static final DiscoveryThread INSTANCE = new DiscoveryThread();
	}

}
