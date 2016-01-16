package kainos.clientapp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.util.Enumeration;

import android.os.Handler;

public class DiscoveryThread implements Runnable {
	
	private final static String DISCOVER_REQUEST = "DISCOVER_REQUEST";
	private final static String DISCOVER_RESPONSE = "DISCOVER_RESPONSE";
	
	private DatagramSocket mDatagramSocket;
	private Handler mHandler;
	private String mServerIP = "";
	private int mPort;

	@Override
	public void run() {
	// Find the server using UDP broadcast
		try {
			//Open a random port to send the package
			mDatagramSocket = new DatagramSocket();
			mDatagramSocket.setBroadcast(true);

			byte[] sendData = DISCOVER_REQUEST.getBytes();

			//Try the 255.255.255.255 first
			try {
			    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), mPort);
			    mDatagramSocket.send(sendPacket);
			    System.out.println(getClass().getName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
			  } catch (Exception e) {
				System.out.println(e.getMessage());
			  }

			  // Broadcast the message over all the network interfaces
			  Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
			  while (interfaces.hasMoreElements()) {
				  NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

				  if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					  continue; // Don't want to broadcast to the loopback interface
				  }

				  for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
					  InetAddress broadcast = interfaceAddress.getBroadcast();
					  if (broadcast == null) {
						  	continue;
					  }

					  // Send the broadcast package!
					  try {
						  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, mPort);
						  mDatagramSocket.send(sendPacket);
						  System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
					  } catch (Exception e) {
						  System.out.println(e.getMessage());
					  }

					  System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
				  }
			  }

			  System.out.println(getClass().getName() + ">>> Done looping over all network interfaces. Now waiting for a reply!");

			  //Wait for a response
			  byte[] recvBuf = new byte[15000];
			  DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
			  
			  mDatagramSocket.setSoTimeout(5000);
			 
			  mDatagramSocket.receive(receivePacket);
			
			 
			  
			  //Close the port!
			  mDatagramSocket.close();
			  
			  
			  //We have a response
			  System.out.println(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

			  //Check if the message is correct
			  String message = new String(receivePacket.getData()).trim();
			  if (message.equals(DISCOVER_RESPONSE)) {
				  this.setServerIP(receivePacket.getAddress().getHostAddress());
				  mHandler.sendEmptyMessage(SocketClientActivity.SERVER_IP_RECEIVED);
			  }
			  
			} catch (SocketTimeoutException e) {
				mDatagramSocket.close();
				mHandler.sendEmptyMessage(SocketClientActivity.UDP_TIMEOUT);
			} catch (IOException ex) {
				
			}
		}
	
	public DiscoveryThread(Handler handler, int port) {
		mHandler = handler;
		mPort = port;
	}

	public String getServerIP() {
		return mServerIP;
	}

	public void setServerIP(String serverIP) {
		this.mServerIP = serverIP;
	}		
	
	
	

}
