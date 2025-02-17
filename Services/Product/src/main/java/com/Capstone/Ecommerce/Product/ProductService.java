package com.Capstone.Ecommerce.Product;

import com.Capstone.Ecommerce.Category.Category;
import com.Capstone.Ecommerce.Category.CategoryRepo;
import com.Capstone.Ecommerce.Exceptions.ProductCurrentlyUnavailableException;
import com.Capstone.Ecommerce.Exceptions.ResourceNotFoundException;
import com.Capstone.Ecommerce.Utils.UtilsFunctions;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final CategoryRepo categoryRepo;
    private final UtilsFunctions utilsFunctions;

    public List<ProductResponse> getAllProduct() {

        List<Product> productList = productRepo.findAll();

        return productList.stream()
                .filter(product -> product.getAvailableQuantity()>0)
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse addProduct(ProductRequest request) {
        request.categories()
                .forEach(category -> {
                    Optional<Category> existingCategory = categoryRepo.findByName(category);
                    if(existingCategory.isEmpty()){
                        categoryRepo.save(Category.builder()
                                        .name(category)
                                        .build());
                    }

                });

        Product product = productMapper.toProduct(request);
        product = productRepo.save(product);

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getPrice(),
                request.categories()
        );

    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("product with given id do not exist"));

        if(product.getAvailableQuantity()<0){
            throw new ProductCurrentlyUnavailableException("product is currently not available");
        }


        return productMapper.toProductResponse(product);

    }

    @Transactional
    public ProductResponse updateProductById(Long id, ProductRequest request) {
        Product product = productRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("product with given id do not exist"));

        System.out.println("product exist in update method");

        utilsFunctions.updateObject(request,product);

        if(request.categories()!=null) {
            request.categories()
                    .forEach(category -> {
                        Optional<Category> existingCategory = categoryRepo.findByName(category);
                        if (existingCategory.isEmpty()) {
                            categoryRepo.save(Category.builder()
                                    .name(category)
                                    .build());
                        }

                    });

            Set<Category> categories = request.categories().stream()
                    .map(category -> categoryRepo.findByName(category)
                            .orElseThrow(() -> new ResourceNotFoundException("Category do not exist")))
                    .collect(Collectors.toSet());

            product.setCategories(categories);
        }

        product = productRepo.save(product);
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getPrice(),
                request.categories()
        );

    }

    @Transactional
    public ProductResponse deleteProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("product with given id do not exist"));

        productRepo.delete(product);

        return productMapper.toProductResponse(product);
    }
}
