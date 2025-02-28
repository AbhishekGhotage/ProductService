package com.example.productservice.repositories;

import com.example.productservice.configurations.projections.ProductWithIdAndTitle;
import com.example.productservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //Product Repo should contain all the methods (CRUD) related to a Product model.

    // Declared Queries: -
    /* Sample Methods: -
    List<Product> findByPriceIsGreaterThan(Double price);
    //select * from products where price >?

    List<Product> findProductByTitleLike(String word); // case-sensitive
    //select * from products where title like '%iPhone'

    List<Product> findByTitleLikeIgnoreCase(String word); // case-insensitive.

    List<Product> findTop5ByTitleContains(String word);
    //select * from products where title like '' LIMIT 5

    List<Product> findTopByTitleContains(int top, String word);

    List<Product> findProductsByTitleContainsAndPriceGreaterThan(
            String word,
            Double price);

    List<Product> findProductsByTitleContainsOrderById(String word);
    */

    Optional<Product> findById(Long id);

//    List<Product> findAll();

    @Override
    Page<Product> findAll(Pageable pageable);

    void deleteById(Long id);

    // HQL: -(dB independent)
    @Query("select p.id as id, p.title as title from Product p where p.id = :x")
    Optional<ProductWithIdAndTitle> randomSearchMethod(Long x); // To see the result of this query, go to the test file.


    // Native Queries(SQL): -(db dependent)
    @Query(value = "select p.id as id, p.title as title from product p where p.id = :x", nativeQuery = true)
    Optional<ProductWithIdAndTitle> randomSearchMethod2(Long x);
}

/*
1. Repository should be an interface.
2. Repository should extend JPARepository.


Product product = productRepository.findById(100);
If we get null product then, while executing the below line, we will get NPE.
String title = p.getTitle(); // NPE

Hence we have used Optional<Product> instead of only Product to avoid NPE or any other E.
Optional<Product> optionalProduct = productRepository.findById(100);

*/