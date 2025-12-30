package models;

import lombok.Data;

import java.util.List;


@Data
public class Product {
    private int id;
    private String title;
    private String description;

    private double price;
    private double discountPercentage;
    private double rating;
    private int stock;

    private String brand;
    private String category;
    private String sku;

    private int weight;
    private int minimumOrderQuantity;

    private String warrantyInformation;
    private String shippingInformation;
    private String availabilityStatus;
    private String returnPolicy;

    private String thumbnail;

    // arrays
    private List<String> tags;
    private List<String> images;

    // nested objects
    private Dimensions dimensions;
    private Meta meta;

    // list of objects
    private List<Review> reviews;
}