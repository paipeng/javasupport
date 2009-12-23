package deng.myjava;

public class Task implements Runnable {
  @Override 
  public void run() {
    Thread t = Thread.currentThread();
    System.out.println(this + " " + t + " I am running");  
  }
}
