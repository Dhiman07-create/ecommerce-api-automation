package clients;

import models.Product;
import io.restassured.response.Response;


import java.util.List;


public class ProductsClient extends ApiClient {
    public List<Product> search(String q) {
        Response res = get("/products/search?q=" + q);
        System.out.println("Search api response: "+res.asPrettyString());
        return res.jsonPath().getList("products", Product.class);
    }


    public Product getById(int id) {
        Response res = get("/products/" + id);
        return res.as(Product.class);
    }
}