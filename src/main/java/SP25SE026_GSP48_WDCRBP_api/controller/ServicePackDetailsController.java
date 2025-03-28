package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServicePackDetailsRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CreateServicePackDetailsRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.DeleteServicePackDetailsRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListServicePackDetailsRest;
import SP25SE026_GSP48_WDCRBP_api.service.ServicePackDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/service-pack-details")
@CrossOrigin(origins = "*")
public class ServicePackDetailsController {

    @Autowired
    private ServicePackDetailsService service;

    @PostMapping("/create")
    public ResponseEntity<CreateServicePackDetailsRest> create(@Valid @RequestBody CreateServicePackDetailsRequest request) {
        CreateServicePackDetailsRest response = service.createServicePackDetails(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{servicePackDetailsId}")
    public ResponseEntity<CreateServicePackDetailsRest> updateServicePackDetails(
            @PathVariable Long servicePackDetailsId,
            @Valid @RequestBody CreateServicePackDetailsRequest request) {
        CreateServicePackDetailsRest response = service.updateServicePackDetails(servicePackDetailsId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteServicePackDetailsRest> deleteServicePackDetails(
            @RequestParam("servicePackDetailsId") Long servicePackDetailsId) {
        DeleteServicePackDetailsRest response = service.deleteServicePackDetails(servicePackDetailsId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ListServicePackDetailsRest> getAllServicePackDetails() {
        ListServicePackDetailsRest response = service.getAllServicePackDetails();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{servicePackDetailsId}")
    public ResponseEntity<ListServicePackDetailsRest> getServicePackDetailsById(
            @PathVariable("servicePackDetailsId") Long servicePackDetailsId) {
        ListServicePackDetailsRest response = service.getServicePackDetailsById(servicePackDetailsId);
        return ResponseEntity.ok(response);
    }
}

