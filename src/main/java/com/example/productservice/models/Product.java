package com.example.productservice.models;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product extends BaseModel {
    private String title;
    private double price;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Category category;
}

/*
Cardinality: -

   1             1
Product ----- Category ==> M:1
   M             1
 */