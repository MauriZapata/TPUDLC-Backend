package Services;

import Repositories.DocumentoRepository;
import Repositories.PalabraRepository;
import Repositories.PosteoRepository;
import domain.Documento;
import domain.Palabra;
import domain.Posteo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;


public class IndexadorService {

    @Inject
    private DocumentoRepository documentoRepositorio;
    @Inject
    private PalabraRepository palabraRepositorio;
    @Inject
    private PosteoRepository posteoRepositorio;

    public static final String carpetaDocumentos = "C:\\TPUDLC\\";
    public static final String carpetaIndexado = "C:\\TPUDLC\\DocumentosIndexados\\";
    private int cantIdx;

    public int indexar() throws IOException {

        cantIdx = 0;
        File fileDirectorioDocs = new File(carpetaDocumentos);

        File[] arrayDocumentos = fileDirectorioDocs.listFiles();

        ArrayList<File> listaDocs = new ArrayList();

        for (int i = 0; i < arrayDocumentos.length; i++) {
            if (!arrayDocumentos[i].isDirectory()) {
                listaDocs.add(arrayDocumentos[i]);
            }
        }

        Pattern p = Pattern.compile("[a-zA-Z]+");
        Integer j = 0;

        Integer idPal = palabraRepositorio.getMaxId() + 1;
        Integer idPost = posteoRepositorio.getMaxId() + 1;
        if (listaDocs.size() > 0) {
            HashMap<String, Palabra> vocabulario = iniciarVocabulario();

            for (File doc : listaDocs) {
                System.gc();

                System.out.println("------------ Documento: " + doc.getName() + "------------");
                Documento documento = documentoRepositorio.getByName(doc.getName());
                if (documento == null) {
                    documento = new Documento(doc.getName());
                    documento = documentoRepositorio.create(documento);
                }

                HashMap<String, Posteo> posteos = new HashMap(100000, 0.5f);

                ArrayList<Palabra> palsPendientesCreacion = new ArrayList(50000);

                BufferedReader br = new BufferedReader(new FileReader(doc));
                String renglon;
                String primerasLineas = "";
                int lineCount = 0;
                while ((renglon = br.readLine()) != null) {

                    if (0 <= lineCount && lineCount <= 2) {
                        primerasLineas += renglon + "- ";
                    }

                    Matcher m = p.matcher(renglon);

                    while (m.find()) {

                        String pal = m.group().toLowerCase();

                        Palabra palabra = vocabulario.get(pal);
                        if (palabra == null) {
                            if (palabra == null) {
                                palabra = new Palabra(idPal, pal);
                                idPal++;
                                palsPendientesCreacion.add(palabra);
                            }
                            vocabulario.put(palabra.getPalabra(), palabra);
                        }

                        Posteo posteo = posteos.get(pal);
                        if (posteo == null) {
                            posteo = new Posteo(idPost, palabra, documento);
                            idPost++;
                            posteos.put(pal, posteo);
                        }

                        posteo.increaseTf();

                        if (j % 10000 == 0) {
                            System.out.println("Palabras procesadas: " + j.toString());
                        }

                        j++;
                    }
                    lineCount++;
                }
                br.close();
                documento.setPrimerasLineas(primerasLineas);
                documento.setPath(carpetaIndexado + documento.getNombre());
                // Se actualiza el documento
                documentoRepositorio.update(documento);
                // Se insertan las palabras
                palabraRepositorio.createList(palsPendientesCreacion);

                updatePalabras(vocabulario, posteos);

                // Y por último se insertan los posteos
                Collection<Posteo> values = posteos.values();
                ArrayList<Posteo> valuesList = new ArrayList<Posteo>(values);
                posteoRepositorio.createList(valuesList);

                System.out.println("Se indexó el documento: " + doc.getName());
                moveToIndexados(doc);
                cantIdx++;
            }

        }
        System.out.println("----------------------------Proceso de indexación finalizado con éxito.");
        return cantIdx;
    }

    private HashMap<String, Palabra> iniciarVocabulario() {
        HashMap<String, Palabra> vocabulario = new HashMap(800000, 0.5f);
        ArrayList<Palabra> queryPalabras = new ArrayList<Palabra>(palabraRepositorio.getAll());
        Integer queryLength = queryPalabras.size();
        for (int i = 0; i < queryLength; i++) {
            String stringPalabra = queryPalabras.get(i).getPalabra();
            Palabra objetoPalabra = queryPalabras.get(i);
            vocabulario.put(stringPalabra, objetoPalabra);
        }

        return vocabulario;
    }

    private void updatePalabras(HashMap<String, Palabra> vocabulario,
            HashMap<String, Posteo> posteos) {
        Collection<Posteo> colPost = posteos.values();
        ArrayList<Posteo> arrayListPosteos = new ArrayList(colPost);
        ArrayList<Palabra> arrayListPalabras = new ArrayList(vocabulario.size());
        for (Posteo posteo : arrayListPosteos) {
            Palabra palabra = posteo.getPalabra();
            palabra.increaseNr();
            if (posteo.getTf() > palabra.getMaxtf()) {
                palabra.setMaxtf(posteo.getTf());
            }
            arrayListPalabras.add(palabra);
        }
        palabraRepositorio.updateList(arrayListPalabras);
    }


    // Método para mover los archivos indexados al nuevo directotrio
    private void moveToIndexados(File file) {
        try {
            Path temp = Files.move(Paths.get(carpetaDocumentos + file.getName()),
                    Paths.get(carpetaIndexado + file.getName()));

        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }
}
