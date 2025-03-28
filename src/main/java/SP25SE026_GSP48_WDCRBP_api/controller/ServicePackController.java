package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CreateServicePackRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.DeleteServicePackRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListServicePackRest;
import SP25SE026_GSP48_WDCRBP_api.service.ServicePackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/service-pack")
@CrossOrigin(origins = "*")
public class ServicePackController {

    @Autowired
    private ServicePackService servicePackService;

    @PostMapping("/create")
    public ResponseEntity<CreateServicePackRest> createServicePack(
            @Valid @RequestBody CreateServicePackRequest request) {
        return ResponseEntity.ok(servicePackService.createServicePack(request));
    }

    @PutMapping("/update")
    public ResponseEntity<CreateServicePackRest> updateServicePack(
            @RequestParam Long servicePackId,
            @Valid @RequestBody CreateServicePackRequest request
    ) {
        return ResponseEntity.ok(servicePackService.updateServicePack(servicePackId, request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteServicePackRest> deleteServicePack(@RequestParam Long servicePackId) {
        DeleteServicePackRest response = servicePackService.deleteServicePack(servicePackId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<ListServicePackRest> getAllServicePacks() {
        return ResponseEntity.ok(servicePackService.getAllServicePacks());
    }

    @GetMapping("/detail")
    public ResponseEntity<ListServicePackRest> getServicePackById(@RequestParam Long servicePackId) {
        return ResponseEntity.ok(servicePackService.getServicePackById(servicePackId));
    }

}
