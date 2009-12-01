package deng.simplespringapp.container.services;

import java.net.InetAddress;
import java.net.UnknownHostException;

import deng.simplespringapp.container.AbstractService;

import lombok.Setter;

/**
 * Ping a host server continually between an milliseconds interval.
 * 
 * @author dengz1
 *
 */
public class PingService extends AbstractService {
	private volatile boolean isRunning = false;
	
	@Setter
	private String pingHost = "localhost";
	
	@Setter
	private long pingInterval = 5000;
	
	public boolean isRunning() {
		return isRunning;
	}
	
	@Override
	public void destroy() {
		isRunning = false;
		
		super.destroy();
	}
	
	@Override
	public void run() {
		isRunning = true;
		
		while (isRunning) {
			try {
				ping();
				Thread.sleep(pingInterval);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	

	private void ping() throws UnknownHostException {
		logger.info("Pinging " + pingHost);
		InetAddress address = InetAddress.getByName(pingHost);
		logger.info("Success! " + address);
	}
}
