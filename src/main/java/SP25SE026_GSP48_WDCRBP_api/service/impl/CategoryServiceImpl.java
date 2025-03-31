package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.CategoryChildrenDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.CategoryDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Category;
import SP25SE026_GSP48_WDCRBP_api.repository.CategoryRepository;
import SP25SE026_GSP48_WDCRBP_api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

