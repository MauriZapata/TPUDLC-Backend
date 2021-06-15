package Repositories;

import domain.Documento;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

// Repositorio que realiza todas las entradas a la base de datos relacionadas con la entidad documento.
public class DocumentoRepository
{
    EntityManagerFactory ef = Persistence.createEntityManagerFactory("MotorDeBusquedaPU");
    EntityManager entityManager = ef.createEntityManager();
    
    public DocumentoRepository() {
    }
    
    // Método que crea un documento
    public Documento create(Documento doc)
    {
        try
        {
            entityManager.getTransaction().begin();
            entityManager.persist(doc);
            entityManager.flush();
            entityManager.getTransaction().commit();
        }
        catch (Exception ex)
        {
            entityManager.getTransaction().rollback();
        }
        
        return doc;
    }
    
    // Método que actualiza un documento
    public void update(Documento doc)
    {
        try
        {
            entityManager.getTransaction().begin();
            Documento documento = entityManager.merge(doc);
            entityManager.persist(documento);
            entityManager.flush();
            entityManager.getTransaction().commit();
        }
        catch (Exception ex)
        {
            entityManager.getTransaction().rollback();
        }
    }
    
    // Método que busca un documento por id
    public Documento getById(Integer id){
        List<Documento> resp = entityManager.createNamedQuery("Documento.getById")
            .setParameter("id", id)
            .getResultList();
        if (resp.size() == 1)
            return resp.get(0);
        
        return null;
    }
    
    // Método que busca un documento por nombre
    public Documento getByName(String nombre) {
        List<Documento> resp = entityManager.createNamedQuery("Documento.getByNombre")
                .setParameter("nombre", nombre)
                .getResultList();
        if (resp.size() == 1)
            return resp.get(0);
        
        return null;
    }
    
    // Método que trae el id del último documento insertado
    public Integer getMaxId()
    {
        List<Documento> documentos = entityManager.createNamedQuery("Documento.getMaxId")
                .getResultList();
        if (documentos == null || documentos.size() == 0)
            return 0;
        
        return documentos.get(0).getId();
    }
    
}
