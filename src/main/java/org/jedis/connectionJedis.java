package org.jedis;

import com.mongodb.quickstart.Connection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.Set;


import java.util.*;

public class connectionJedis {
    private static JedisPool pool = new JedisPool("localhost", 6379);
    private static Map<String, Stack<Map<byte[], byte[]>>> cartUndoMap = new HashMap<>();

    public static void addItemToCart(String clienteId) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el ID del carrito: ");
        String cartId = scanner.nextLine();

        System.out.print("Ingrese el ID del artículo: ");
        String itemId = scanner.nextLine();

        System.out.print("Ingrese la cantidad: ");
        int cantidad = scanner.nextInt();
        try (Jedis jedis = pool.getResource()) {
            // Verificar si el artículo existe en el catálogo
            boolean itemExists = Connection.checkIfItemExists(itemId);
            if (itemExists) {
                saveState(cartId, jedis.hgetAll(cartId.getBytes()));
                jedis.hset(cartId.getBytes(), itemId.getBytes(), String.valueOf(cantidad).getBytes());
                jedis.hset(cartId.getBytes(), "clienteId".getBytes(), clienteId.getBytes());
                System.out.println("Agregado correctamente.");
            } else {
                System.out.println("El artículo no existe en el catálogo.");
            }
        }
    }
    public static void printCartItemsv2() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el ID del carrito: ");
        String cartIdBuscar = scanner.nextLine();

        try (Jedis jedis = pool.getResource()) {
            Map<byte[], byte[]> cartItems = jedis.hgetAll(cartIdBuscar.getBytes());

            if (cartItems.isEmpty()) {
                System.out.println("El carrito de compra con ID " + cartIdBuscar + " no se encontró.");
                return;
            }

            for (Map.Entry<byte[], byte[]> entry : cartItems.entrySet()) {
                String itemId = new String(entry.getKey());
                if (!"clienteId".equals(itemId)) {
                    int cantidad = Integer.parseInt(new String(entry.getValue()));
                    System.out.println("Item: " + itemId + ", Cantidad: " + cantidad);
                }
            }
        }
    }

    public static boolean existsCart(String cartIdBuscar) {

        try (Jedis jedis = pool.getResource()) {
            Map<byte[], byte[]> cartItems = jedis.hgetAll(cartIdBuscar.getBytes());

            if (cartItems.isEmpty()) {

                return false;
            }

            for (Map.Entry<byte[], byte[]> entry : cartItems.entrySet()) {
                String itemId = new String(entry.getKey());
                if (!"clienteId".equals(itemId)) {
                    int cantidad = Integer.parseInt(new String(entry.getValue()));
                }
            }
            return true;
        }
    }



    public static void printCartItems(String cartIdBuscar) {

        try (Jedis jedis = pool.getResource()) {
            Map<byte[], byte[]> cartItems = jedis.hgetAll(cartIdBuscar.getBytes());
            for (Map.Entry<byte[], byte[]> entry : cartItems.entrySet()) {
                String itemId = new String(entry.getKey());
                if (!"clienteId".equals(itemId)) {
                    int cantidad = Integer.parseInt(new String(entry.getValue()));
                    System.out.println("Item: " + itemId + ", Cantidad: " + cantidad);
                }
            }
        }
    }


    public static void updateCartItemQuantity(String cartId, String itemId, int cantidadNueva) {
        try (Jedis jedis = pool.getResource()) {
            saveState(cartId, jedis.hgetAll(cartId.getBytes()));
            jedis.hset(cartId.getBytes(), itemId.getBytes(), String.valueOf(cantidadNueva).getBytes());
        }
    }

    public static void undo( ) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el ID del carrito para volver un paso atras: ");
        String cartId = scanner.nextLine();
        Stack<Map<byte[], byte[]>> undoStack = cartUndoMap.get(cartId);
        if (undoStack != null && !undoStack.isEmpty()) {
            Map<byte[], byte[]> previousState = undoStack.pop();
            try (Jedis jedis = pool.getResource()) {
                jedis.del(cartId.getBytes());
                for (Map.Entry<byte[], byte[]> entry : previousState.entrySet()) {
                    jedis.hset(cartId.getBytes(), entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private static void saveState(String cartId, Map<byte[], byte[]> currentState) {
        Stack<Map<byte[], byte[]>> undoStack = cartUndoMap.get(cartId);
        if (undoStack == null) {
            undoStack = new Stack<>();
            cartUndoMap.put(cartId, undoStack);
        }
        undoStack.push(new HashMap<>(currentState));
    }

    public static void removeItemCart() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el ID del carrito para Eliminar un item: ");
        String cartId = scanner.nextLine();

        System.out.print("Ingrese el ID del item para eliminar: ");
        String itemId = scanner.nextLine();
        try (Jedis jedis = pool.getResource()) {
            saveState(cartId, jedis.hgetAll(cartId.getBytes()));
            jedis.hdel(cartId.getBytes(), itemId.getBytes());
        }
        connectionJedis.printCartItems(cartId);
    }



    public static void deleteCart() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el ID del carrito para Eliminar : ");
        String cartIdDelete = scanner.nextLine();
        try (Jedis jedis = pool.getResource()) {
            saveState(cartIdDelete, jedis.hgetAll(cartIdDelete.getBytes()));
            jedis.del(cartIdDelete.getBytes());
        }
    }

    public static void payedCart(String cartIdDelete) {
        try (Jedis jedis = pool.getResource()) {
            saveState(cartIdDelete, jedis.hgetAll(cartIdDelete.getBytes()));
            jedis.del(cartIdDelete.getBytes());
        }
    }

    public static String getClienteIdFromCart(String cartId) {
        try (Jedis jedis = pool.getResource()) {
            byte[] clienteIdBytes = jedis.hget(cartId.getBytes(), "clienteId".getBytes());
            if (clienteIdBytes != null) {
                return new String(clienteIdBytes);
            } else {
                return null;
            }
        }
    }
    public static HashMap<String, Object> extractCartProducts(String cartId) {
        try (Jedis jedis = pool.getResource()) {
            Map<String, String> cartData = jedis.hgetAll(cartId);

            HashMap<String, Object> productos = new HashMap<>();
            for (Map.Entry<String, String> entry : cartData.entrySet()) {
                String itemId = entry.getKey();
                int quantity = Integer.parseInt(entry.getValue());

                productos.put(itemId, quantity);
            }

            return productos;
        }
    }

}
