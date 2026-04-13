package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import pojos.Erabiltzailea;

/**
 * Erabiltzailea entitatearentzako Datuetarako Sarbide Objektua (DAO - Data Access Object).
 * JPA (Java Persistence API) eta ObjectDB erabiltzen ditu erabiltzaileen datuak kudeatzeko 
 * (saioa hasi, erregistratu, zerrendatu eta ezabatu).
 */
public class ErabiltzaileakDAO {

	/**
	 * Erabiltzaile baten kredentzialak egiaztatzen ditu saioa hasteko.
	 * * @param erabiltzailea Saioa hasi nahi duen erabiltzailearen izena.
	 * @param pasahitza Erabiltzaile horren pasahitza.
	 * @return Datu-basean aurkitutako {@link Erabiltzailea} objektua, edo null kredentzialak okerrak badira edo ez bada existitzen.
	 */
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

	/**
	 * Erabiltzaile berri bat datu-basean erregistratzen du. 
	 * Lehenik erabiltzaile izen hori aske dagoen egiaztatzen du. Sortzen den 
	 * erabiltzaile berriari "ERABILTZAILEA" rola esleitzen zaio lehenespenez.
	 * * @param erabiltzailea Sortu nahi den erabiltzaile berriaren izena.
	 * @param pasahitza Erabiltzaile berriari esleituko zaion pasahitza.
	 * @return true erregistroa ondo burutu bada, edo false erabiltzailea jadanik existitzen bada edo erroreren bat egon bada.
	 */
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

	/**
	 * Datu-basean erregistratuta dauden erabiltzaile guztien zerrenda eskuratzen du.
	 * * @return {@link Erabiltzailea} objektuen zerrenda (List).
	 */
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

	/**
	 * Erabiltzaile zehatz bat datu-baseko erregistroetatik ezabatzen du.
	 * * @param erabiltzailea Ezabatu nahi den erabiltzailearen izena.
	 * @return true erabiltzailea modu egokian ezabatu bada, edo false erabiltzailea ez bada aurkitu edo erroreren bat egon bada.
	 */
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