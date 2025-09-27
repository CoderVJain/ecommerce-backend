package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.exception.ProductException;
import com.example.E_CommerceSpring.model.Product;
import com.example.E_CommerceSpring.model.Size;
import com.example.E_CommerceSpring.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ProductService {

    public Product createProduct(CreateProductRequest req);
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes,
                                       Integer minPrice, Integer maxPrice, Integer minDiscount,
                                       String sort, String stock, Integer pageNumber, Integer pageSize);
    public List<Product> getProductByCategory(String category);
    public Product getProductById(Long id) throws ProductException;
    public void deleteProduct(Long id) throws ProductException;
    public Product updateProduct(Long id, Product req) throws ProductException;
    public Map<String, List<String>> getFilterOptions(String category);


}
