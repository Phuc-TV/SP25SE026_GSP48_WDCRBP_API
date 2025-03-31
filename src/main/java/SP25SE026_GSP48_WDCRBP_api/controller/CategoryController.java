package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.CategoryDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Category;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CategoryRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CategoryRes;
import SP25SE026_GSP48_WDCRBP_api.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/Category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getAllCategory")
    public CoreApiResponse getAllCategory()
    {
        List<CategoryDto> categories = categoryService.listAllCategory();

        if (categories == null)
            return CoreApiResponse.error("Empty list");
        return CoreApiResponse.success(categories);
    }

    @PostMapping
    public CoreApiResponse<CategoryRes> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        try{
            CategoryRes response = categoryService.createCategory(categoryRequest);
            return CoreApiResponse.success(response, "Tạo danh mục thành công");
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST,"Không tạo được danh mục" + e.getMessage());
        }
    }

    @GetMapping("/{categoryId}")
    public CoreApiResponse<CategoryRes> getCategoryById(@PathVariable Long categoryId) {
        try{
            CategoryRes response = categoryService.getCategoryById(categoryId);
            return CoreApiResponse.success(response, "Lấy danh mục thành công");
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST,"Không tạo được danh mục" + e.getMessage());
        }
    }

    @PutMapping("/{categoryId}")
    public CoreApiResponse<CategoryRes> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest categoryRequest) {
        try{
            CategoryRes response = categoryService.updateCategory(categoryId, categoryRequest);
            return CoreApiResponse.success(response, "Cập nhật danh mục thành công");
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST,"Không tạo được danh mục" + e.getMessage());
        }
    }

    @DeleteMapping("/{categoryId}")
    public CoreApiResponse<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return CoreApiResponse.success("Xóa danh mục thành công");
    }
}
