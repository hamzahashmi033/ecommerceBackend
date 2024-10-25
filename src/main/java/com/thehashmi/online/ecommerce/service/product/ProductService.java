package com.thehashmi.online.ecommerce.service.product;

import com.thehashmi.online.ecommerce.dto.CategoryDto;
import com.thehashmi.online.ecommerce.dto.ImageDto;
import com.thehashmi.online.ecommerce.dto.ProductDto;
import com.thehashmi.online.ecommerce.model.Images;
import com.thehashmi.online.ecommerce.repository.CategoryRepository;
import com.thehashmi.online.ecommerce.repository.ImageRepository;
import com.thehashmi.online.ecommerce.repository.ProductRepository;
import com.thehashmi.online.ecommerce.model.Category;
import com.thehashmi.online.ecommerce.model.Product;
import com.thehashmi.online.ecommerce.request.AddProductRequest;
import com.thehashmi.online.ecommerce.request.UpdateProductRequest;
import com.thehashmi.online.ecommerce.utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request
                        .getCategory().getName()))
                .orElseGet(()->{
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }
    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getDescription(),
                request.getPrice(),
                request.getInventory(),
                category

        );
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new NotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            productRepository.deleteById(id);
        }else{
            throw new NotFoundException("Product not found");
        }
    }

    @Override
    public Product updateProductById(UpdateProductRequest request, Long id) {
        Optional<Product> isProductExits = productRepository.findById(id);
        if(isProductExits.isPresent()){
            Product existingProduct = isProductExits.get();
            Product updatedProduct = updateExistingProduct(existingProduct,request);
            return productRepository.save(updatedProduct);
        }
        throw new NotFoundException("Product not found!");
    }
    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        // Update the product's fields with the values from the request
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        // Find the category by name and set it for the existing product
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

        return existingProduct;
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getAllProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public int countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product : products){
            productDtos.add(convertToDto(product));
        }
        return productDtos;
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product,ProductDto.class);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(product.getCategory().getId());
        categoryDto.setName(product.getCategory().getName());
        productDto.setCategory(categoryDto);

        List<Images> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = new ArrayList<>();
        for(Images image : images){
            ImageDto imageDto = modelMapper.map(image,ImageDto.class);
            imageDtos.add(imageDto);
        }
        productDto.setImages(imageDtos);
        return productDto;
    }
}
