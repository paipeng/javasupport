package deng.myrunbean;


/**
 * 
 * A runnable bean that's act as service.
 * @author zemian
 * 
 */
public interface RunnableBean extends Runnable {
	
	public void init() throws Exception;

	public void destroy() throws Exception;
}
