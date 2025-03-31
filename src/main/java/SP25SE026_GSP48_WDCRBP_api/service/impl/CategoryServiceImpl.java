package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.dto.CategoryDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Category;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CategoryRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CategoryRes;
import SP25SE026_GSP48_WDCRBP_api.repository.CategoryRepository;
import SP25SE026_GSP48_WDCRBP_api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> listAllCategory() {
        List<Category> allCategories = categoryRepository.findAll();
        List<CategoryDto> levelOneCategories = new ArrayList<>();

        // Only grab level 1 categories
        for (Category category : allCategories) {
            if ("1".equals(category.getCategoryLevel())) {
                CategoryDto dto = convertToDto(category);
                dto.setChildren(buildChildren(allCategories, category.getCategoryId()));
                levelOneCategories.add(dto);
            }
        }
        return levelOneCategories;
    }

    // Recursively build children (works for level 2, 3, 4, ...)
    private List<CategoryDto> buildChildren(List<Category> allCategories, Long parentId) {
        List<CategoryDto> children = new ArrayList<>();
        for (Category category : allCategories) {
            if (parentId.equals(category.getParentId())) {
                CategoryDto childDto = convertToDto(category);
                childDto.setChildren(buildChildren(allCategories, category.getCategoryId()));
                children.add(childDto);
            }
        }
        return children.isEmpty() ? null : children;
    }

    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        dto.setDescription(category.getDescription());
        dto.setCategoryLevel(category.getCategoryLevel());
        // children will be set separately
        return dto;
    }

    @Override
    public CategoryRes createCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setParentId(categoryRequest.getParentId());
        category.setCategoryName(categoryRequest.getCategoryName());
        category.setDescription(categoryRequest.getDescription());
        category.setCategoryLevel(categoryRequest.getCategoryLevel());
        category.setStatus(categoryRequest.getStatus());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryRes(savedCategory);
    }

    @Override
    public CategoryRes getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(this::convertToCategoryRes)
                .orElse(null); // return null if not found
    }

    @Override
    public CategoryRes updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));

        existingCategory.setParentId(categoryRequest.getParentId());
        existingCategory.setCategoryName(categoryRequest.getCategoryName());
        existingCategory.setDescription(categoryRequest.getDescription());
        existingCategory.setCategoryLevel(categoryRequest.getCategoryLevel());
        existingCategory.setStatus(categoryRequest.getStatus());
        existingCategory.setUpdatedAt(LocalDateTime.now());

        Category updatedCategory = categoryRepository.save(existingCategory);
        return convertToCategoryRes(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
        categoryRepository.delete(category);
    }

    private CategoryRes convertToCategoryRes(Category category) {
        return CategoryRes.builder()
                .categoryId(category.getCategoryId())
                .parentId(category.getParentId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .categoryLevel(category.getCategoryLevel())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .status(category.isStatus())
                .build();
    }
}

