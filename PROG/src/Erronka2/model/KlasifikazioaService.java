package Erronka2.model;

import java.util.*;

import Erronka2.model.dao.PartidaDAO;
import Erronka2.model.dao.TaldeaDAO;
import Erronka2.model.pojos.Partida;
import Erronka2.model.pojos.Taldea;
import Erronka2.model.pojos.TaldearenKlasifikazioa;

public class KlasifikazioaService {

    private PartidaDAO partidaDAO;
    private TaldeaDAO taldeaDAO;

    public KlasifikazioaService() {
        partidaDAO = new PartidaDAO();
        taldeaDAO = new TaldeaDAO();
    }

    public List<TaldearenKlasifikazioa> getKlasifikazioaDenboraldian(int denboraldiaKod) {
        // Obtener partidos de la temporada
        List<Partida> partidak = partidaDAO.getByDenboraldia(denboraldiaKod);
        Map<Integer, TaldearenKlasifikazioa> map = new HashMap<>();

        // Inicializar con todos los equipos
        List<Taldea> taldeak = taldeaDAO.getAll();
        for (Taldea t : taldeak) {
            map.put(t.getTaldeaKod(), new TaldearenKlasifikazioa(t));
        }

        // Procesar partidos
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

                etxekoa.gehituSetakIrabaziak(etxekoSets);
                etxekoa.gehituSetakGalduak(kanpokoSets);
                kanpokoa.gehituSetakIrabaziak(kanpokoSets);
                kanpokoa.gehituSetakGalduak(etxekoSets);

                etxekoa.gehitupartidaJokatua();
                kanpokoa.gehitupartidaJokatua();

                if (etxekoSets > kanpokoSets) {
                    etxekoa.gehitupartidaIrabazia();
                    kanpokoa.gehitupartidaGaldua();
                } else {
                    kanpokoa.gehitupartidaIrabazia();
                    etxekoa.gehitupartidaGaldua();
                }
            } catch (NumberFormatException e) {
                // ignorar
            }
        }

        List<TaldearenKlasifikazioa> lista = new ArrayList<>(map.values());
        Collections.sort(lista, (a, b) -> {
            if (b.getPuntuak() != a.getPuntuak())
                return b.getPuntuak() - a.getPuntuak();
            if (b.getSetDiferentzia() != a.getSetDiferentzia())
                return b.getSetDiferentzia() - a.getSetDiferentzia();
            return b.getSetakIrabaziak() - a.getSetakIrabaziak();
        });
        return lista;
    }
}