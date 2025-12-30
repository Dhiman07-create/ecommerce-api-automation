package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Cart {
    private int id;
    private int userId;
    private List<CartProduct> products;
    private double total;
    private double discountedTotal;
    private int totalProducts;
    private int totalQuantity;
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
class CartProduct {
    private int id;
    private String title;
    private double price;
    private int quantity;
    private double total;
    private double discountPercentage;
    private double discountedPrice;
    private double discountedTotal;
    private String thumbnail;
}
