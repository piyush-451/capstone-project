package com.Capstone.Ecommerce.Product;

import com.Capstone.Ecommerce.Utils.UtilsFunctions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final UtilsFunctions conversionUtils;

    @GetMapping("/")
    public ResponseEntity<List<ProductResponse>> getAllProduct(){
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody @Valid ProductRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        System.out.println("get product by id");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.getProductById(id));
    }

//    @PatchMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_INTERNAL')")
//    public ResponseEntity<ProductResponse> updateProductById(@PathVariable Long id,@RequestBody ProductRequest request){
//        System.out.println("productController");
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.updateProductById(id,request));
//    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_INTERNAL')")
    public ResponseEntity<ProductResponse> purchaseProductById(@PathVariable Long id,@RequestBody ProductRequest request){
        System.out.println("productController put");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.updateProductById(id,request));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductResponse> deleteProductById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(productService.deleteProductById(id));
    }


}
