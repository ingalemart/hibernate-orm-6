package org.hibernate.bugs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.bugs.entity.MarketSale;
import org.hibernate.bugs.entity.Peach;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		final String fruitName = "Good Peach";
		final String fruitColor = "Red";

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		Peach peach = new Peach();
		peach.setColor(fruitColor);
		peach.setName(fruitName);
		entityManager.persist(peach);

		MarketSale marketSale = new MarketSale();
		marketSale.setDateSold(LocalDate.now());
		marketSale.setFruitColor(fruitColor);
		marketSale.setFruitName(fruitName);
		entityManager.persist(marketSale);

		List<String> peaches = entityManager.createQuery(
			"select fromSubquery.name " +
			"from (" +
				"select p.name name, p.color color from Peach p) fromSubquery " +
			"where (fromSubquery.name, fromSubquery.color) IN ( " +
				"select m.fruitName, m.fruitColor " +
				"from MarketSale m)",
			 String.class).getResultList();

		assertEquals(1, peaches.size());

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
