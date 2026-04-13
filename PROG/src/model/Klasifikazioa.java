package model;

import java.util.*;

import dao.PartidaDAO;
import dao.TaldeaDAO;
import pojos.Partida;
import pojos.Taldea;
import pojos.TaldearenKlasifikazioa;

public class Klasifikazioa {

	private PartidaDAO partidaDAO;
	private TaldeaDAO taldeaDAO;

	public Klasifikazioa() {
		partidaDAO = new PartidaDAO();
		taldeaDAO = new TaldeaDAO();
	}

	public List<TaldearenKlasifikazioa> lortuKlasifikazioaDenboraldian(int denboraldiaKod) {

		// Denboraldi honetako partidak lortu
		List<Partida> partidak = partidaDAO.partidaLortuDenboraldiBitartez(denboraldiaKod);

		// Talde bakoitzaren klasifikazioa gordetzeko mapa (taldeKodea -> klasifikazioa)
		Map<Integer, TaldearenKlasifikazioa> map = new HashMap<>();

		// Talde guztiak hasieratu (nahiz eta partidarik jokatu ez)
		List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
		for (Taldea t : taldeak) {
			map.put(t.getTaldeaKod(), new TaldearenKlasifikazioa(t));
		}

		// Partida bakoitza prozesatu
		for (Partida p : partidak) {

			// Talderen bat null bada, partida hau saltatu
			if (p.getEtxekoTaldea() == null || p.getKanpokoTaldea() == null)
				continue;

			int etxekoTaldeKod = p.getEtxekoTaldea().getTaldeaKod();
			int kanpokoTaldeKod = p.getKanpokoTaldea().getTaldeaKod();

			// Mapan dagokion klasifikazioa lortu
			TaldearenKlasifikazioa etxekoa = map.get(etxekoTaldeKod);
			TaldearenKlasifikazioa kanpokoa = map.get(kanpokoTaldeKod);

			// Ez bada aurkitzen, saltatu
			if (etxekoa == null || kanpokoa == null)
				continue;

			// Emaitza zatitu (adibidez: "3-1")
			String[] sets = p.getEmaitza().split("-");
			if (sets.length != 2)
				continue;

			try {
				int etxekoSets = Integer.parseInt(sets[0].trim());
				int kanpokoSets = Integer.parseInt(sets[1].trim());

				// Set irabaziak eta galduak gehitu
				etxekoa.gehituSetakIrabaziak(etxekoSets);
				etxekoa.gehituSetakGalduak(kanpokoSets);

				kanpokoa.gehituSetakIrabaziak(kanpokoSets);
				kanpokoa.gehituSetakGalduak(etxekoSets);

				// Jokatutako partidak gehitu
				etxekoa.gehitupartidaJokatua();
				kanpokoa.gehitupartidaJokatua();

				// Irabazlea zein den kalkulatu
				if (etxekoSets > kanpokoSets) {
					etxekoa.gehitupartidaIrabazia();
					kanpokoa.gehitupartidaGaldua();
				} else {
					kanpokoa.gehitupartidaIrabazia();
					etxekoa.gehitupartidaGaldua();
				}

			} catch (NumberFormatException e) {
				// Emaitza okerra bada (zenbakia ez bada), ez dugu kontuan hartzen
			}
		}

		// Mapan dauden balioak lista batean bihurtu
		List<TaldearenKlasifikazioa> lista = new ArrayList<>(map.values());

		/* Klasifikazioa ordenatu:
			1. Puntuak
		 	2. Set diferentzia
			3. Irabazitako set kopurua */
		Collections.sort(lista, (a, b) -> {
			if (b.getPuntuak() != a.getPuntuak())
				return b.getPuntuak() - a.getPuntuak();

			if (b.getSetDiferentzia() != a.getSetDiferentzia())
				return b.getSetDiferentzia() - a.getSetDiferentzia();

			return b.getSetakIrabaziak() - a.getSetakIrabaziak();
		});

		// Azken klasifikazioa bueltatu
		return lista;
	}
}