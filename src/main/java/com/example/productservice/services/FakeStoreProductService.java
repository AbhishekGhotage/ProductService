package com.example.productservice.services;

import com.example.productservice.dtos.FakeStoreProductDto;
import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service("FakeStoreProductService")
public class FakeStoreProductService implements ProductService {
    private RestTemplate restTemplate;
    private RedisTemplate<String, Object> redisTemplate;

    public FakeStoreProductService(RestTemplate restTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException{

//        throw new RuntimeException("Something went wrong");

        // Check if this product is available in REDIS(cache) or not?
        Product product = (Product) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCT_" + productId);

        // Cache HIT(i.e., Product is found in the cache)
        if (product != null) {
            return product;
        }

        // Cache MISS(i.e., Product is not found in the cache)
        // Call FakeStore to fetch the Product with given id. ==> HTTP Call.
        FakeStoreProductDto fakeStoreProductDto = restTemplate.getForObject(
                "https://fakestoreapi.com/products/" + productId,
                FakeStoreProductDto.class
        );
        if(fakeStoreProductDto == null){
            throw new ProductNotFoundException("Product with id " + productId + " not found.");
        }

        // Before returning the product to the user or client, store it in the Redis(cache).
        product = convertFakeStoreProductDtoToProduct(fakeStoreProductDto);
        redisTemplate.opsForHash().put("PRODUCTS", "PRODUCT_" + productId, product);

        // Convert FakeStoreProductDto into Product.
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreProductDto[] fakeStoreProductDtos = restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                FakeStoreProductDto[].class
        );

        // Convert the List of FakeStoreProductDtos into Products.
        // Method 1: - Using for loop
        List<Product> products = new ArrayList<>();
        for(FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtos){
            products.add(convertFakeStoreProductDtoToProduct(fakeStoreProductDto));
        }
        return products;

        // Method 2: - Using stream
//        return List.of(fakeStoreProductDtos).stream()
//                .map(this::convertFakeStoreProductDtoToProduct)
//                .toList();
    }

    @Override
    public List<Product> getAllTheProducts(int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public Product updateProduct(Long id, Product product) {

        // Original Method: -
//        RequestCallback requestCallback = this.httpEntityCallback(request, responseType);
//        HttpMessageConverterExtractor<T> responseExtractor =
//                new HttpMessageConverterExtractor(responseType, this.getMessageConverters());
//
//        return (T)this.execute(url, HttpMethod.PATCH, requestCallback, responseExtractor);

        // Updated Method: -
        RequestCallback requestCallback = restTemplate.httpEntityCallback(product, FakeStoreProductDto.class);

        HttpMessageConverterExtractor<FakeStoreProductDto> responseExtractor =
                new HttpMessageConverterExtractor(FakeStoreProductService.class, restTemplate.getMessageConverters());

        FakeStoreProductDto response = restTemplate.execute("https://fakestoreapi.com/products" + id,
                HttpMethod.PATCH, requestCallback, responseExtractor
        );

        // Convert FakeStoreProductDto into Product.
        return convertFakeStoreProductDtoToProduct(response);
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }

    @Override
    public Product addNewProduct(Product product) {
        return null;
    }

    private Product convertFakeStoreProductDtoToProduct(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setPrice(fakeStoreProductDto.getPrice());

        Category category = new Category();
        category.setDescription(fakeStoreProductDto.getDescription());
        product.setCategory(category);

        return product;
    }
}