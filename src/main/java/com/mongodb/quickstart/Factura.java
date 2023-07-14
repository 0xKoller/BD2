package  com.mongodb.quickstart;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.*;

public class Factura {

    private String id;
    private String id_user;
    private double importe;
    private Document productos;
    private boolean metodo;
    private String id_emple;
    private Date fecha;

    public Factura() {
        this.id_user = "";
        this.importe = 0.0;
        this.productos = Document.parse("");
        this.metodo = true;
        this.id_emple = "";
        this.id = generarId();
        this.fecha = new Date();
    }

    public Factura(String id_user,  Document productos, boolean metodo) {
        this.id_user = id_user;
        this.productos = productos;
        this.metodo = metodo;
        this.id = generarId();
        this.fecha = new Date();
    }

    public String getId() {
        return id;
    }

    private static String generarId() {
        Random random = new Random();
        String caracteres = "0123456789";
        int longitud = 10;
        StringBuilder sb = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            int indice = random.nextInt(caracteres.length());
            char caracter = caracteres.charAt(indice);
            sb.append(caracter);
        }
        return sb.toString();
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public HashMap<String, Object> getProductos() {
        HashMap<String, Object> map = new HashMap<>(this.productos);
        return map;
    }

    public void setProductos(HashMap<String, Object> productos) {
        float resultado = 0;
        for (Object value : productos.values()) {
            int floatValue = (int) value;
            resultado += floatValue;
        }
        setImporte(resultado);
        this.productos = new Document(productos);
    }

    public Document getProductosForFactura() {
        return new Document(this.productos);
    }

    public boolean getMetodo() {
        return metodo;
    }

    public void setMetodo(boolean metodo) {
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

    public Document toDocument() {
        Document document = new Document();
        document.append("id_user", this.id_user)
                .append("importe", this.importe)
                .append("productos", this.productos)
                .append("metodo", this.metodo)
                .append("id_emple", this.id_emple)
                .append("fecha", this.fecha);
        return document;
    }
}
