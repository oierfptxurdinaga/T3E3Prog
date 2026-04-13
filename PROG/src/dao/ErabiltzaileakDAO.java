package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import pojos.Erabiltzailea;

public class ErabiltzaileakDAO {

	// Erabiltzailea eta pasahitza existitzen diren begiratzeko metodoa
	public Erabiltzailea login(String erabiltzailea, String pasahitza) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/erabiltzaileak.odb");
		EntityManager em = emf.createEntityManager();

		try {
			TypedQuery<Erabiltzailea> query = em.createQuery(
					"SELECT e FROM Erabiltzailea e WHERE e.erabiltzailea = :izena AND e.pasahitza = :pass",
					Erabiltzailea.class);

			query.setParameter("izena", erabiltzailea);
			query.setParameter("pass", pasahitza);

			return query.getResultStream().findFirst().orElse(null);

		} finally {
			em.close();
		}

	}

	// Erabiltzailea berri bat erregitratzeko metodoa
	public boolean erregistratu(String erabiltzailea, String pasahitza) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/erabiltzaileak.odb");
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			// Konprobatu ea erabiltzailea hori existitzen zen
			TypedQuery<Erabiltzailea> query = em.createQuery("SELECT e FROM Erabiltzailea e WHERE e.erabiltzailea = :izena", Erabiltzailea.class);
			query.setParameter("izena", erabiltzailea);

			if (!query.getResultList().isEmpty()) {
				// Existitzen denenan
				return false;
			}

			// Erabiltzaile berria sortzerakoan bere rola ERABILTZAILEA izango da
			Erabiltzailea berria = new Erabiltzailea(erabiltzailea, pasahitza, "ERABILTZAILEA");

			// Gorde datu-basean
			em.persist(berria);

			em.getTransaction().commit();

			return true; // Ondo sortuta

		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			return false;

		} finally {
			em.close();
			emf.close();
		}
	}

	// Metodo honekin kontsulta batekin objectdb-ko erabiltzaile guztiak atera ahal ditugu
	public List<Erabiltzailea> getErabiltzaileak() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/erabiltzaileak.odb");
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Erabiltzailea> query = em.createQuery("SELECT e FROM Erabiltzailea e", Erabiltzailea.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	// Kontsulta batekin erabiltzaile bat ezabatu ahal izateko
	public boolean ezabatuErabiltzailea(String erabiltzailea) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/erabiltzaileak.odb");
		EntityManager em = emf.createEntityManager();
	    try {
	        TypedQuery<Erabiltzailea> query = em.createQuery( "SELECT e FROM Erabiltzailea e WHERE e.erabiltzailea = :izena", Erabiltzailea.class);
	        query.setParameter("izena", erabiltzailea);
	        Erabiltzailea e = query.getResultStream().findFirst().orElse(null);

	        if (e == null) return false; // Existetzen ez denenan

	        em.getTransaction().begin();
	        em.remove(em.contains(e) ? e : em.merge(e));
	        em.getTransaction().commit();

	        return true; // Ezabatu da
	        
	    } catch (Exception ex) {
	        em.getTransaction().rollback();
	        ex.printStackTrace();
	        return false;
	    } finally {
	        em.close();
	    }
	}

}
