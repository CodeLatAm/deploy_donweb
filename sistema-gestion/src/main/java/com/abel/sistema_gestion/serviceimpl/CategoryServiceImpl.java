package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.entity.Category;
import com.abel.sistema_gestion.exception.CategoryNotFountException;
import com.abel.sistema_gestion.repository.CategoryRepository;
import com.abel.sistema_gestion.serviceimpl.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
            throw new CategoryNotFountException("Categoria no encontrada con id: " + categoryId);
        });
        return category;
    }
}
