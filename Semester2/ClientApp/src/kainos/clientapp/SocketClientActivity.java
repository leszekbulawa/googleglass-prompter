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
import android.view.WindowManager;
import android.widget.TextView;



public class SocketClientActivity extends Activity {
	
	public final static int START_SERVER_DISCOVERY = 0;
	public final static int SERVER_IP_RECEIVED = 1;
	public final static int START_TCP_CONN = 2;
	public final static int OPEN_PRESENTATION = 3;
	public final static int START_PRESENTATION = 4;
	public final static int PRINT_NOTES = 5;
	private final static String START = "start";
	private final static String NEXT = "next";
	private final static String PREVIOUS = "previous";
	private final static String EXIT = "exit";
	
	private TextView mTvInfo;
	String received;
	DatagramSocket datagramSocket;
	String serverIP;
	String message;
	DiscoveryThread discoveryThread;
	TCPConnectionThread tcpConnection;
	String[] notes;
	int indx = 0;
	boolean getnotes = true;
	
	ObjectOutputStream outStream;
	ObjectInputStream inStream;
	
	private GestureDetector mGestureDetector;
	
	int port = 8888;
	
	private final Handler myHandler = new Handler() {
	    public void handleMessage(Message msg) {
	        final int what = msg.what;
	        switch(what) {
	        case START_SERVER_DISCOVERY:
	        	mTvInfo.setText("Starting server discovery...");
	        	discoveryThread = new DiscoveryThread(myHandler, port);
	        	new Thread(discoveryThread).start();
	        	break;
	        case SERVER_IP_RECEIVED:
	        	serverIP = discoveryThread.getServerIP();
	        	mTvInfo.setText("Discovered server " + serverIP);
	        	myHandler.sendEmptyMessage(START_TCP_CONN);
	        	break;
	        case START_TCP_CONN: 
	        	mTvInfo.setText("Starting TCP connection...");
	        	tcpConnection = new TCPConnectionThread(myHandler, serverIP, port);
	        	new Thread(tcpConnection).start();
	        	break;
	        case OPEN_PRESENTATION:
	        	outStream = tcpConnection.getOutStream();
	        	mTvInfo.setText("Open presentation on the server and TAP to start it");
	        	break;
	        case START_PRESENTATION:
	        	mTvInfo.setText("Press TAP to start the presentation");
	        	break;
	        case PRINT_NOTES:
	        	mTvInfo.setText(notes[indx]);
	        }
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_socket_client);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		mTvInfo = (TextView) findViewById(R.id.info);
		myHandler.sendEmptyMessage(START_SERVER_DISCOVERY);
		
		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			@Override
			public boolean onGesture(Gesture gesture) {
			if (gesture == Gesture.TAP) {
				indx = 0;
				new ControlTask().execute(START);
				return true;
			} else  if (gesture == Gesture.SWIPE_RIGHT) {
				if (indx < notes.length - 1) indx++;
				else new ControlTask().execute(EXIT);
				new ControlTask().execute(NEXT);
				return true;
			} else if (gesture == Gesture.SWIPE_LEFT) {
				if (indx > 0 ) indx--;
				else new ControlTask().execute(EXIT);
				new ControlTask().execute(PREVIOUS);
				return true;
			} else if (gesture == Gesture.TWO_TAP) {
				indx = 0;
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
		if (datagramSocket != null) datagramSocket.close();
	}


	class ControlTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... control) {
			try {
				outStream.writeUTF(control[0]);
				outStream.flush();
				if(control[0] == START && getnotes) {
					inStream = new ObjectInputStream(tcpConnection.getSocketIn());
					if(inStream != null) {
						notes = (String[]) inStream.readObject();
						getnotes = false;
					}
				}
				
			} catch (IOException | ClassNotFoundException e) {
				System.out.println(e.getMessage());
			}
			myHandler.sendEmptyMessage(PRINT_NOTES);
			return null;
		}
		
	}
}
