package com.icore.ecommerce_platform.config;

import com.icore.ecommerce_platform.dao.ProductRepository;
import com.icore.ecommerce_platform.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds a few sample products on startup — but only under the {@code demo} profile,
 * so demo deployments have a populated catalog while a real production DB stays clean.
 */
@Component
@Profile("demo")
public class DemoDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataInitializer.class);

    private final ProductRepository productRepository;

    public DemoDataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return; // catalog already populated — don't duplicate
        }

        List<Product> products = List.of(
                buildProduct("OnePlus 12", "OnePlus", "Mobiles", 68999, 10),
                buildProduct("iPhone 15",  "Apple",   "Mobiles", 72500, 5),
                buildProduct("PS5",        "Sony",    "Gaming",  49500, 8),
                buildProduct("ROG Ally",   "Asus",    "Gaming",  59400, 3),
                buildProduct("HP Victus",  "HP",      "Laptops", 67900, 6)
        );
        productRepository.saveAll(products);
        log.info("Seeded {} demo products", products.size());
    }

    private Product buildProduct(String name, String brand, String category, double price, int stock) {
        Product p = new Product();
        p.setProductName(name);
        p.setBrandName(brand);
        p.setProductCategory(category);
        p.setProductPrice(price);
        p.setProductStatus(true);
        p.setStock(stock);
        return p;
    }
}