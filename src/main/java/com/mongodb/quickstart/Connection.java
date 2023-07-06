package com.mongodb.quickstart;

import cassandraConnector.CassandraConnector;
import cassandraConnector.PercistenciaLog;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

public class Connection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "tpo";

    public static boolean checkIfItemExists(String itemId) {

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("tpo");
            MongoCollection<Document> catalogCollection = database.getCollection("productos");

            Document query = new Document("code", itemId);
            long count = catalogCollection.countDocuments(query);

            return count > 0;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void agregarProducto() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el nombre: ");
        String nombre = scanner.nextLine();
        System.out.println("Ingrese el nombre: ");
        String code = generarCode();
        System.out.println("Ingrese el descuento: ");
        Integer desc = scanner.nextInt();
        System.out.println("Ingrese el precio: ");
        Double price = scanner.nextDouble();
        System.out.println("Ingrese el stock: ");
        Integer stock = scanner.nextInt();
        List<String> fotos = new ArrayList<>();
        String opt = "";
        while(opt != "N"){
            System.out.println("Ingrese el link de la imagen: ");
            String foto = scanner.nextLine();
            fotos.add(foto);
            System.out.println("Si quiere seguir agregando imagenes, coloque S y si quiere parar coloque N: ");
            opt = scanner.nextLine();
        }
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("productos");
            Document producto = new Document()
                    .append("name", nombre)
                    .append("code", code)
                    .append("desc", desc)
                    .append("precio", price)
                    .append("stock", stock);
            // Agregar la foto si se proporciona un valor no nulo
            if (fotos != null && !fotos.isEmpty()) {
                producto.append("fotos", fotos);
            }
            collection.insertOne(producto);
            System.out.println("Producto agregado correctamente.");
        }
    }

    public static void verProductos() {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("productos");
            FindIterable<Document> documents = collection.find();
            MongoCursor<Document> cursor = documents.iterator();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                String nombre = document.getString("name");
                Double price = document.getDouble("price");
                String code = document.getString("code");
                System.out.println("Nombre: "+nombre+" | Precio: "+price+" | Codigo: "+code);

            }
        }
    }
    public static void actualizarProducto() {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("productos");

            Scanner scanner = new Scanner(System.in);

            // Solicitar el código del producto a actualizar
            System.out.print("Ingrese el código del producto: ");
            String code = scanner.nextLine();

            // Crear el filtro para seleccionar el producto por su código
            Bson filter = Filters.eq("code", code);

            // Solicitar el nuevo nombre
            System.out.print("Ingrese el nuevo nombre (deje en blanco para omitir): ");
            String nuevoNombre = scanner.nextLine();

            // Solicitar la nueva descripción
            System.out.print("Ingrese la nueva descripción (deje en blanco para omitir): ");
            String nuevaDescripcion = scanner.nextLine();

            // Solicitar el nuevo precio
            System.out.print("Ingrese el nuevo precio (deje en blanco para omitir): ");
            String precioString = scanner.nextLine();
            Optional<Double> nuevoPrecio = Optional.empty();
            if (!precioString.isEmpty()) {
                try {
                    double precio = Double.parseDouble(precioString);
                    nuevoPrecio = Optional.of(precio);
                } catch (NumberFormatException e) {
                    System.out.println("El valor del precio ingresado no es válido. Se omitirá la actualización del precio.");
                }
            }
            /*FindIterable<Document> documents = collection.find(filter);
            MongoCursor<Document> cursor = documents.iterator();

            // Verificar si se encontró el producto
            if (cursor.hasNext()) {
                Document document = cursor.next();
                String nombre = document.getString("name");
                String descripcion = document.getString("desc");
                String precio = document.getString("price");
                PercistenciaLog.insertLog(code,nombre,nuevoNombre,descripcion,nuevaDescripcion,precio,nuevoPrecio);
            }*/


            // Crear el documento con las actualizaciones
            Document updateDocument = new Document();

            // Actualizar el nombre si se proporciona un nuevo valor
            if (!nuevoNombre.isEmpty()) {
                updateDocument.append("name", nuevoNombre);
            }

            // Actualizar la descripción si se proporciona un nuevo valor
            if (!nuevaDescripcion.isEmpty()) {
                updateDocument.append("desc", nuevaDescripcion);
            }

            // Actualizar el precio si se proporciona un nuevo valor
            nuevoPrecio.ifPresent(precio -> updateDocument.append("price", precio));

            // Verificar si se proporcionó al menos un campo para actualizar
            if (updateDocument.isEmpty()) {
                System.out.println("No se proporcionaron campos para actualizar.");
                return;
            }

            // Actualizar el producto
            UpdateResult updateResult = collection.updateOne(filter, new Document("$set", updateDocument));

            // Verificar si la actualización fue exitosa
            if (updateResult.getModifiedCount() > 0) {
                System.out.println("Producto actualizado correctamente.");
            } else {
                System.out.println("No se encontró el producto con el código especificado.");
            }


        }
    }

    public static List<String> seleccionUsuario(){
        Map<String, String> usuarios = new HashMap<>();
        List<String> user = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection("usuarios");
        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            String id = document.getString("id");
            String nombre = document.getString("name");
            System.out.println("Nombre: " + nombre + " | ID: " + id);
            usuarios.put(id,nombre);
        }
        cursor.close();
        mongoClient.close();
        String userInput;
        // Bucle para validar la entrada del usuario
        while (true) {
            System.out.print("Ingrese un ID válido: ");
            userInput = scanner.nextLine();
            if (usuarios.containsKey(userInput)) {
                break; // Salir del bucle si la entrada es válida
            } else {
                System.out.println("ID inválido. Inténtelo nuevamente.");
            }
        }
        user.add(userInput);
        user.add(usuarios.get(userInput));
        return user;
    }

    public static void crearUsuario(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre");
        String name = scanner.nextLine();
        System.out.print("Direccion");
        String address = scanner.nextLine();
        System.out.print("Documento");
        int doc = scanner.nextInt();
        String id = generarId("usuarios");
        String corriente = generarId("cc");
        String cat = "";
        ArrayList<String> logins = new ArrayList<>();
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection("usuarios");
        Document document = new Document()
                .append("name", name)
                .append("address", address)
                .append("corriente", corriente)
                .append("doc", doc)
                .append("id",id)
                .append("cat",cat)
                .append("logins",logins);
        collection.insertOne(document);
        mongoClient.close();
    }

    public static String facturarCarrito(String carrito){
        Scanner scanner = new Scanner(System.in);
        JedisPool pool = new JedisPool("localhost", 6379);
        Map<String, Stack<Map<byte[], byte[]>>> cartUndoMap = new HashMap<>();
        boolean validarStock = true;
        int metodo_pago = -1;
        boolean state = false;
        try (Jedis jedis = pool.getResource()) {
            Map<byte[], byte[]> cartItems = jedis.hgetAll(carrito.getBytes());
            for (Map.Entry<byte[], byte[]> entry : cartItems.entrySet()) {
                String itemId = new String(entry.getKey());
                validarStock = verificarStock(itemId);
                if(validarStock != true){break;}
            }
        }

        while (true){
            System.out.println("Pagar con (1) Tarjeta, (2)Efectivo o (3)Cuenta Corriente: ");
            metodo_pago = scanner.nextInt();
            switch (metodo_pago){
                case 1:
                    state= true;
                    break;
                case 2:
                    state= true;
                    break;
                case 3:
//                    verificarCC(id);
                    break;
            }
            break;
        }

        if(validarStock == true){
            return  "Todo Ok";
        }else{
            return "Hay productos faltantes en stock.";
        }
    }

    private static boolean verificarStock(String code){


        return true;
    }

    private static boolean verificarCC(String id){
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("cc");
            FindIterable<Document> documents = collection.find();
            MongoCursor<Document> cursor = documents.iterator();
            while (cursor.hasNext()) {
                Document document = cursor.next();}}
        return true;
    }

    private static String generarId(String table){
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(table);
        MongoCursor<Document> cursor = collection.find().iterator();
        List<String> ids = new ArrayList<>();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            String id = document.getString("id");
            ids.add(id);
        }
        cursor.close();
        mongoClient.close();
        Random random = new Random();
        String caracteres = "abcdefghijklmnopqrstuvwxyz0123456789";
        int longitud = 5;
        String nuevoId = "";
        boolean idRepetido = true;
        while (idRepetido) {
            StringBuilder sb = new StringBuilder(longitud);
            for (int i = 0; i < longitud; i++) {
                int indice = random.nextInt(caracteres.length());
                char caracter = caracteres.charAt(indice);
                sb.append(caracter);
            }
            nuevoId = sb.toString();
            idRepetido = ids.contains(nuevoId);
        }
        return nuevoId;
    }

    private static String generarCode(){
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection("productos");
        MongoCursor<Document> cursor = collection.find().iterator();
        List<String> ids = new ArrayList<>();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            String id = document.getString("code");
            ids.add(id);
        }
        cursor.close();
        mongoClient.close();
        Random random = new Random();
        String caracteres = "0123456789";
        int longitud = 4;
        String nuevoId = "";
        boolean idRepetido = true;
        while (idRepetido) {
            StringBuilder sb = new StringBuilder(longitud);
            for (int i = 0; i < longitud; i++) {
                int indice = random.nextInt(caracteres.length());
                char caracter = caracteres.charAt(indice);
                sb.append(caracter);
            }
            nuevoId = sb.toString();
            idRepetido = ids.contains(nuevoId);
        }
        return nuevoId;
    }



}

