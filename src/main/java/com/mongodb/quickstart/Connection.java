package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

import java.util.*;

public class Connection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "tpo";
    private static final String COLLECTION_NAME = "productos";

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

            // Agregar un nuevo producto
            //agregarProducto("Producto test","2342", "Descripción del producto 1", 99.99,23);

            // Ver todos los productos
            //verProductos();
            actualizarProducto();
        }
    }

    public static void agregarProducto(String nombre,String code, String descripcion,String fotos, double precio, int stock) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document producto = new Document()
                    .append("nombre", nombre)
                    .append("code", code)
                    .append("descripcion", descripcion)
                    .append("precio", precio)
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
        Map<String, List<String>> productos = new HashMap<>();
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            FindIterable<Document> documents = collection.find();
            MongoCursor<Document> cursor = documents.iterator();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                String nombre = document.getString("name");
                String price = document.getString("price");
                String code = document.getString("code");
                List<String> info = new ArrayList<>();
                info.add(nombre);
                info.add(price);
                System.out.println("Nombre: "+nombre+" Precio: "+price+" Codigo: "+code);

            }
        }
    }
    public static void actualizarProducto() {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

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

            // Crear el documento con las actualizaciones
            Document updateDocument = new Document();

            // Actualizar el nombre si se proporciona un nuevo valor
            if (!nuevoNombre.isEmpty()) {
                updateDocument.append("nombre", nuevoNombre);
            }

            // Actualizar la descripción si se proporciona un nuevo valor
            if (!nuevaDescripcion.isEmpty()) {
                updateDocument.append("descripcion", nuevaDescripcion);
            }

            // Actualizar el precio si se proporciona un nuevo valor
            nuevoPrecio.ifPresent(precio -> updateDocument.append("precio", precio));

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
}