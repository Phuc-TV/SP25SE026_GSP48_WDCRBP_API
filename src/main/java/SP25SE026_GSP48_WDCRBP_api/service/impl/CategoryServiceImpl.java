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
    public List<CategoryDto> listAllCategory()
    {
        List<Category> categoriesList = new ArrayList<>();
        List<Category> categorie = categoryRepository.findAll();

        for (Category category : categorie)
        {
            if (category.getParentId() != null)
            {
                categoriesList.add(category);
            }
        }

        List<CategoryDto> categories = new ArrayList<>();

        for (Category category : categoriesList)
        {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryLevel("1");
            categoryDto.setCategoryName(category.getCategoryName());
            categoryDto.setDescription(category.getDescription());

            List<CategoryChildrenDto> categoryChildrenDtos = new ArrayList<>();

            for (Category categoryy : categorie)
            {
                if (categoryy.getParentId().equals(category.getCategoryId()))
                {
                    CategoryChildrenDto categoryChildrenDto = new CategoryChildrenDto();
                    categoryChildrenDto.setCategoryName(categoryy.getCategoryName());
                    categoryChildrenDto.setCategoryLevel("2");
                    categoryChildrenDto.setDescription(categoryy.getDescription());

                    categoryChildrenDtos.add(categoryChildrenDto);
                }
            }
            categoryDto.setChildren(categoryChildrenDtos);
            categories.add(categoryDto);
        }

        return categories;
    }
}

