package kainos.clientapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.transition.Visibility;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;



public class SocketClientActivity extends Activity {
	
	public final static int START_SERVER_DISCOVERY = 0;
	public final static int UDP_TIMEOUT = 1;
	public final static int SERVER_IP_RECEIVED = 2;
	public final static int START_TCP_CONN = 3;
	public final static int TCP_CONN_LOST = 4;
	public final static int OPEN_PRESENTATION = 5;
	public final static int START_PRESENTATION = 6;
	public final static int PRINT_NOTES = 7;
	private final static String START = "start";
	private final static String NEXT = "next";
	private final static String PREVIOUS = "previous";
	private final static String EXIT = "exit";
	private final static String CHANGE_FONT = "font";
	
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
	
	private ListeningThread listenToServer;
	private boolean isListening;
	private GetFonts getFonts;
	boolean fontData[] = {true, false, false}; 
	private Typeface typeface;
	int textSize = 16;
	
	private GestureDetector mGestureDetector;
	private Chronometer chronometer;
	
	int port = 8888;
	
	private final Handler myHandler = new Handler() {
	    public void handleMessage(Message msg) {
	        final int what = msg.what;
	        switch(what) {
	        case START_SERVER_DISCOVERY:
	        	mTvInfo.setText("Starting server discovery...");        	
	        	new Thread(discoveryThread).start();
	        	break;
	        case UDP_TIMEOUT:
	        	mTvInfo.setText("Timeout reached, TAP again to reconnect.");
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
	        case TCP_CONN_LOST: 
	        	isListening = false;
	        	mTvInfo.setText("Connection lost. Restart server and TAP to reconnect");
	        	break;
	        case OPEN_PRESENTATION:
	        	outStream = tcpConnection.getOutStream();
	        	isListening = true;
	        	new Thread(listenToServer).start();
	        	mTvInfo.setText("Open presentation on the server and TAP to start it");
	        	break;
	        case START_PRESENTATION:
	        	mTvInfo.setText("Press TAP to start the presentation");
	        	break;
	        case PRINT_NOTES:
	        	if(notes != null){
	        		mTvInfo.setText(notes[indx]);
	        	} else {
	        		mTvInfo.setText("Presentation not loaded.\nOpen presentation on the server and TAP to start it");
	        		
	        	}
	        }
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_socket_client);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		listenToServer  = new ListeningThread();
		
		getFonts = new GetFonts(getApplicationContext());
		typeface = getFonts.getArial();	
		
		mTvInfo = (TextView) findViewById(R.id.info);
		mTvInfo.setTypeface(typeface);
		mTvInfo.setTextSize(textSize);
		
		discoveryThread = new DiscoveryThread(myHandler, port);
		
		chronometer = (Chronometer) findViewById(R.id.chronometer);
		chronometer.setBase(SystemClock.elapsedRealtime() + (3*1000));	
		
		myHandler.sendEmptyMessage(START_SERVER_DISCOVERY);
		
		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			@Override
			public boolean onGesture(Gesture gesture) {
			if (gesture == Gesture.TAP) {
				String command = mTvInfo.getText().toString();
				if(command.contains("Timeout reached") || command.contains("Connection lost")){
					myHandler.sendEmptyMessage(START_SERVER_DISCOVERY);
				} else {
					indx = 0;
					new ControlTask().execute(START);
					if(!chronometer.isActivated())
						chronometer.start();
					return true;
				}
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
			} else if (gesture == Gesture.LONG_PRESS) {
				indx = 0;
				new ControlTask().execute(EXIT);
				return true;
			}
			return false;
		}
		});
	}

	/**
	 * 
	 */
	protected void changeFont(boolean[] data) {
		if(data[0]){
			typeface = getFonts.getArial();
			textSize = 16;	
		} else if(data[1]){
			typeface = getFonts.getArial();
			textSize = 18;	
		} else if (data[2]) {
			typeface = getFonts.getCalibri();
			textSize = 18;	
		} else {
			typeface = getFonts.getArial();
			textSize = 16;	
			System.out.println("Font error, switched to default");
		}
		new ControlTask().execute(CHANGE_FONT);
		
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


	private class ControlTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... control) {
			if(control[0] != CHANGE_FONT){
				try {
					outStream.writeUTF(control[0]);
					outStream.flush();				
				} catch (IOException  e) {
					System.out.println(e.getMessage());
				}
				myHandler.sendEmptyMessage(PRINT_NOTES);
				return false;
			} else{
				return true;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				mTvInfo.setTypeface(typeface);
				mTvInfo.setTextSize(textSize);
			}
		}	
	}
	
	private class ChronometerTask extends AsyncTask<Boolean, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Boolean... control) {
			return control[0];
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				chronometer.setVisibility(View.VISIBLE);
				chronometer.setBase(SystemClock.elapsedRealtime() + (3*1000));
				chronometer.start();
			} else {
				chronometer.stop();
				chronometer.setVisibility(View.INVISIBLE);
			}
		}	
	}
	
	private class ListeningThread implements Runnable {

		@Override
		public void run() {
			Object dataReceived;
			try {
				inStream = new ObjectInputStream(tcpConnection.getSocketIn());
				while (isListening) {
					dataReceived = inStream.readObject();
					if (dataReceived instanceof boolean[]) {
						fontData = (boolean[]) dataReceived;
						changeFont(fontData);
					} else if (dataReceived instanceof String[]) {
						notes = (String[]) dataReceived;
					} else if (dataReceived instanceof Boolean){
						new ChronometerTask().execute((Boolean)dataReceived);
					}
					System.out.println("CHODZI!!!!!!!!!!!!!!!!!!!!!!!");
				}
			} catch (IOException e) { //socket connection lost
				isListening = false;
				
				myHandler.sendEmptyMessage(TCP_CONN_LOST);
				
//				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if(tcpConnection != null)
				tcpConnection.closeSocket();
		};

	
	}
}
