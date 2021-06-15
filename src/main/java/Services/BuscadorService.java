package Services;

import domain.Documento;
import javax.inject.Inject;
import Repositories.PalabraRepository;
import Repositories.PosteoRepository;
import domain.Palabra;
import domain.Posteo;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuscadorService {

    @Inject
    private PalabraRepository palabraRepositorio;
    @Inject
    private PosteoRepository posteoRepositorio;

    private final String carpetaIndexado = IndexadorService.carpetaIndexado;

    public List<Documento> buscar(String query) {

        File fileDirectorioDocs = new File(carpetaIndexado);
        Integer N = fileDirectorioDocs.listFiles().length;

        // Inicialización de la lista de respuestas que se retornará al final.
        List<Documento> resp = new ArrayList();

        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(query);
        ArrayList<Palabra> listaPalabras = new ArrayList(10);
        while (m.find()) {
            String termino = m.group().toLowerCase();
            Palabra pal = palabraRepositorio.getByString(termino);
            if (pal != null) {
                listaPalabras.add(pal);
            }
        }

        if (listaPalabras.size() > 0) {
            Collections.sort(listaPalabras, new ComparadorNrPalabras());

            HashMap<Documento, Double> mapDocumentos = new HashMap(200);

            for (Palabra pal : listaPalabras) {
                
                // Se obtienen los dos posteos con tf mas alto
                ArrayList<Posteo> posteos = new ArrayList(posteoRepositorio.getOrderedByPalabra(10, pal));
                for (Posteo post : posteos) {

                    Double puntaje = post.getTf() * Math.log10(N / pal.getNr());

                    Documento doc = post.getDocumento();
                    if (!mapDocumentos.containsKey(doc)) {
                        mapDocumentos.put(doc, 0.0);
                    }

                    
                    Double puntTemp = mapDocumentos.get(doc) + puntaje;
                    mapDocumentos.remove(doc);
                    mapDocumentos.put(doc, puntTemp);

                }
            }

            ArrayList<ContenedorDocumento> listaDocumentos = new ArrayList();

            Iterator it = mapDocumentos.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Documento, Double> par = (Map.Entry) it.next();
                Documento docu = par.getKey();
                Double punt = par.getValue();

                ContenedorDocumento cont = new ContenedorDocumento(
                        docu, punt);
                listaDocumentos.add(cont);

                it.remove();
            }

            // se ordenan de acuerdo a sus puntajes
            Collections.sort(listaDocumentos,
                    new ComparadorPuntajeDocumentos());

            for (int i = listaDocumentos.size() - 1; i > -1; i--) {
                Documento docResp = listaDocumentos.get(i).getDoc();
                resp.add(docResp);
            }
        }

        return resp;
    }

    private class ComparadorNrPalabras implements Comparator<Palabra> {

        @Override
        public int compare(Palabra p1, Palabra p2) {
            return p1.getNr().compareTo(p2.getNr());
        }
    }

    private class ContenedorDocumento {

        private Documento doc;
        private Double puntaje;

        public ContenedorDocumento(Documento doc, Double puntaje) {
            this.doc = doc;
            this.puntaje = puntaje;
        }

        public Documento getDoc() {
            return doc;
        }

        public Double getPuntaje() {
            return puntaje;
        }

    }
    
    private class ComparadorPuntajeDocumentos implements Comparator<ContenedorDocumento> {

        @Override
        public int compare(ContenedorDocumento p1, ContenedorDocumento p2) {
            return p1.getPuntaje().compareTo(p2.getPuntaje());
        }
    }

    
}
