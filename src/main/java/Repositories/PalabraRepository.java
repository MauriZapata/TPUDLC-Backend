package Repositories;

import domain.Palabra;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

// Repositorio que realiza todas las entradas a la base de datos relacionadas con la entidad palabra.
public class PalabraRepository {

    EntityManagerFactory ef = Persistence.createEntityManagerFactory("MotorDeBusquedaPU");
    EntityManager entityManager = ef.createEntityManager();

    public PalabraRepository() {
    }

    // Método que trae el último id insertado
    public int getMaxId() {
        List<Palabra> palabras = entityManager.createNamedQuery("Palabra.getMaxId")
                .setMaxResults(1)
                .getResultList();
        if (palabras == null || palabras.size() == 0) {
            return 0;
        }

        return palabras.get(0).getId();
    }

    // Método que persiste una lista de palabras
    public List<Palabra> createList(List<Palabra> list) {

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

    // Método que actualiza una lista de palabras
    public List<Palabra> updateList(List<Palabra> list) {
        try {
            entityManager.getTransaction().begin();
            for (int i = 0; i < list.size(); i++) {
                Palabra palabra = entityManager.merge(list.get(i));
                entityManager.persist(palabra);
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

    // Trae todas las palabras para el vocabulario
    public List<Palabra> getAll() {

        String className = Palabra.class.getSimpleName();
        Query query = entityManager.createQuery("SELECT e FROM " + className + " e");
        return query.getResultList();
    }

    // Trae una palabra por su id
    public Palabra getById(Integer id) {
        List<Palabra> palabras = entityManager.createNamedQuery("Palabra.getById")
                .setParameter("idPalabra", id)
                .getResultList();
        if (palabras.size() == 1) {
            return palabras.get(0);
        }

        return null;
    }

    // Trae una palabra de acuerdo a un string
    public Palabra getByString(String palabra) {
        List<Palabra> palabras = entityManager.createNamedQuery("Palabra.getByPalabra")
                .setParameter("palabra", palabra)
                .getResultList();
        if (palabras.size() == 1) {
            return palabras.get(0);
        }

        return null;
    }
}
