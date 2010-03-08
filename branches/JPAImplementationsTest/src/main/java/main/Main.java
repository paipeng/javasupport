package main;

public class Main {
	public static void main(String[] args) {
		System.out.println("Waiting 2 minutes to prepare for monitoring...");
		try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Starting EntityManager... will start threads in 30 seconds");
		try {
			JPAManager jpam = new JPAManager();
			jpam.getEntityManager().close();
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Starting threads...");
		
		// Inserting threads
		for (int i = 1; i <= 2; i++) {
			(new Thread(new JPAInsertingThread(i))).start();
		}
		// Querying threads
		for (int i = 1; i <= 18; i++) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			(new Thread(new JPAQueryingThread(i))).start();
		}
		try {
			Thread.sleep(1800000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out
				.println("Sending stop signal to threads and waiting 15 minutes........................");
		JPAQueryingThread.setStop(true);
		JPAInsertingThread.setStop(true);
		try {
			Thread.sleep(900000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}