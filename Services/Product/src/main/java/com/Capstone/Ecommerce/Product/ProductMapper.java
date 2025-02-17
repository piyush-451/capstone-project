package com.Capstone.Ecommerce.Product;

import com.Capstone.Ecommerce.Category.Category;
import com.Capstone.Ecommerce.Category.CategoryRepo;
import com.Capstone.Ecommerce.Category.CategoryRequest;
import com.Capstone.Ecommerce.Exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryRepo categoryRepo;

    public Product toProduct(ProductRequest request) {
        Set<Category> categories = request.categories().stream()
                .map(category -> categoryRepo.findByName(category)
                        .orElseThrow(() -> new ResourceNotFoundException("Product has non existent category")))
                .collect(Collectors.toSet());

        return Product.builder()
                .name(request.name())
                .description(request.description())
                .availableQuantity(request.availableQuantity())
                .price(request.price())
                .categories(categories)
                .build();
    }

    public ProductResponse toProductResponse(Product product){
        Set<String> categories = product.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet());

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getPrice(),
                categories
        );
    }
}
