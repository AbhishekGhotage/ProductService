package com.example.productservice.contollers;


import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Product;
import com.example.productservice.repositories.CategoryRepository;
import com.example.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private CategoryRepository categoryRepository;
    private ProductService productService;

    public ProductController(@Qualifier("SelfProductService") ProductService productService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    // http://localhost:8080/products/10
    @GetMapping("/{id}")
//    public Product getProductById(@PathVariable("id") Long id) {
//// return new Product();
//        return productService.getSingleProduct(id);
//    }
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) throws ProductNotFoundException {

        // Method 1 to create and return ResponseEntity<Product>.
//        Product product = productService.getSingleProduct(id);

//        Method 1.1: -
//        if(product == null){
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(product);

//        Method 1.2: -
//        return new ResponseEntity<>(product, HttpStatus.OK);


//        Method 2 to create and return ResponseEntity<Product>.
//        ResponseEntity<Product> responseEntity = new ResponseEntity<>(
//                productService.getSingleProduct(id),
//                HttpStatus.OK
//        );
//        return responseEntity;


        // Exception Handling: -
        // Method 1: - Not a good practice to write try and catch block inside the controller methods as the controller should be
        // minimal as possible. Hence, we have created GlobalExceptionHandler class inside the controller-advice package to catch exceptions across all the controllers.
//        ResponseEntity<Product> responseEntity = null;
//        try {
//            responseEntity = new ResponseEntity<>(
//                    productService.getSingleProduct(id),
//                    HttpStatus.OK
//            );
//        } catch (Exception e) {
//            responseEntity = new ResponseEntity<>(
//                    HttpStatus.NOT_FOUND
//            );
//        }
//        return responseEntity;

        // Method 2: - If exceptions happen in below code, then it will be taken care by GlobalExceptionHandler class inside the controller-advice package.
        ResponseEntity<Product> responseEntity = new ResponseEntity<>(
                productService.getSingleProduct(id),
                HttpStatus.OK
        );
        return responseEntity;
    }

//    @GetMapping()
//    public List<Product> getAllProducts() {
//        return productService.getAllProducts();
//    }

    @GetMapping()
    public List<Product> getAllTheProducts(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        return productService.getAllTheProducts(pageNumber, pageSize);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Long productId) throws ProductNotFoundException {
        productService.deleteProduct(productId);
    }

    // PATCH --> http://localhost:8080/products/10
    @PatchMapping("/{id}")
    public Product updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @PutMapping("/{id}")
    public Product replaceProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        return null;
    }

    @PostMapping
    public Product addNewProduct(@RequestBody Product product){
//        Category category = product.getCategory();
//        if (category.getId() == null) {
//            // We need to create a Category object in the dB first.
//            category = categoryRepository.save(category);
//        }
        return productService.addNewProduct(product);
    }
}