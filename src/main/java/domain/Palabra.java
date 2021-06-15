package domain;

import javax.persistence.*;

// Queries básicas
@NamedQueries(
        {
            @NamedQuery(name = "Palabra.getById", query = "SELECT p from Palabra p WHERE p.idPalabra = :idPalabra"),
            @NamedQuery(name = "Palabra.getByPalabra", query = "SELECT p from Palabra p WHERE p.palabra = :palabra"),
            @NamedQuery(name = "Palabra.getMaxId", query = "SELECT p FROM Palabra p ORDER BY p.idPalabra DESC")
        }
)

@Entity
@Table(name = "PALABRAS")

public class Palabra {
    
    @Id
    @Column(name = "idPalabra")
    private Integer idPalabra;
    
    @Column(unique = true)
    private String palabra;
    
    @Column
    private Integer nr;
    
    @Column
    private Integer maxtf;

    public Palabra(String palabra) {
        this.palabra = palabra;
        this.nr = 0;
        this.maxtf = 0;
    }
    
    public Palabra(Integer id, String palabra) {
        this.idPalabra = id;
        this.palabra = palabra;
        this.nr = 0;
        this.maxtf = 0;
    }
    
    public Palabra() {
    }

    public Integer getId() {
        return idPalabra;
    }

    public String getPalabra() {
        return palabra;
    }

    public Integer getNr() {
        return nr;
    }

    public Integer getMaxtf() {
        return maxtf;
    }

    public void setId(Integer id) {
        this.idPalabra = id;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public void setNr(Integer nr) {
        this.nr = nr;
    }

    public void setMaxtf(Integer maxtf) {
        this.maxtf = maxtf;
    }
    
    public void increaseNr() {
        this.nr++;
    }
    
}
