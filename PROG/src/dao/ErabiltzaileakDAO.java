package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import pojos.Erabiltzailea;
import util.LoggerUtil;

/**
 * Erabiltzailea entitatearen DAO klasea. JPA eta ObjectDB erabiltzen ditu.
 */
public class ErabiltzaileakDAO {

	/**
	 * Erabiltzaile baten kredentzialak egiaztatzen ditu saioa hasteko.
	 * 
	 * @param erabiltzailea Saioa hasi nahi duen erabiltzailearen izena.
	 * @param pasahitza     Erabiltzailearen pasahitza.
	 * @return Aurkitutako erabiltzailea, edo null.
	 */
	public Erabiltzailea login(String erabiltzailea, String pasahitza) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/erabiltzaileak.odb");
		EntityManager em = emf.createEntityManager();

		try {
			TypedQuery<Erabiltzailea> query = em.createQuery(
					"SELECT e FROM Erabiltzailea e WHERE e.erabiltzailea = :izena AND e.pasahitza = :pass",
					Erabiltzailea.class);
			query.setParameter("izena", erabiltzailea);
			query.setParameter("pass", pasahitza);
			Erabiltzailea erab = query.getResultStream().findFirst().orElse(null);

			if (erab != null) {
				LoggerUtil.log("Saioa hasi da: " + erabiltzailea + " (" + erab.getRola() + ")");
			} else {
				LoggerUtil.log("Saio okerra: " + erabiltzailea);
			}

			return erab;
		} finally {
			em.close();
			emf.close();
		}
	}

	/**
	 * Erabiltzaile berri bat erregistratzen du "ERABILTZAILEA" rolarekin.
	 * 
	 * @param erabiltzailea Erabiltzaile berriaren izena.
	 * @param pasahitza     Erabiltzaile berriaren pasahitza.
	 * @return true ondo erregistratu bada, false existitzen bada.
	 */
	public boolean erregistratu(String erabiltzailea, String pasahitza) {
		return sortuErabiltzaileaRolarekin(erabiltzailea, pasahitza, "ERABILTZAILEA");
	}

	/**
	 * Erabiltzaile berri bat sortzen du rol zehatz batekin.
	 * 
	 * @param erabiltzailea Erabiltzaile izena.
	 * @param pasahitza     Pasahitza.
	 * @param rola          Rola (ADMIN, EPAILEA, ERABILTZAILEA).
	 * @return true ondo sortu bada.
	 */
	public boolean sortuErabiltzaileaRolarekin(String erabiltzailea, String pasahitza, String rola) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/erabiltzaileak.odb");
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			// Egiaztatu ea existitzen den
			TypedQuery<Erabiltzailea> query = em
					.createQuery("SELECT e FROM Erabiltzailea e WHERE e.erabiltzailea = :izena", Erabiltzailea.class);
			query.setParameter("izena", erabiltzailea);

			if (!query.getResultList().isEmpty()) {
				LoggerUtil.log("Erregistro saioa (dagoeneko existitzen da): " + erabiltzailea);
				return false;
			}

			Erabiltzailea berria = new Erabiltzailea(erabiltzailea, pasahitza, rola);
			em.persist(berria);
			em.getTransaction().commit();

			LoggerUtil.log("Erabiltzaile berria sortu da: " + erabiltzailea + " (" + rola + ")");
			return true;

		} catch (Exception e) {
			em.getTransaction().rollback();
			LoggerUtil.log("ERROR erabiltzailea sortzean: " + e.getMessage());
			return false;
		} finally {
			em.close();
			emf.close();
		}
	}

	/**
	 * Datu-basean dauden erabiltzaile guztiak lortzen ditu.
	 * 
	 * @return Erabiltzaileen zerrenda.
	 */
	public List<Erabiltzailea> getErabiltzaileak() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/erabiltzaileak.odb");
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Erabiltzailea> query = em.createQuery("SELECT e FROM Erabiltzailea e", Erabiltzailea.class);
			return query.getResultList();
		} finally {
			em.close();
			emf.close();
		}
	}

	/**
	 * Erabiltzaile bat ezabatzen du bere izenaren arabera.
	 * 
	 * @param erabiltzailea Ezabatu nahi den erabiltzailearen izena.
	 * @return true ondo ezabatu bada, false ez bada aurkitu.
	 */
	public boolean ezabatuErabiltzailea(String erabiltzailea) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/erabiltzaileak.odb");
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Erabiltzailea> query = em
					.createQuery("SELECT e FROM Erabiltzailea e WHERE e.erabiltzailea = :izena", Erabiltzailea.class);
			query.setParameter("izena", erabiltzailea);
			Erabiltzailea e = query.getResultStream().findFirst().orElse(null);

			if (e == null) {
				return false;
			}

			em.getTransaction().begin();
			em.remove(em.contains(e) ? e : em.merge(e));
			em.getTransaction().commit();

			LoggerUtil.log("Erabiltzailea ezabatu da: " + erabiltzailea + " (" + e.getRola() + ")");
			return true;

		} catch (Exception ex) {
			em.getTransaction().rollback();
			LoggerUtil.log("ERROR erabiltzailea ezabatzean: " + ex.getMessage());
			return false;
		} finally {
			em.close();
			emf.close();
		}
	}

	/**
	 * Erabiltzaile baten rola eguneratzen du.
	 * 
	 * @param erabiltzailea Erabiltzaile izena.
	 * @param rolaBerria    Rola berria (ADMIN, EPAILEA, ERABILTZAILEA).
	 * @return true ondo eguneratu bada.
	 */
	public boolean eguneratuRola(String erabiltzailea, String rolaBerria) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/erabiltzaileak.odb");
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Erabiltzailea> query = em
					.createQuery("SELECT e FROM Erabiltzailea e WHERE e.erabiltzailea = :izena", Erabiltzailea.class);
			query.setParameter("izena", erabiltzailea);
			Erabiltzailea e = query.getResultStream().findFirst().orElse(null);

			if (e == null) {
				return false;
			}

			em.getTransaction().begin();
			e.setRola(rolaBerria);
			em.getTransaction().commit();

			LoggerUtil.log("Erabiltzaile rola eguneratu da: " + erabiltzailea + " -> " + rolaBerria);
			return true;

		} catch (Exception ex) {
			em.getTransaction().rollback();
			LoggerUtil.log("ERROR erabiltzaile rola eguneratzean: " + ex.getMessage());
			return false;
		} finally {
			em.close();
			emf.close();
		}
	}
}