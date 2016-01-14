package kainos.clientapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Handler;

public class TCPConnectionThread implements Runnable {
	private Handler mHandler;
	private String mServerIP;
	private int mPort;
	
	private Socket myClient;
	private InputStream socketIn;
	private OutputStream socketOut;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	DataInputStream dataInputStream;

	@Override
	public void run() {
		try {
			myClient = new Socket(mServerIP, mPort);
			socketIn = myClient.getInputStream();
			socketOut = myClient.getOutputStream();
			        		           
			outStream = new ObjectOutputStream(socketOut);
			mHandler.sendEmptyMessage(SocketClientActivity.OPEN_PRESENTATION);
		}
		
		catch (IOException e) {
			System.out.println(e);
		}
	};

	public InputStream getSocketIn() {
		return socketIn;
	}

	public OutputStream getSocketOut() {
		return socketOut;
	}


	public TCPConnectionThread(Handler handler, String serverIP, int port) {
	mHandler = handler;
		mServerIP = serverIP;
		mPort = port;
	}

	
	public ObjectInputStream getInStream() {
		return inStream;
	}

	public void setInStream(ObjectInputStream inStream) {
		this.inStream = inStream;
	}

	public ObjectOutputStream getOutStream() {
		return outStream;
	}

	public void setOutStream(ObjectOutputStream outStream) {
		this.outStream = outStream;
	}
}
