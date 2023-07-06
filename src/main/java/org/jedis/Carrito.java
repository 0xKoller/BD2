package org.jedis;

import java.util.HashMap;


public class Carrito {
    private static String id;
    private static String id_client;
    private static HashMap<String, String> productos;
    private static float total;

    public static String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }

    public HashMap<String, String> getProductos() {
        return productos;
    }

    public void setProductos(HashMap<String, String> productos) {
        this.productos = productos;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}

