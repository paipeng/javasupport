package deng.simplespringapp.containerservices;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import lombok.Setter;

public class ServiceContainer implements Container {
	private Log logger = LogFactory.getLog(getClass());
	
	@Setter
	private List<Service> services;

	@Override
	public void start() {
		logger.debug("Starting container: " + this + " with " + services.size() + " services.");

		for (Service service : services) {
			logger.debug("Initializing service: " + service);
			service.init();
		}

		for (Service service : services) {
			logger.debug("Service " + service + " starting to run.");
			service.run();
			logger.debug("Service " + service + " run completed.");
		}
	}

	@Override
	public void stop() {
		logger.debug("Stopping container: " + this);
		for (Service service : services) {
			logger.debug("Destroying service: " + service);
			service.destroy();
		}
	}
}
