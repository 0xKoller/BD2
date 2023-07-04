package org.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class connectionJedis {
    private static JedisPool pool = new JedisPool("localhost", 6379);
    private static Map<String, Stack<Map<byte[], byte[]>>> cartUndoMap = new HashMap<>();

    public static void addItemToCart(String cartId, String clienteId, String itemId, int quantity) {
        try (Jedis jedis = pool.getResource()) {
            saveState(cartId, jedis.hgetAll(cartId.getBytes()));
            jedis.hset(cartId.getBytes(), itemId.getBytes(), String.valueOf(quantity).getBytes());
            jedis.hset(cartId.getBytes(), "clienteId".getBytes(), clienteId.getBytes());
        }
    }

    public static void printCartItems(String cartId) {
        try (Jedis jedis = pool.getResource()) {
            Map<byte[], byte[]> cartItems = jedis.hgetAll(cartId.getBytes());
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

    public static void undo(String cartId) {
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
    public static void removeItemCart(String cartId, String itemId) {
        try (Jedis jedis = pool.getResource()) {
            saveState(cartId, jedis.hgetAll(cartId.getBytes()));
            jedis.hdel(cartId.getBytes(), itemId.getBytes());
        }
    }
    public static void deleteCart(String cartId) {
        try (Jedis jedis = pool.getResource()) {
            saveState(cartId, jedis.hgetAll(cartId.getBytes()));
            jedis.del(cartId.getBytes());
        }
    }

}