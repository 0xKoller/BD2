package org.jedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class connectionJedis {
    private static JedisPool pool = new JedisPool("localhost", 6379);

    public static void addItemToCart(String cartId,String clienteId, String itemId, int quantity) {
        try (Jedis jedis = pool.getResource()) {
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
    public static void updateCartItemQuantity(String cartId, String itemId, int newQuantity) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hset(cartId.getBytes(), itemId.getBytes(), String.valueOf(newQuantity).getBytes());
        }
    }
}