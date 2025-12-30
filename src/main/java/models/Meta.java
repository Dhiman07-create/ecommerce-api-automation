package models;

import lombok.Data;

@Data
public class Meta {
    private String createdAt;
    private String updatedAt;
    private String barcode;
    private String qrCode;
}