package deng.simplespringapp.container;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Setter;

/**
 * A simple container that will execute a series of services in the order
 * that is given in {@link #services} list.
 * 
 * If no {@link #executorService} is set, then a default thread pool with
 * {@link #threadPoolSize} number of threads will be created to execute 
 * the services.
 * 
 * @author dengz1
 *
 */
public class ServiceContainer extends AbstractService implements Container {
	private volatile boolean isRunning = false;
	
	@Setter
	private List<Service> services;
	
	@Setter
	private ExecutorService executorService;
		
	@Setter
	private int threadPoolSize = 4;	

	@Override
	public boolean isRunning() {
		return isRunning;
	}
	
	@Override
	public void run() {
		// Do nothing here. Everything are in #start method.
	}

	@Override
	public void start() {
		if (executorService == null) {
			logger.debug("Creating default fixed " + threadPoolSize + " threads pool.");
			executorService = Executors.newFixedThreadPool(threadPoolSize);
		}
		
		if (!isRunning) {
			logger.debug("Starting container: " + this + " with " + services.size() + " services.");
			
			for (Service service : services) {
				logger.debug("Initializing service: " + service);
				service.init();
				logger.debug("Service: " + service + " is initialized.");
			}
	
			for (Service service : services) {
				logger.debug("Adding service " + service + " to executorService.");
				executorService.execute(service);
			}
			
			isRunning = true;

			logger.debug("Container: " + this + " has started.");
		}
	}

	@Override
	public void stop() {		
		if (isRunning) {
			logger.debug("Stopping container: " + this);
			isRunning = false;
			for (Service service : services) {
				logger.debug("Destroying service: " + service);
				service.destroy();
				logger.debug("Service " + service + " is destroyed.");
			}
			logger.debug("Container: " + this + " has stopped.");
		} else {
			logger.warn("Trying to stop a service that is not running.");
		}
	}
}
