package deng.simplespringapp.containerservices;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractService implements Service {
	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void destroy() {
		logger.info("Service is destroyed.");
	}

	@Override
	public void init() {
		logger.info("Service is ready.");
	}
}
