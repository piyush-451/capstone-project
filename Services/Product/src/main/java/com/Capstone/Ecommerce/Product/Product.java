package com.Capstone.Ecommerce.Product;

import com.Capstone.Ecommerce.Category.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Check(constraints = "available_quantity >= 0")
    private double availableQuantity;

    private String description;

    @Check(constraints = "price > 0")
    private BigInteger price;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
//    @JsonIgnoreProperties(value = {"id","description"})  //only deserialize if product is fetched... but if we assign the category to ProductResponse it will pass the entire category object
    private Set<Category> categories = new HashSet<>();
}
