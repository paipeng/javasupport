package deng.simplespringapp.container;

/**
 * A container is a start and stoppable service.
 * 
 * @author dengz1
 *
 */
public interface Container extends Service {
	public boolean isRunning();
	public void start();
	public void stop();
}
