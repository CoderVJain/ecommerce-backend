package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.exception.ProductException;
import com.example.E_CommerceSpring.model.Category;
import com.example.E_CommerceSpring.model.Product;
import com.example.E_CommerceSpring.model.Size;
import com.example.E_CommerceSpring.repo.CategoryRepository;
import com.example.E_CommerceSpring.repo.ProductRepository;
import com.example.E_CommerceSpring.request.CreateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest req) {

        Category topLevel = categoryRepository.findByName(req.getTopLevelCategory());

        if (topLevel == null) {
            Category topLevelCategory = new Category();
            topLevelCategory.setName(req.getTopLevelCategory());
            topLevelCategory.setLevel(1);
            topLevel = categoryRepository.save(topLevelCategory); // 👈 Reassigning the saved entity
        }

        System.out.println(topLevel.getName());

        Category secondLevel = categoryRepository.findByNameAndParent(req.getSecondLevelCategory(),
                topLevel.getName());
        if (secondLevel == null) {
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(req.getSecondLevelCategory());
            secondLevelCategory.setParentCategory(topLevel);
            secondLevelCategory.setLevel(2);

            secondLevel = categoryRepository.save(secondLevelCategory); // 👈 Reassigning the saved entity
        }

        System.out.println(secondLevel.getName());

        Category thirdLevel = categoryRepository.findByNameAndParent(req.getThirdLevelCategory(),
                secondLevel.getName());
        if (thirdLevel == null) {
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(req.getThirdLevelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);

            thirdLevel = categoryRepository.save(thirdLevelCategory); // 👈 Reassigning the saved entity
        }

        System.out.println(thirdLevel.getName());

        Product product = new Product();
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscountedPrice(req.getDiscountedPrice());
        product.setDiscountPercent(req.getDiscountPercent());
        product.setImageUrl(req.getImageUrl());
        product.setBrand(req.getBrand());
        product.setPrice(req.getPrice());
        product.setSizes(req.getSize());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());
        product.setQuantity(req.getQuantity());

        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors,
          List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort,
          String stock, Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);

        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);


        if(colors != null && !colors.isEmpty()) {
            products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor()))).collect(Collectors.toList());
        }

        if(stock != null) {
            if(stock.equals("in_stock")) {
                products  = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            }
            else if (stock.equals("out_of_stock")) {
                products  = products.stream().filter(p -> p.getQuantity() <= 0).collect(Collectors.toList());
            }
        }

        if (sizes != null && !sizes.isEmpty()) {
            products = products.stream()
                    .filter(product -> product.getSizes().stream()
                            .anyMatch(size -> sizes.contains(size.getName())))
                    .collect(Collectors.toList());
        }

        int totalElements = products.size();
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), totalElements);

        // Check if the filtered list has enough elements for the requested page
        if (startIndex > endIndex || startIndex >= totalElements) {
            // If the page is out of bounds, return an empty page
            return new PageImpl<>(List.of(), pageable, totalElements);
        }

        List<Product> pageContent = products.subList(startIndex, endIndex);
        Page<Product> page = new PageImpl<>(pageContent, pageable, totalElements);
        System.out.println(page);
        return page;
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return null;
    }

    @Override
    public Product getProductById(Long id) throws ProductException {
        Optional<Product> opt = productRepository.findById(id);

        if(opt.isPresent()) {
            return opt.get();
        }
        else {
            throw new ProductException("Product not found with id " + id);
        }
    }

    @Override
    public void deleteProduct(Long id) throws ProductException {
        Product product = getProductById(id);
        if (product == null) {
            throw new ProductException("Product not found with id: " + id);
        }

        product.getSizes().clear();
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long id, Product req) throws ProductException {
        Product product = getProductById(id);

        if(req.getQuantity() != 0 ) {
            product.setQuantity(req.getQuantity());

        }
        return productRepository.save(product);
    }
    @Override
    public Map<String, List<String>> getFilterOptions(String category) {

        System.out.println(category);
        List<Product> products = productRepository.findAll()
                .stream()
                .filter(p -> p.getCategory().getName().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        Set<String> colors = products.stream()
                .map(Product::getColor)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        System.out.println(colors);

        Set<String> brands = products.stream()
                .map(Product::getBrand)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> sizes = products.stream()
                .flatMap(p -> p.getSizes().stream())
                .map(Size::getName)
                .collect(Collectors.toSet());

        List<String> stock = List.of("in_stock", "out_of_stock");

        Map<String, List<String>> filters = new HashMap<>();
        filters.put("colors", new ArrayList<>(colors));
        filters.put("brands", new ArrayList<>(brands));
        filters.put("sizes", new ArrayList<>(sizes));
        filters.put("stock", stock);

        return filters;
    }

}
