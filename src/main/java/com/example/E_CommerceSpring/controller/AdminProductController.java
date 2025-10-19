package com.example.E_CommerceSpring.controller;

import com.example.E_CommerceSpring.exception.ProductException;
import com.example.E_CommerceSpring.model.Product;
import com.example.E_CommerceSpring.model.Size;
import com.example.E_CommerceSpring.request.CreateProductRequest;
import com.example.E_CommerceSpring.response.ApiResponse;
import com.example.E_CommerceSpring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest req) {
        Product product = productService.createProduct(req);
        return new ResponseEntity<Product>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/softDelete/{id}")
    public ResponseEntity<String> softDeleteProduct(@PathVariable Long id) {
        try {
            productService.softDeleteProduct(id);
            return ResponseEntity.ok("✅ Product soft-deleted successfully (set inactive).");
        } catch (ProductException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    @DeleteMapping("/permanentDelete/{id}")
    public ResponseEntity<String> deleteProductPermanently(@PathVariable Long id) {
        try {
            productService.deleteProductPermanently(id);
            return ResponseEntity.ok("🗑️ Product permanently deleted from database.");
        } catch (ProductException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "color", required = false) List<String> color,
            @RequestParam(name = "sizes", required = false) List<String> sizes,
            @RequestParam(name = "minPrice", required = false, defaultValue = "0") Integer minPrice,
            @RequestParam(name = "maxPrice", required = false, defaultValue = "1000000") Integer maxPrice,
            @RequestParam(name = "minDiscount", required = false, defaultValue = "0") Integer minDiscount,
            @RequestParam(name = "sort", required = false, defaultValue = "") String sort,
            @RequestParam(name = "stock", required = false, defaultValue = "") String stock,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize
    ) {
        // The controller captures all parameters from the URL and passes them to the service.
        Page<Product> products = productService.getAllProduct(
                category, color, sizes, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize
        );
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                 @RequestBody Product req) throws ProductException {
        Product updatedProduct = productService.updateProduct(productId, req);
        return new ResponseEntity<Product>(updatedProduct, HttpStatus.CREATED);
    }

    @PostMapping("/creates")
    public ResponseEntity<ApiResponse> createMultipleProducts(@RequestBody CreateProductRequest[] req) {
        for(CreateProductRequest r: req) {
            productService.createProduct(r);
        }
        ApiResponse res = new ApiResponse();
        res.setMessage("Products Created Successfully");
        res.setStatus(true);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.CREATED);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<Product>> getInactiveProducts() {
        List<Product> inactiveProducts = productService.getInactiveProducts();
        return new ResponseEntity<List<Product>>(inactiveProducts,HttpStatus.OK);
    }

    @PutMapping("/{productId}/activate")
    public ResponseEntity<ApiResponse> activateProduct(@PathVariable Long productId) {
        try {
            Product activatedProduct = productService.makeProductActive(productId);
            ApiResponse response = new ApiResponse("Product activated successfully", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ProductException e) {
            ApiResponse response = new ApiResponse(e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}

