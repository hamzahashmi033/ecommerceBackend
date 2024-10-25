package com.thehashmi.online.ecommerce.service.category;


import com.thehashmi.online.ecommerce.repository.CategoryRepository;
import com.thehashmi.online.ecommerce.model.Category;
import com.thehashmi.online.ecommerce.utils.AlreadyExistsException;
import com.thehashmi.online.ecommerce.utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        if(categoryRepository.existsByName(category.getName())){
            throw new AlreadyExistsException(category.getName() +" already exists!");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Category oldCategory = getCategoryById(id);
        if(oldCategory == null){
            throw new NotFoundException("Category not found");
        }
        oldCategory.setName(category.getName());
        return categoryRepository.save(oldCategory);
    }

    @Override
    public void deleteCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            throw new NotFoundException("Category not found");
        }
        categoryRepository.delete(category);
    }
}
