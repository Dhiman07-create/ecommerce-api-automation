package utils;

import java.util.HashMap;
import java.util.Map;


public class TestDataFactory {
    public static Map<String, Object> loginPayload(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        return map;
    }


    public static Map<String, Object> addCartPayload(int userId, int productId, int qty) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        Map<String, Object> p = new HashMap<>();
        p.put("id", productId);
        p.put("quantity", qty);
        map.put("products", new Object[]{p});
        return map;
    }
}
