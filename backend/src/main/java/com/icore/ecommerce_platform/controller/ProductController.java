package com.icore.ecommerce_platform.controller;

import com.icore.ecommerce_platform.dao.ProductRepository;
import com.icore.ecommerce_platform.dto.UserPublicAccessDto;
import com.icore.ecommerce_platform.entity.Product;
import com.icore.ecommerce_platform.service.ProductService;
import com.icore.ecommerce_platform.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST endpoints for admin-only operations under {@code /api/admin}:
 * managing the product catalog and listing users. Restricted to the
 * {@code ADMIN} authority.
 */
@RestController
@RequestMapping("api/admin")
public class ProductController {

    private ProductService productService;
    private ProductRepository productRepository;
    private UserService userService;

    public ProductController(ProductService productService, ProductRepository productRepository, UserService userService) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.userService = userService;
    }


    @PostMapping("/product/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product saved = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/product/add_list")
    public ResponseEntity<List<Product>> addProduct(@RequestBody List<Product> productList) {
        List<Product> saved = productService.addProduct(productList);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/product/remove/{id}")
    public ResponseEntity<Void> removeProduct(@PathVariable("id") Integer productId) {
        productService.removeProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/product/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Map<String, Object> fields) {
        Product updated = productService.updateProduct(id, fields);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserPublicAccessDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
