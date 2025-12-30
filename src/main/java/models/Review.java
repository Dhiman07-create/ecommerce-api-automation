package models;

import lombok.Data;

@Data
public class Review {
    private int rating;
    private String comment;
    private String date;
    private String reviewerName;
    private String reviewerEmail;
}