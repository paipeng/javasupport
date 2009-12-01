package deng.simplespringapp.containerservices;

import java.net.InetAddress;
import java.net.UnknownHostException;

import lombok.Setter;

public class PingService extends AbstractService {
	private boolean isRunning = false;
	
	@Setter
	private String pingHost = "localhost";
	
	@Setter
	private long pingInterval = 5000;
	
	@Override
	public void destroy() {
		isRunning = true;
		
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
		logger.info("Got: " + address);
	}
}
