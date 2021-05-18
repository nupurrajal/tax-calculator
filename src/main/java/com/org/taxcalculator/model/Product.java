package com.org.taxcalculator.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class Product {
    int quantity;
    String name;
    double price;
    String category;
}
