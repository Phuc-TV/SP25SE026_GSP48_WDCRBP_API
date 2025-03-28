package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePackDetails;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServicePackDetailsRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CreateServicePackDetailsRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.DeleteServicePackDetailsRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListServicePackDetailsRest;
import SP25SE026_GSP48_WDCRBP_api.repository.ServicePackDetailsRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServicePackRepository;
import SP25SE026_GSP48_WDCRBP_api.service.ServicePackDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicePackDetailsServiceImpl implements ServicePackDetailsService {

    @Autowired
    private ServicePackDetailsRepository servicePackDetailsRepository;

    @Autowired
    private ServicePackRepository servicePackRepository;

    @Override
    public CreateServicePackDetailsRest createServicePackDetails(CreateServicePackDetailsRequest request) {
        ServicePack servicePack = servicePackRepository.findServicePackByServicePackId(request.getServicePackId());

        if (servicePack == null) {
            throw new RuntimeException("ServicePack with ID " + request.getServicePackId() + " does not exist");
        }

        ServicePackDetails servicePackDetails = ServicePackDetails.builder()
                .postLimitPerMonth(request.getPostLimitPerMonth())
                .productManagement(request.getProductManagement())
                .searchResultPriority(request.getSearchResultPriority())
                .personalization(request.getPersonalization())
                .servicePack(servicePack)
                .build();

        ServicePackDetails saved = servicePackDetailsRepository.save(servicePackDetails);

        CreateServicePackDetailsRest.Data data = CreateServicePackDetailsRest.Data.builder()
                .servicePackDetailsId(saved.getServicePackDetailsId())
                .postLimitPerMonth(saved.getPostLimitPerMonth())
                .productManagement(saved.getProductManagement())
                .searchResultPriority(saved.getSearchResultPriority())
                .personalization(saved.getPersonalization())
                .servicePackId(saved.getServicePack().getServicePackId())
                .build();

        return CreateServicePackDetailsRest.builder()
                .status("Success")
                .message("ServicePackDetails created successfully")
                .data(data)
                .build();
    }

    @Override
    public CreateServicePackDetailsRest updateServicePackDetails(Long servicePackDetailsId, CreateServicePackDetailsRequest request) {
        // Check if ServicePackDetails exists
        Optional<ServicePackDetails> existingDetailsOpt = servicePackDetailsRepository.findById(servicePackDetailsId);
        if (existingDetailsOpt.isEmpty()) {
            throw new RuntimeException("ServicePackDetails with ID " + servicePackDetailsId + " does not exist");
        }

        // Check if ServicePack exists
        Optional<ServicePack> servicePackOpt = servicePackRepository.findById(request.getServicePackId());
        if (servicePackOpt.isEmpty()) {
            throw new RuntimeException("ServicePack with ID " + request.getServicePackId() + " does not exist");
        }

        ServicePackDetails existingDetails = existingDetailsOpt.get();
        ServicePack servicePack = servicePackOpt.get();

        // Update fields
        existingDetails.setPostLimitPerMonth(request.getPostLimitPerMonth());
        existingDetails.setProductManagement(request.getProductManagement());
        existingDetails.setSearchResultPriority(request.getSearchResultPriority());
        existingDetails.setPersonalization(request.getPersonalization());
        existingDetails.setServicePack(servicePack);

        // Save updated entity
        ServicePackDetails updatedDetails = servicePackDetailsRepository.save(existingDetails);

        // Prepare response
        CreateServicePackDetailsRest.Data data = CreateServicePackDetailsRest.Data.builder()
                .servicePackDetailsId(updatedDetails.getServicePackDetailsId())
                .postLimitPerMonth(updatedDetails.getPostLimitPerMonth())
                .productManagement(updatedDetails.getProductManagement())
                .searchResultPriority(updatedDetails.getSearchResultPriority())
                .personalization(updatedDetails.getPersonalization())
                .servicePackId(updatedDetails.getServicePack().getServicePackId())
                .build();

        return CreateServicePackDetailsRest.builder()
                .status("Success")
                .message("ServicePackDetails updated successfully")
                .data(data)
                .build();
    }

    @Override
    public DeleteServicePackDetailsRest deleteServicePackDetails(Long servicePackDetailsId) {
        // Check if ServicePackDetails exists
        Optional<ServicePackDetails> existingDetailsOpt = servicePackDetailsRepository.findById(servicePackDetailsId);
        if (existingDetailsOpt.isEmpty()) {
            throw new RuntimeException("ServicePackDetails with ID " + servicePackDetailsId + " does not exist");
        }

        // Delete the entity
        servicePackDetailsRepository.deleteById(servicePackDetailsId);

        // Prepare response
        DeleteServicePackDetailsRest response = new DeleteServicePackDetailsRest();
        response.setStatus("Success");
        response.setMessage("ServicePackDetails deleted successfully");
        return response;
    }

    @Override
    public ListServicePackDetailsRest getAllServicePackDetails() {
        List<ServicePackDetails> detailsList = servicePackDetailsRepository.findAll();
        List<ListServicePackDetailsRest.Data> dataList = detailsList.stream().map(this::mapToData).collect(Collectors.toList());

        ListServicePackDetailsRest response = new ListServicePackDetailsRest();
        response.setStatus("Success");
        response.setMessage("Retrieved all service pack details successfully");
        response.setData(dataList);
        return response;
    }

    @Override
    public ListServicePackDetailsRest getServicePackDetailsById(Long servicePackDetailsId) {
        Optional<ServicePackDetails> detailsOpt = servicePackDetailsRepository.findById(servicePackDetailsId);
        if (detailsOpt.isEmpty()) {
            throw new RuntimeException("ServicePackDetails with ID " + servicePackDetailsId + " does not exist");
        }

        ListServicePackDetailsRest.Data data = mapToData(detailsOpt.get());
        ListServicePackDetailsRest response = new ListServicePackDetailsRest();
        response.setStatus("Success");
        response.setMessage("Retrieved service pack details successfully");
        response.setData(List.of(data));
        return response;
    }

    private ListServicePackDetailsRest.Data mapToData(ServicePackDetails details) {
        ListServicePackDetailsRest.Data data = new ListServicePackDetailsRest.Data();
        data.setServicePackDetailsId(details.getServicePackDetailsId());
        data.setPostLimitPerMonth(details.getPostLimitPerMonth());
        data.setProductManagement(details.getProductManagement());
        data.setSearchResultPriority(details.getSearchResultPriority());
        data.setPersonalization(details.getPersonalization());
        data.setServicePackId(details.getServicePack() != null ? details.getServicePack().getServicePackId() : null);
        return data;
    }
}

