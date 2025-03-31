package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.TransactionUpdateRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListTransactionRes;
import SP25SE026_GSP48_WDCRBP_api.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PutMapping("/update-status")
    public CoreApiResponse<String> updateTransactionStatus(@Valid @RequestBody TransactionUpdateRequest request) {
        try {
            transactionService.updateTransaction(request);
            return CoreApiResponse.success("Transaction updated successfully.");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating transaction: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public CoreApiResponse<List<ListTransactionRes.Data>> getAllTransactions() {
        try{
            List<ListTransactionRes.Data> result = transactionService.getAllTransactions();
            String message = result.isEmpty() ? "Danh sách giao dịch rỗng" : "Lấy danh sách giao dịch thành công";
            return CoreApiResponse.success(result, message);
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi lấy danh sách giao dịch: " + e.getMessage());
        }

    }

    @GetMapping("/status")
    public CoreApiResponse<List<ListTransactionRes.Data>> getByStatus(@RequestParam boolean status) {
        try{
            List<ListTransactionRes.Data> result = transactionService.getTransactionsByStatus(status);
            String message = result.isEmpty() ? "Không có giao dịch nào với trạng thái đã chỉ định" : "Lấy giao dịch theo trạng thái thành công";
            return CoreApiResponse.success(result, message);
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi lấy danh sách giao dịch: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public CoreApiResponse<List<ListTransactionRes.Data>> getByUser(@PathVariable Long userId) {
        try{
            List<ListTransactionRes.Data> result = transactionService.getTransactionsByUserId(userId);
            String message = result.isEmpty() ? "Người dùng chưa có giao dịch nào" : "Lấy giao dịch theo người dùng thành công";
            return CoreApiResponse.success(result, message);
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi lấy danh sách giao dịch: " + e.getMessage());
        }
    }


    @GetMapping("/{transactionId}")
    public CoreApiResponse<List<ListTransactionRes.Data>> getById(@PathVariable Long transactionId) {
        try {
            List<ListTransactionRes.Data> result = transactionService.getTransactionById(transactionId);
            String message = result.isEmpty() ? "Không có giao dịch nào với số thứ tự đã chỉ định" : "Lấy giao dịch theo số thứ tự dùng thành công";
            return CoreApiResponse.success(result, message);
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi lấy danh sách giao dịch: " + e.getMessage());
        }
    }
}

