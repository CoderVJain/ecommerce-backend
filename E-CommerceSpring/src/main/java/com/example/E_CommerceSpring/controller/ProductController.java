package com.example.E_CommerceSpring.controller;

import com.example.E_CommerceSpring.exception.ProductException;
import com.example.E_CommerceSpring.model.Product;
import com.example.E_CommerceSpring.model.Size;
import com.example.E_CommerceSpring.service.ProductService;
import com.example.E_CommerceSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "colors", required = false) List<String> colors,
            @RequestParam(name = "sizes", required = false) List<String> sizes,
            @RequestParam(name = "minPrice", required = false, defaultValue = "0") Integer minPrice,
            @RequestParam(name = "maxPrice", required = false, defaultValue = "1000000") Integer maxPrice,
            @RequestParam(name = "minDiscount", required = false, defaultValue = "0") Integer minDiscount,
            @RequestParam(name = "sort", required = false, defaultValue = "") String sort,
            @RequestParam(name = "stock", required = false, defaultValue = "") String stock,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize
    ) {
        Page<Product> res = productService.getAllProduct(
                category, colors, sizes, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize
        );

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/product/id/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) throws ProductException {
        Product product = productService.getProductById(productId);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @GetMapping("/products/filters")
    public ResponseEntity<Map<String, List<String>>> getFilters(@RequestParam String category) {
        return new ResponseEntity<Map<String, List<String>>>(productService.getFilterOptions(category), HttpStatus.OK);
    }


}
