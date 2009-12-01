package deng.simplespringapp.container;

/**
 * A runnable service that provides init and destroy life cycles.
 * 
 * @author dengz1
 *
 */
public interface Service extends Runnable {
	public void init();
	public void destroy();
}
