package main;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class JPAQueryingThread implements Runnable {
	Random generator = new Random();
	int threadCount;
	int salary;
	int year;
	Query query = null;
	Number numEmployees = 0;
	String temp;
	Timestamp date;
	static boolean stop = false; 
	static int queries = 0;
	
	synchronized static int getQueries() {
		return queries;
	}
	
	synchronized static void roundFinished () {
		queries += 5; 
	}

	public static boolean isStop() {
		return stop;
	}

	public static void setStop(boolean stop) {
		JPAQueryingThread.stop = stop;
	}

	public JPAQueryingThread(int i) {
		this.threadCount = i;
	}

	@Override
	public void run() {
		System.out.println("QueryingThread " + threadCount + " started");
		EntityManager em = new JPAManager().getEntityManager();

		while (!isStop()) {

			// find number of female employees

			query = em.createNamedQuery("findAllWomen");

			numEmployees = (Number) query.getSingleResult();
			System.out.println("CONSULT -- Number of female employees: " + numEmployees);

			// find number of male employees

			query = em.createNamedQuery("findAllMen");

			numEmployees = (Number) query.getSingleResult();
			System.out.println("CONSULT -- Number of male employees: " + numEmployees);

			// find employees hired after a fixed date

			year = getRandomYear();
			Query query = em.createNamedQuery("findAllHiredAfterDate");
			temp = year + "-01-01";
			Timestamp date = Timestamp.valueOf(temp + " 00:00:00.000000000");

			query.setParameter("date", toDate(date));
			numEmployees = (Number) query.getSingleResult();
			System.out.println("CONSULT -- Employees hired since " + year + ": "
					+ numEmployees);

			// find employees born after a fixed date

			year = getRandomYear() - 20;
			query = em.createNamedQuery("findAllBirthAfterDate");
			String temp = year + "-01-01";
			date = Timestamp.valueOf(temp + " 00:00:00.000000000");

			query.setParameter("date", toDate(date));
			numEmployees = (Number) query.getSingleResult();
			System.out.println("CONSULT -- Employees born after " + year + ": "
					+ numEmployees);

			// find female employees who have earned more than a fixed salary

			query = em.createNamedQuery("findAllWomenMaxSalary");

			query.setParameter("salary", getRandomSalary());
			numEmployees = (Number) query.getSingleResult();
			System.out.println("CONSULT -- Women who have earned more than " + salary
					+ ": " + numEmployees);
			roundFinished();
		}
		System.out.println("QueryingThread " + threadCount + " finished. Queries executed until now: " + getQueries());
	}

	public int getRandomYear() {
		year = generator.nextInt(20) + 1980;
		return year;
	}

	public int getRandomSalary() {
		salary = generator.nextInt(50000) + 40000;
		return salary;

	}
	
	public static java.sql.Date toDate(java.sql.Timestamp timestamp) {
	    long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
	    return new java.sql.Date(milliseconds);
	}
}
