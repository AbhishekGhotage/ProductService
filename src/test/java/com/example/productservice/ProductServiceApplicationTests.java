package com.example.productservice;

import com.example.productservice.configurations.projections.ProductWithIdAndTitle;
import com.example.productservice.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ProductServiceApplicationTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testDBQueries(){
        Optional<ProductWithIdAndTitle> optionalProductWithIdAndTitle = productRepository.randomSearchMethod(1L);

        System.out.println("id : " + optionalProductWithIdAndTitle.get().getId() + " " + "\n" + "title : " + optionalProductWithIdAndTitle.get().getTitle());
    }
}