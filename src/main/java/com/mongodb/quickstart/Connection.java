package com.mongodb.quickstart;


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
import org.jedis.Carrito;
import org.jedis.connectionJedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.print.Doc;
import java.time.Duration;
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
        while (opt != "N") {
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
                System.out.println("Nombre: " + nombre + " | Precio: " + price + " | Codigo: " + code);

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


    public static List<String> seleccionUsuario() {
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
            usuarios.put(id, nombre);
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
    public static void addToClientDuracion(String idUser, int duracion) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("usuarios");

            // Crear el filtro para buscar el cliente por el ID del usuario
            Document filter = new Document("id", idUser);

            // Crear el documento con el valor a agregar a la lista
            Document valueToAdd = new Document("lista", duracion);

            // Crear el documento de actualización para agregar el valor a la lista
            Document updateDocument = new Document("$push", valueToAdd);

            // Actualizar el cliente en la colección
            collection.updateOne(filter, updateDocument);

            System.out.println("Valor agregado correctamente al cliente.");
            actualizarEstadoCliente(idUser);
        }
    }
    public static void actualizarEstadoCliente(String idUser) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("usuarios");

            // Filtrar por ID del cliente
            Document filter = new Document("id", idUser);

            // Obtener el cliente
            Document cliente = collection.find(filter).first();

            // Verificar si el cliente existe
            if (cliente != null) {
                // Obtener la lista de números
                List<Integer> numeros = cliente.getList("lista", Integer.class);

                // Calcular el promedio
                double promedio = calcularPromedio(numeros);

                // Obtener el nombre basado en el promedio
                String estado = obtenerNombre(promedio);

                // Actualizar el nombre en el cliente
                cliente.put("tipoCliente", estado);
                collection.replaceOne(filter, cliente);

                // Imprimir el resultado
                System.out.println("El cliente es de tipo: " + estado);
            } else {
                System.out.println("No se encontró el cliente con el ID especificado.");
            }
        }
    }
    private static double calcularPromedio(List<Integer> numeros) {
        if (numeros == null || numeros.isEmpty()) {
            return 0.0;
        }

        int suma = 0;
        for (int numero : numeros) {
            suma += numero;
        }

        return (double) suma / numeros.size();
    }
    private static String obtenerNombre(double promedio) {
        if (promedio > 240) {
            return "TOP";
        } else if (240 > promedio && promedio > 120) {
            return "MEDIUM";
        }else{
            return "LOW";
        }
    }

    public static void crearUsuario() {
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
                .append("id", id)
                .append("cat", cat)
                .append("logins", logins);
        collection.insertOne(document);
        mongoClient.close();
    }

    public static String facturarCarrito() {

        Scanner scanner = new Scanner(System.in);
        JedisPool pool = new JedisPool("localhost", 6379);
        String id_cc = "";
        System.out.println("Ingrese el ID del carrito que desea pagar: ");
        String id = scanner.nextLine();
        boolean validarStock = true;
        int metodo_pago = -1;
        boolean state = false;
        boolean exists = true;
        exists = connectionJedis.existsCart(id);
        if(exists){
        try (Jedis jedis = pool.getResource()) {
            Map<byte[], byte[]> cartItems = jedis.hgetAll(id.getBytes());
            for (Map.Entry<byte[], byte[]> entry : cartItems.entrySet()) {
                String itemId = new String(entry.getKey());
                String stock = new String(entry.getValue());
                validarStock = verificarStock(itemId, stock);
                if (validarStock != true) {
                    break;
                }
            }
            Factura factura = new Factura();
            while (true) {
                System.out.println("Pagar con (1) Tarjeta, (2)Efectivo o (3)Cuenta Corriente: ");
                metodo_pago = scanner.nextInt();
                switch (metodo_pago) {
                    case 1:
                        factura.setMetodo(true);
                        state = true;
                        break;
                    case 2:
                        factura.setMetodo(true);
                        state = true;
                        break;
                    case 3:
                        id_cc = scanner.nextLine();
                        state = verificarCC(id_cc);
                        break;
                }
                break;
            }
            if (state) {
                connectionJedis.payedCart(id);
                if (validarStock == true) {
                    String id_del_usuario = connectionJedis.getClienteIdFromCart(id);
                    factura.setId_user(id_del_usuario);
                    factura.setProductos(connectionJedis.extractCartProducts(id));
                    if (metodo_pago != 3) {
                        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
                        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
                        MongoCollection<Document> collection = database.getCollection("usuarios");
                        Document document = new Document("facturas", factura.toDocument());
                        collection.insertOne(document);
                        return "Factura pagada.";
                    } else {
                        factura.setMetodo(false);
                        agregarCC(factura.getId(),factura.getImporte(), id_cc);
                        return "Factura agregada en cc.";
                    }
                } else {
                    return "Hay productos faltantes en stock.";
                }
            } else {
                return "No hay saldo en la cuenta corriente.";
            }
        }
        }else{
            return "No existe ese carrito.";
        }
    }


    private static boolean verificarCC(String id) {
        HashMap<String, Double> cuentas = new HashMap<>();
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("cc");
            FindIterable<Document> documents = collection.find();
            MongoCursor<Document> cursor = documents.iterator();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                String idC = document.getString("id");
                Double saldo = document.getDouble("saldo");
                cuentas.put(idC, saldo);
            }
            if (cuentas.containsKey(id)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static String generarId(String table) {
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

    private static String generarCode() {
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

    private static boolean verificarStock(String code, String stockSoli) {
        boolean flag = true;
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("productos");
            Document query = new Document("code", code); // Define la consulta (por ejemplo, field: value)
            FindIterable<Document> result = collection.find(query);
            MongoCursor<Document> cursor = result.iterator();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                int stock = document.getInteger("stock");
                int soli = Integer.parseInt(stockSoli);
                if(stock < soli){
                    flag = false;
                }
            }
        }
        return flag;
    }

    private static void agregarCC(String id_fact, Double monto, String id_cc){
        HashMap<String, String> cc = new HashMap<>();
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection("cc");
        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            String id = document.getString("id");
            String idObject = document.getString("_id");
            cc.put(id, idObject);
        }
        Document query = new Document("_id", cc.get(id_cc));
        Document update = new Document("$push", new Document("fact", id_fact));
        collection.updateOne(query, update);
        Document updatedDocument = collection.find(query).first();

        // Obtener el valor double y modificarlo
        double oldValue = updatedDocument.getDouble("saldo");
        double newValue = oldValue + monto;
        cursor.close();

    }

    public static void pagarFactura(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese la CC a pagar: ");
        String cc = scanner.nextLine();
        List<String> ccExistentes = new ArrayList<>();
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection("cc");
        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            String id = document.getString("id");
            ccExistentes.add(id);
        }
        if(ccExistentes.contains(cc)){

            Document document = collection.find().first();

            List<String> arrayField = (List<String>) document.get("fact");

            // Recorrer e imprimir los elementos del array
            for (String element : arrayField) {
                System.out.println("Factura ID: "+element);
            }
            System.out.println("Seleccione una factura a pagar: ");
            String factSelecc = scanner.nextLine();
            MongoCollection<Document> collectionUsers = database.getCollection("usuarios");

            // Realizar la búsqueda por código dentro del campo de array
            Document query = new Document("arrayField", new Document("$elemMatch", new Document("codigo", factSelecc)));
            Document documentUsers = collectionUsers.find(query).first();
        }
    }
}

