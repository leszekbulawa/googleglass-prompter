package kainos.clientapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;



public class SocketClientActivity extends Activity implements Runnable{
	private TextView mTvInfo;
	String received;
	DatagramSocket c;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_socket_client);
		mTvInfo = (TextView) findViewById(R.id.info);
		mTvInfo.setText("Connecting to server ...");
		Thread thread = new Thread(this);
		thread.start();		
	}

	@Override
	protected void onStop() {
		super.onStop();  
		if (c != null) c.close();
	}


	public void run()
	{
		String serverIP = "192.168.56.1"; // my Mac on 28

		int port = 8888;
		// Find the server using UDP broadcast
		try {
		  //Open a random port to send the package
		  c = new DatagramSocket();
		  c.setBroadcast(true);

		  byte[] sendData = "DISCOVER_REQUEST".getBytes();

		  //Try the 255.255.255.255 first
		  try {
		    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), port);
		    c.send(sendPacket);
		    System.out.println(getClass().getName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
		  } catch (Exception e) {
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
		        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, port);
		        c.send(sendPacket);
		        System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
		      } catch (Exception e) {
		      }

		      System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
		    }
		  }

		  System.out.println(getClass().getName() + ">>> Done looping over all network interfaces. Now waiting for a reply!");

		  //Wait for a response
		  byte[] recvBuf = new byte[15000];
		  DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
		  mTvInfo.setText("Packet Sent. Waiting fo response.");
		  c.receive(receivePacket);

		  //We have a response
		  System.out.println(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

		  //Check if the message is correct
		  String message = new String(receivePacket.getData()).trim();
		  if (message.equals("DISCOVER_RESPONSE")) {
		    //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
			  mTvInfo.setText("message:" + message);
		  }

		  //Close the port!
		  c.close();
		} catch (IOException ex) {
			final IOException e = ex;
			runOnUiThread(new Runnable() {
				public void run() {
					mTvInfo.setText(e.getMessage());
				}
			});	
		}
		
	}
}
