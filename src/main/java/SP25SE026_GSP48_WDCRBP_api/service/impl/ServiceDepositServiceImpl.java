package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceDeposit;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ServiceDepositBriefRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ServiceWithDepositsRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UpdateServiceDepositPercentRes;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceDepositRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceRepository;
import SP25SE026_GSP48_WDCRBP_api.service.ServiceDepositService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceDepositServiceImpl implements ServiceDepositService {

    private final ServiceRepository serviceRepository;
    private final ServiceDepositRepository serviceDepositRepository;

    @Override
    public List<UpdateServiceDepositPercentRes> updatePercentAllServiceDepositByServiceId(Long serviceId, Short newPercent) {
        Optional<SP25SE026_GSP48_WDCRBP_api.model.entity.Service> serviceOpt = serviceRepository.findById(serviceId);
        if (serviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Service not found with ID: " + serviceId);
        }

        List<ServiceDeposit> deposits = serviceDepositRepository.findServiceDepositsByService(serviceOpt.get());
        for (ServiceDeposit deposit : deposits) {
            deposit.setPercent(newPercent);
            deposit.setUpdatedAt(LocalDateTime.now());
        }

        List<ServiceDeposit> updated = serviceDepositRepository.saveAll(deposits);

        return updated.stream().map(deposit -> {
            UpdateServiceDepositPercentRes res = new UpdateServiceDepositPercentRes();
            res.setDepositNumber(deposit.getDepositNumber());
            res.setPercent(deposit.getPercent());
            res.setDescription(deposit.getDescription());
            res.setStatus(deposit.getStatus());
            res.setCreatedAt(deposit.getCreatedAt());
            res.setUpdatedAt(deposit.getUpdatedAt());
            res.setServiceId(deposit.getService().getServiceId());
            return res;
        }).toList();
    }

    @Override
    public List<ServiceWithDepositsRes> getAllServiceWithDeposits() {
        List<SP25SE026_GSP48_WDCRBP_api.model.entity.Service> services = serviceRepository.findAll();

        return services.stream().map(service -> {
            List<ServiceDeposit> deposits = serviceDepositRepository.findServiceDepositsByService(service);

            List<ServiceDepositBriefRes> depositResponses = deposits.stream().map(deposit -> {
                ServiceDepositBriefRes depositRes = new ServiceDepositBriefRes();
                depositRes.setServiceDepositId(deposit.getServiceDepositId());
                depositRes.setPercent(deposit.getPercent());
                return depositRes;
            }).toList();

            ServiceWithDepositsRes.ServiceBasicInfo info = new ServiceWithDepositsRes.ServiceBasicInfo();
            info.setServiceId(service.getServiceId());
            info.setServiceName(service.getServiceName());

            ServiceWithDepositsRes result = new ServiceWithDepositsRes();
            result.setService(info);
            result.setServiceDeposits(depositResponses);

            return result;
        }).toList();
    }
}

