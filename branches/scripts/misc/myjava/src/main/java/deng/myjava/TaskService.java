package deng.myjava;

public class TaskService extends AbstractService {
	public TaskService() {
		setName(this.toString());
	}
  @Override 
  public void run() {
    Thread t = Thread.currentThread();
    System.out.println(this + " " + t + " I am running");  
  }
}
