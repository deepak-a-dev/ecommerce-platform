package com.icore.ecommerce_platform.controller;

import com.icore.ecommerce_platform.dao.ProductRepository;
import com.icore.ecommerce_platform.entity.Product;
import com.icore.ecommerce_platform.entity.User;
import com.icore.ecommerce_platform.service.ProductService;
import com.icore.ecommerce_platform.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public String addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PostMapping("/product/add_list")
    public String addProduct(@RequestBody List<Product> productList) {
        return productService.addProduct(productList);
    }

    @DeleteMapping("/product/remove/{id}")
    public String removeProduct(@PathVariable("id") Integer productId) {
        return productService.removeProduct(productId);
    }

//    @PatchMapping("/product/update/{id}")
//    public String updateProduct(@PathVariable Integer id,
//                                @RequestParam(required = false) String productName,
//                                @RequestParam(required = false) String brandName,
//                                @RequestParam(required = false) String productCategory) {
//        productService.updateProduct(id, productName, brandName, productCategory);
//        return "PRODUCT UPDATED SUCCESSFULLY";
//    }

    @PatchMapping("/product/update/{id}")
    public String updateProduct(@PathVariable Integer id, @RequestBody Map<String, Object> fields) {
        return productService.updateProduct(id, fields);
    }

    @GetMapping("/getUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
