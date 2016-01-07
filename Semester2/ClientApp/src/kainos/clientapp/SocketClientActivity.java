package kainos.clientapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.TextView;



public class SocketClientActivity extends Activity implements Runnable{
	
	private final static int SERVER_IP_RECEIVED = 0;
	private final static int START_TCP_CONN = 1;
	private final static int ERROR = 2;
	private final static int DOWNLOAD_NOTES = 3;
	private final static String START = "start";
	private final static String NEXT = "next";
	private final static String PREVIOUS = "previous";
	private final static String EXIT = "exit";
	
	private TextView mTvInfo;
	String received;
	DatagramSocket c;
	String serverIP;
	Socket myClient;
	InputStream socketIn;
	OutputStream socketOut;
	
	ObjectInputStream inStream;
	ObjectOutputStream outStream;
	
	private GestureDetector mGestureDetector;
	
	int port = 8888;
	
	private final Handler myHandler = new Handler() {
	    public void handleMessage(Message msg) {
	        final int what = msg.what;
	        switch(what) {
	        case SERVER_IP_RECEIVED: 
	        	mTvInfo.setText("Discovered server " + serverIP);
	        	myHandler.sendEmptyMessage(START_TCP_CONN);
	        	break;
	        case START_TCP_CONN: 
	        	mTvInfo.setText("Start TCP connection...");
	        	tcpConnection.start();
	        	break;
	        case DOWNLOAD_NOTES: 
	        	mTvInfo.setText("Press TAP to start the presentation");
	        	break;
	        }
	    }
	};
	
	Thread tcpConnection = new Thread(){
		@Override
		public void run() {
			super.run();
			try {
		           myClient = new Socket(serverIP, port);
		           socketIn = new DataInputStream(myClient.getInputStream());
		           socketOut = new DataOutputStream(myClient.getOutputStream());
		        		           
		           //inStream = new ObjectInputStream(socketIn);
				   outStream = new ObjectOutputStream(socketOut);
				   System.out.println("UDA£O SIÊ!");
				   myHandler.sendEmptyMessage(DOWNLOAD_NOTES);
		    }
		    catch (IOException e) {
		        System.out.println(e);
		    }
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_socket_client);
		mTvInfo = (TextView) findViewById(R.id.info);
		mTvInfo.setText("Connecting to server ...");
		Thread thread = new Thread(this);
		thread.start();		
		mGestureDetector = new GestureDetector(this);

		mGestureDetector.setBaseListener(new GestureDetector.BaseListener() {
		@Override
		public boolean onGesture(Gesture gesture) {
			if (gesture == Gesture.TAP) {
				System.out.println("TAP!!!");
				new ControlTask().execute(START);
				return true;
			} else if (gesture == Gesture.SWIPE_RIGHT) {
				System.out.println("SWIPE_RIGHT");
				new ControlTask().execute(NEXT);
				return true;
			} else if (gesture == Gesture.SWIPE_LEFT) {
				System.out.println("SWIPE_LEFT");
				new ControlTask().execute(PREVIOUS);
				return true;
			} else if (gesture == Gesture.SWIPE_DOWN) {
				System.out.println("SWIPE_DOWN");
				new ControlTask().execute(EXIT);
				return true;
			}
			return false;
		}
		});
	}

	// Send generic motion events to the gesture detector
	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if (mGestureDetector != null) {
			return mGestureDetector.onMotionEvent(event);
		}
		return false;
	}        

	@Override
	protected void onStop() {
		super.onStop();  
		if (c != null) c.close();
	}


	public void run()
	{
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
		  c.receive(receivePacket);
		  //Close the port!
		  c.close();
		  //We have a response
		  System.out.println(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

		  //Check if the message is correct
		  String message = new String(receivePacket.getData()).trim();
		  if (message.equals("DISCOVER_RESPONSE")) {
		    //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
			  serverIP = receivePacket.getAddress().getHostAddress();
			  myHandler.sendEmptyMessage(SERVER_IP_RECEIVED);
		  }

		  
		} catch (IOException ex) {
		}
	}

	class ControlTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... control) {
			try {
				outStream.writeUTF(control[0]);
				outStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
}
