package main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAManager {
	private final static EntityManagerFactory emf = Persistence
			.createEntityManagerFactory("JPAImplementationTest");
	private EntityManager em = emf.createEntityManager();
	public EntityManager getEntityManager () {
		return em;
	}
}
