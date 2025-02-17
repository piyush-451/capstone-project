package com.Capstone.Ecommerce.Product;

import com.Capstone.Ecommerce.Category.Category;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import org.hibernate.annotations.Check;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public record ProductResponse(
        Long id,

        String name,

        String description,

        double availableQuantity,

        BigInteger price,

        Set<String> categories

) {
}
