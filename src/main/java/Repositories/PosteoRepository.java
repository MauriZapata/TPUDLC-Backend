package Repositories;

import domain.Documento;
import domain.Palabra;
import domain.Posteo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

// Repositorio que realiza todas las entradas a la base de datos relacionadas con la entidad palabra.
public class PosteoRepository {

    EntityManagerFactory ef = Persistence.createEntityManagerFactory("MotorDeBusquedaPU");
    EntityManager entityManager = ef.createEntityManager();

    public PosteoRepository() {
    }

    // Método que persiste una lista de posteos
    public List<Posteo> createList(List<Posteo> list) {
        try {
            entityManager.getTransaction().begin();
            for (int i = 0; i < list.size(); i++) {
                entityManager.persist(list.get(i));
                if (i % 1000 == 0 || i == list.size() - 1) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
        return list;
    }

    // Método que trae un posteo por id
    public Posteo getById(Integer id) {
        List<Posteo> posteos = entityManager.createNamedQuery("Posteo.getById")
                .setParameter("idPosteo", id)
                .getResultList();
        if (posteos.size() == 1) {
            return posteos.get(0);
        }
        return null;
    }

    // Método que trae un posteo por palabra y documento
    public Posteo getByPalabraDocumento(Palabra palabra, Documento documento) {
        List<Posteo> posteos = entityManager.createNamedQuery("Posteo.getByPalabraDocumento")
                .setParameter("palabra", palabra).setParameter("documento", documento)
                .getResultList();
        if (posteos.size() == 1) {
            return posteos.get(0);
        }

        return null;
    }

    // Método que trae una lista de posteos por palabra
    public List<Posteo> getByPalabra(Palabra palabra) {
        List<Posteo> posteos = entityManager.createNamedQuery("Posteo.getByPalabra")
                .setParameter("palabra", palabra)
                .getResultList();

        return posteos;
    }

    // Método que trae una lista de posteos ordenados por palabra
    public List<Posteo> getOrderedByPalabra(int cantidad, Palabra palabra) {
        List<Posteo> posteos = entityManager.createNamedQuery("Posteo.getOrderedByPalabra")
                .setParameter("palabra", palabra).setMaxResults(cantidad)
                .getResultList();

        return posteos;
    }

    // Método que trae un posteo de acuerdo a un documento
    public List<Posteo> getByDocumento(Documento documento) {
        List<Posteo> posteos = entityManager.createNamedQuery("Posteo.getByDocumento")
                .setParameter("documento", documento)
                .getResultList();

        return posteos;
    }

    // Método que trae el último id insertado
    public Integer getMaxId() {
        List<Posteo> posteos = entityManager.createNamedQuery("Posteo.getMaxId")
                .setMaxResults(1)
                .getResultList();
        if (posteos == null || posteos.size() == 0) {
            return 0;
        }
        return posteos.get(0).getId();
    }
}
