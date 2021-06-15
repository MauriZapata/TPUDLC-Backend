package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

// Queries Básicas
@NamedQueries(
        {
            @NamedQuery(name = "Documento.getById", query = "SELECT d FROM Documento d WHERE d.idDocumento = :idDocumento"),
            @NamedQuery(name = "Documento.getByNombre", query = "SELECT d FROM Documento d WHERE d.nombre = :nombre"),
            @NamedQuery(name = "Documento.getMaxId", query = "SELECT d FROM Documento d ORDER BY d.idDocumento DESC")
        }
)

@Entity
@Table(name="DOCUMENTOS")

public class Documento
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idDocumento")
    private Integer idDocumento;
    
    @Column(unique = true, name = "nombre")
    private String nombre;
    
    @Column(unique = false, name = "primerasLineas")
    private String primerasLineas;
    
    @Column(unique = true, name = "ruta")
    private String ruta;
    
    

    public Integer getId() {
        return idDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(Integer id) {
        this.idDocumento = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Documento(String nombre) {
        this.nombre = nombre;
    }
    
    public String getPrimerasLineas() {
        return primerasLineas;
    }

    public void setPrimerasLineas(String primerasLineas) {
        this.primerasLineas = primerasLineas;
    }
    
     public String getPath() {
        return ruta;
    }
 
    public void setPath(String ruta) {
        this.ruta = ruta;
    }

    public Documento() {
    }
}
 
