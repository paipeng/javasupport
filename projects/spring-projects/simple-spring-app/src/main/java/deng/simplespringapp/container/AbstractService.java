package deng.simplespringapp.container;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provide a inheritable logger and empty impl methods of of a service except
 * for {@link #run()}.
 * 
 * @author dengz1
 *
 */
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
