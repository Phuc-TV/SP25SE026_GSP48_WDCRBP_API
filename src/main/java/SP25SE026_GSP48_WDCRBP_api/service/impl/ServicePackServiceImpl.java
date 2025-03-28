package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CreateServicePackRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListServicePackRest;
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
    public CreateServicePackRest createServicePack(CreateServicePackRequest request) {

        if (servicePackRepository.existsByName(request.getName())) {
            throw new RuntimeException("Service Pack with this name already exists.");
        }

        ServicePack servicePack = modelMapper.map(request, ServicePack.class);
        ServicePack saved = servicePackRepository.save(servicePack);

        CreateServicePackRest.Data responseData = CreateServicePackRest.Data.builder()
                .servicePackId(saved.getServicePackId())
                .name(saved.getName())
                .price(saved.getPrice())
                .description(saved.getDescription())
                .duration(saved.getDuration())
                .build();

        return CreateServicePackRest.builder()
                .data(responseData)
                .build();
    }

    @Override
    public CreateServicePackRest updateServicePack(Long servicePackId, CreateServicePackRequest request) {
        ServicePack existing = servicePackRepository.findById(servicePackId)
                .orElseThrow(() -> new RuntimeException("Service Pack not found with ID: " + servicePackId));

        // Update values
        existing.setName(request.getName());
        existing.setPrice(request.getPrice());
        existing.setDescription(request.getDescription());
        existing.setDuration(request.getDuration());

        // Save changes
        ServicePack updated = servicePackRepository.save(existing);

        // Prepare response
        CreateServicePackRest.Data data = CreateServicePackRest.Data.builder()
                .servicePackId(updated.getServicePackId())
                .name(updated.getName())
                .price(updated.getPrice())
                .description(updated.getDescription())
                .duration(updated.getDuration())
                .build();

        return CreateServicePackRest.builder()
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
    public ListServicePackRest getAllServicePacks() {
        List<ServicePack> packs = servicePackRepository.findAll();

        List<ListServicePackRest.Data> dataList = packs.stream().map(pack -> {
            ListServicePackRest.Data dto = new ListServicePackRest.Data();
            dto.setServicePackId(pack.getServicePackId());
            dto.setName(pack.getName());
            dto.setPrice(pack.getPrice());
            dto.setDescription(pack.getDescription());
            dto.setDuration(pack.getDuration());
            return dto;
        }).toList();

        ListServicePackRest response = new ListServicePackRest();
        response.setData(dataList);

        return response;
    }

    @Override
    public ListServicePackRest getServicePackById(Long servicePackId) {
        ServicePack pack = servicePackRepository.findById(servicePackId)
                .orElseThrow(() -> new RuntimeException("Service Pack not found with ID: " + servicePackId));

        ListServicePackRest.Data dto = new ListServicePackRest.Data();
        dto.setServicePackId(pack.getServicePackId());
        dto.setName(pack.getName());
        dto.setPrice(pack.getPrice());
        dto.setDescription(pack.getDescription());
        dto.setDuration(pack.getDuration());

        ListServicePackRest response = new ListServicePackRest();
        response.setData(List.of(dto));

        return response;
    }
}
