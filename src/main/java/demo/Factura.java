package demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Entity
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String id_user;
    private int doc;
    private double importe;
    private List<String> productos;
    private String metodo;
    private String id_emple;
    private Date fecha;


    // Constructor vacío (obligatorio para JPA)
    public Factura() {
    }

    public Factura(String id_user, int doc, double importe, List<String> productos, String metodo, String id_emple, Date fecha) {
        this.id_user = id_user;
        this.doc = doc;
        this.importe = importe;
        this.productos = productos;
        this.metodo = metodo;
        this.id_emple = id_emple;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public int getDoc() {
        return doc;
    }

    public void setDoc(int doc) {
        this.doc = doc;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public List<String> getProductos() {
        return productos;
    }

    public void setProductos(List<String> productos) {
        this.productos = productos;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getId_emple() {
        return id_emple;
    }

    public void setId_emple(String id_emple) {
        this.id_emple = id_emple;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}
