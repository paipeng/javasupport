package main;

import java.util.Random;

import javax.persistence.EntityManager;

import entity.Employees;

public class JPAInsertingThread implements Runnable {
	int threadCount;
	static boolean stop = false;
	static int inserts = 0;
	
	synchronized static int getInserts() {
		return inserts;
	}
	
	synchronized static void roundFinished () {
		inserts ++; 
	}
	
	public static boolean isStop() {
		return stop;
	}

	public static void setStop(boolean stop) {
		JPAInsertingThread.stop = stop;
	}


	
	public JPAInsertingThread(int i) {
		this.threadCount = i;
	}
	
	@Override
	public void run() {
		long created = 0;
		System.out.println("InsertingThread " + threadCount + " started");
		EntityManager em = new JPAManager().getEntityManager();

		while (!isStop()) {
			em.getTransaction().begin();

			// We get one employee with a random identification number (emp_no)
			// and duplicate the record
			Random generator = new Random();

			int emp_no = generator.nextInt(100000) + 10000;

			Employees emp = em.find(Employees.class, emp_no);

			// We insert a new copy (but with different id)
			Employees emp_copy = new Employees(emp);
			em.persist(emp_copy);
			em.getTransaction().commit();
			created++;
			if (created % 50 == 0) {
				System.out.println("INSERT -- " + created + " new employees created by now by insertingthread " + threadCount);
			}
			roundFinished();
		}
		em.close();
		System.out.println("InsertingThread " + threadCount + " finished. Inserts executed until now: " + getInserts());
	}

}
