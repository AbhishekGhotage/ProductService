package com.example.productservice.services;

import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Product;
import com.example.productservice.repositories.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("SelfProductService")
public class SelfProductService implements ProductService {
    private ProductRepository productRepository;
    private RedisTemplate<String, Object> redisTemplate;

    public SelfProductService(ProductRepository productRepository, RedisTemplate<String, Object> redisTemplate) {
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {

        // Make a call to DB to fetch a product with given id.
//        Optional<Product> optionalProduct = Optional.ofNullable(productRepository.findById(productId)
//                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found."))
//        );
//        return optionalProduct.get();

//      We can use either above or below method to return Product.

        // Check if this product is available in REDIS(cache) or not?
        Product product = (Product) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCT_" + productId);

        // Cache HIT(i.e., Product is found in the cache)
        if (product != null) {
            return product;
        }

        // Cache MISS(i.e., Product is not found in the cache)
        // Make a call to DB to fetch a product with given id.
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Product with id " + productId + " not found.");
        }

        Product product1 = optionalProduct.get();

        // Before returning the product to the user or client, store it in the Redis(cache).
        redisTemplate.opsForHash().put("PRODUCTS", "PRODUCT_" + productId, product1);

        return product1;


//        Optional<ProductWithIdAndTitle> optionalProductWithIdAndTitle = productRepository.randomSearchMethod(productId);
//        if (optionalProductWithIdAndTitle.isEmpty()) {
//            throw new ProductNotFoundException("Product with id " + productId + " not found.");
//        }
//
//        Product product = new Product();
//        product.setId(optionalProductWithIdAndTitle.get().getId());
//        product.setTitle(optionalProductWithIdAndTitle.get().getTitle());
//
//        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllTheProducts(int pageNumber, int pageSize) {

//        Sort sort = Sort.by("price").ascending().and(Sort.by("title").descending());
//        Sort.by("price").ascending().and(Sort.by("title").ascending()).and(Sort.by("quantity").ascending());


        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("price").ascending());
        return productRepository.findAll(pageRequest).getContent();

//        Either you can above or below method to return List<Product>.
//        return productRepository.findAll(
//                PageRequest.of(pageNumber,
//                        pageSize,
//                        Sort.by("price").ascending()))
//                .getContent();
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        return null;
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Product with id " + id + " not found.");
        }

        productRepository.deleteById(id);
    }

    @Override
    public Product addNewProduct(Product product) {
        return productRepository.save(product);
    }
}