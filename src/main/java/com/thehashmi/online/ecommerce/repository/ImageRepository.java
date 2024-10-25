package com.thehashmi.online.ecommerce.repository;

import com.thehashmi.online.ecommerce.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ImageRepository extends JpaRepository<Images,Long> {
    List<Images> findByProductId(Long id);

}
