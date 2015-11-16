package serverapp.host;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Tom
 *
 * @created on 24 paü 2015
 *
 */
public class LocalHost {

	private static String HOST_NAME = "Unknown";
	private static String HOST_IP="";	
	
	public LocalHost() {
		try {
			InetAddress IPaddres = Inet4Address.getLocalHost();
			HOST_NAME = IPaddres.getHostName();
			HOST_IP = IPaddres.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public String getHostName(){
		return HOST_NAME;
	}
	
	public String getHostIP(){
		return HOST_IP;
	}
}
