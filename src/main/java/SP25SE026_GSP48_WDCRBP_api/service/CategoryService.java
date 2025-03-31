package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.CategoryDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Category;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CategoryRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CategoryRes;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> listAllCategory();
    CategoryRes createCategory(CategoryRequest categoryRequest);
    CategoryRes getCategoryById(Long categoryId);
    CategoryRes updateCategory(Long categoryId, CategoryRequest categoryRequest);
    void deleteCategory(Long categoryId);
}
