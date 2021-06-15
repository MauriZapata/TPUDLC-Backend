package domain;

import javax.persistence.*;

// Queries básicas
@NamedQueries(
        {
            @NamedQuery(name = "Posteo.getById", query = "SELECT p FROM Posteo p WHERE p.idPosteo = :idPosteo"),
            @NamedQuery(name = "Posteo.getByPalabra", query = "SELECT p FROM Posteo p WHERE p.palabra = :palabra"),
            @NamedQuery(name = "Posteo.getOrderedByPalabra", query = "SELECT p FROM Posteo p WHERE p.palabra = :palabra ORDER BY p.tf DESC"),
            @NamedQuery(name = "Posteo.getByDocumento", query = "SELECT p FROM Posteo p WHERE p.documento = :documento"),
            @NamedQuery(name = "Posteo.getByPalabraDocumento", query = "SELECT p FROM Posteo p WHERE p.palabra = :palabra AND p.documento = :documento"),
            @NamedQuery(name = "Posteo.getMaxId", query = "SELECT p FROM Posteo p ORDER BY p.idPosteo DESC")
        }
)

@Entity
@Table(name = "POSTEOS")

public class Posteo {
    
    @Id
    @Column(name = "idPosteo")
    private Integer idPosteo;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="idPalabra")
    private Palabra palabra;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="idDocumento")
    private Documento documento;
    
    @Column
    private Integer tf;

    public Posteo(Palabra palabra, Documento documento) {
        this.palabra = palabra;
        this.documento = documento;
        this.tf = 0;
    }
    
    public Posteo(Integer id, Palabra palabra, Documento documento) {
        this.idPosteo = id;
        this.palabra = palabra;
        this.documento = documento;
        this.tf = 0;
    }
    
    public Posteo() {
    }

    public Integer getId() {
        return idPosteo;
    }

    public Palabra getPalabra() {
        return palabra;
    }

    public Documento getDocumento() {
        return documento;
    }

    public Integer getTf() {
        return tf;
    }

    public void setIdPosteo(Integer id) {
        this.idPosteo = id;
    }

    public void setPalabra(Palabra palabra) {
        this.palabra = palabra;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public void setTf(Integer tf) {
        this.tf = tf;
    }
    
    public void increaseTf() { this.tf++; }
    
}
