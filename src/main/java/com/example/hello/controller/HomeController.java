package com.example.hello.controller;

import com.example.hello.model.Product;
import com.example.hello.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        List<String> categories = products.stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("categories", categories);
        return "home";
    }

    @GetMapping("/products")
    public String search(@RequestParam(required = false) String query, @RequestParam(required = false) String category, Model model) {
        List<Product> products = productService.getAllProducts();
        List<Product> filtered = products;
        if (query != null && !query.isEmpty()) {
            filtered = filtered.stream()
                    .filter(p -> p.getTitle().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (category != null && !category.isEmpty()) {
            filtered = filtered.stream()
                    .filter(p -> p.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        
        List<String> categories = products.stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("categories", categories);
        
        model.addAttribute("products", filtered);
        return "product-list";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id).orElse(null);
        model.addAttribute("product", product);
        return "product-detail";
    }
}
