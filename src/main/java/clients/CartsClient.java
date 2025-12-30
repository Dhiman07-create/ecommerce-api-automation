package clients;

import models.Cart;
import io.restassured.response.Response;


import java.util.Map;


public class CartsClient extends ApiClient {
    public Cart addToCart(Map<String, Object> payload) {
        Response res = post("/carts/add", payload);
        System.out.println("Add api response: "+res.asPrettyString());
        return res.as(Cart.class);
    }


    public Cart getCart(int id) {
        //return get("/carts/" + id).then().statusCode(200).extract().as(Cart.class);
        return get("/carts/1").then().statusCode(200).extract().as(Cart.class);
    }


    public Cart updateCart(int id, Map<String, Object> payload) {
        Response res = put("/carts/" + id, payload);
        return res.as(Cart.class);
    }


    public void deleteCart(int id) {
        delete("/carts/" + id);
    }
}