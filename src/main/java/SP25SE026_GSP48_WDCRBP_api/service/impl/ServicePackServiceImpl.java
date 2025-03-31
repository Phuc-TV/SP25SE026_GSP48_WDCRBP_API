package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CreateServicePackRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListServicePackRes;
import SP25SE026_GSP48_WDCRBP_api.repository.ServicePackRepository;
import SP25SE026_GSP48_WDCRBP_api.service.ServicePackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;

@Service
public class ServicePackServiceImpl implements ServicePackService {
    @Autowired
    private ServicePackRepository servicePackRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ServicePackServiceImpl(ServicePackRepository servicePackRepository)
    {
        this.servicePackRepository = servicePackRepository;
    }

    public List<ServicePack> getAllServicePack()
    {
        return servicePackRepository.findAll();
    }

    @Override
    public CreateServicePackRes createServicePack(CreateServicePackRequest request) {
        if (servicePackRepository.existsByName(request.getName())) {
            throw new RuntimeException("Service Pack with this name already exists.");
        }
        ServicePack servicePack = modelMapper.map(request, ServicePack.class);
        ServicePack saved = servicePackRepository.save(servicePack);
        CreateServicePackRes.Data responseData = CreateServicePackRes.Data.builder()
                .servicePackId(saved.getServicePackId())
                .name(saved.getName())
                .price(saved.getPrice())
                .description(saved.getDescription())
                .duration(saved.getDuration())
                .postLimitPerMonth(saved.getPostLimitPerMonth())
                .productManagement(saved.getProductManagement())
                .searchResultPriority(saved.getSearchResultPriority())
                .personalization(saved.getPersonalization())
                .build();
        return CreateServicePackRes.builder()
                .data(responseData)
                .build();
    }

    @Override
    public CreateServicePackRes updateServicePack(Long servicePackId, CreateServicePackRequest request) {
        ServicePack existing = servicePackRepository.findById(servicePackId)
                .orElseThrow(() -> new RuntimeException("Service Pack not found with ID: " + servicePackId));
        existing.setName(request.getName());
        existing.setPrice(request.getPrice());
        existing.setDescription(request.getDescription());
        existing.setDuration(request.getDuration());
        existing.setPostLimitPerMonth(request.getPostLimitPerMonth());
        existing.setProductManagement(request.getProductManagement());
        existing.setSearchResultPriority(request.getSearchResultPriority());
        existing.setPersonalization(request.getPersonalization());
        ServicePack updated = servicePackRepository.save(existing);
        CreateServicePackRes.Data data = CreateServicePackRes.Data.builder()
                .servicePackId(updated.getServicePackId())
                .name(updated.getName())
                .price(updated.getPrice())
                .description(updated.getDescription())
                .duration(updated.getDuration())
                .postLimitPerMonth(updated.getPostLimitPerMonth())
                .productManagement(updated.getProductManagement())
                .searchResultPriority(updated.getSearchResultPriority())
                .personalization(updated.getPersonalization())
                .build();
        return CreateServicePackRes.builder()
                .data(data)
                .build();
    }

    @Override
    public void deleteServicePack(Long servicePackId) {
        ServicePack servicePack = servicePackRepository.findById(servicePackId)
                .orElseThrow(() -> new RuntimeException("Service Pack not found with ID: " + servicePackId));
        servicePackRepository.delete(servicePack);
    }

    @Override
    public List<ListServicePackRes.Data> getAllServicePacks() {
        List<ServicePack> packs = servicePackRepository.findAll();
        return packs.stream().map(pack -> {
            ListServicePackRes.Data dto = new ListServicePackRes.Data();
            dto.setServicePackId(pack.getServicePackId());
            dto.setName(pack.getName());
            dto.setPrice(pack.getPrice());
            dto.setDescription(pack.getDescription());
            dto.setDuration(pack.getDuration());
            dto.setPostLimitPerMonth(pack.getPostLimitPerMonth());
            dto.setProductManagement(pack.getProductManagement());
            dto.setSearchResultPriority(pack.getSearchResultPriority());
            dto.setPersonalization(pack.getPersonalization());
            return dto;
        }).toList();
    }

    @Override
    public ListServicePackRes getServicePackById(Long servicePackId) {
        ServicePack pack = servicePackRepository.findById(servicePackId)
                .orElseThrow(() -> new RuntimeException("Service Pack not found with ID: " + servicePackId));
        ListServicePackRes.Data dto = new ListServicePackRes.Data();
        dto.setServicePackId(pack.getServicePackId());
        dto.setName(pack.getName());
        dto.setPrice(pack.getPrice());
        dto.setDescription(pack.getDescription());
        dto.setDuration(pack.getDuration());
        dto.setPostLimitPerMonth(pack.getPostLimitPerMonth());
        dto.setProductManagement(pack.getProductManagement());
        dto.setSearchResultPriority(pack.getSearchResultPriority());
        dto.setPersonalization(pack.getPersonalization());
        ListServicePackRes response = new ListServicePackRes();
        response.setData(List.of(dto));
        return response;
    }
}
