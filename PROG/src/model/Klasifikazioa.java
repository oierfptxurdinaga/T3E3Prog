package model;

import java.util.*;
import dao.PartidaDAO;
import dao.TaldeaDAO;
import pojos.Partida;
import pojos.Taldea;
import pojos.TaldearenKlasifikazioa;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Negozio-logikaren klasea (Model geruza). Boleibol ligako taldeen sailkapena kalkulatzeaz arduratzen da.
 */
public class Klasifikazioa {

    private PartidaDAO partidaDAO;
    private TaldeaDAO taldeaDAO;

    public Klasifikazioa() {
        partidaDAO = new PartidaDAO();
        taldeaDAO = new TaldeaDAO();
    }

    /**
     * Denboraldi bateko sailkapena kalkulatu eta ordenatzen du.
     * Irizpideak: 1. Puntuak, 2. Set diferentzia, 3. Irabazitako setak.
     * 
     * @param denboraldiaKod Denboraldiaren identifikatzailea.
     * @return Ordenatutako TaldearenKlasifikazioa zerrenda.
     * @throws DataAccessException Errorea badago datu-basean.
     */
    public List<TaldearenKlasifikazioa> lortuKlasifikazioaDenboraldian(int denboraldiaKod) throws DataAccessException {
        List<Partida> partidak = partidaDAO.partidaLortuDenboraldiBitartez(denboraldiaKod);
        Map<Integer, TaldearenKlasifikazioa> map = new HashMap<>();

        // Talde guztiak hasieratu
        List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
        for (Taldea t : taldeak) {
            map.put(t.getTaldeaKod(), new TaldearenKlasifikazioa(t));
        }

        // Partida bakoitza prozesatu
        for (Partida p : partidak) {
            if (p.getEtxekoTaldea() == null || p.getKanpokoTaldea() == null) continue;

            int etxekoTaldeKod = p.getEtxekoTaldea().getTaldeaKod();
            int kanpokoTaldeKod = p.getKanpokoTaldea().getTaldeaKod();

            TaldearenKlasifikazioa etxekoa = map.get(etxekoTaldeKod);
            TaldearenKlasifikazioa kanpokoa = map.get(kanpokoTaldeKod);

            if (etxekoa == null || kanpokoa == null) continue;

            String[] sets = p.getEmaitza().split("-");
            if (sets.length != 2) continue;

            try {
                int etxekoSets = Integer.parseInt(sets[0].trim());
                int kanpokoSets = Integer.parseInt(sets[1].trim());

                // Setak gehitu
                etxekoa.gehituSetakIrabaziak(etxekoSets);
                etxekoa.gehituSetakGalduak(kanpokoSets);
                kanpokoa.gehituSetakIrabaziak(kanpokoSets);
                kanpokoa.gehituSetakGalduak(etxekoSets);

                // Partida jokatuak
                etxekoa.gehitupartidaJokatua();
                kanpokoa.gehitupartidaJokatua();

                // Irabazlea zehaztu
                if (etxekoSets > kanpokoSets) {
                    etxekoa.gehitupartidaIrabazia();
                    kanpokoa.gehitupartidaGaldua();
                } else {
                    kanpokoa.gehitupartidaIrabazia();
                    etxekoa.gehitupartidaGaldua();
                }
            } catch (NumberFormatException e) {
                // Emaitza okerra bada, saltatu
                LoggerUtil.log("Oharra: Partida baten emaitza ezin da prozesatu: " + p.getEmaitza());
            }
        }

        List<TaldearenKlasifikazioa> lista = new ArrayList<>(map.values());

		// Ordenatu: puntuak > set diferentzia > irabazitako setak
        Collections.sort(lista, (a, b) -> {
            if (b.getPuntuak() != a.getPuntuak()) return b.getPuntuak() - a.getPuntuak();
            if (b.getSetDiferentzia() != a.getSetDiferentzia()) return b.getSetDiferentzia() - a.getSetDiferentzia();
            return b.getSetakIrabaziak() - a.getSetakIrabaziak();
        });

        return lista;
    }
}