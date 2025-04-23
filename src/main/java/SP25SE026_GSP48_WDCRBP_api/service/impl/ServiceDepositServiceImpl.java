package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceDeposit;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateServiceDepositPercentRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ServiceDepositBriefRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ServiceWithDepositsRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UpdateServiceDepositPercentRes;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceDepositRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceRepository;
import SP25SE026_GSP48_WDCRBP_api.service.ServiceDepositService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ServiceDepositServiceImpl implements ServiceDepositService {

    private final ServiceRepository serviceRepository;
    private final ServiceDepositRepository serviceDepositRepository;

    @Override
    public List<UpdateServiceDepositPercentRes> updateDepositPercents(UpdateServiceDepositPercentRequest request) {
        var service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found with ID: " + request.getServiceId()));

        List<UpdateServiceDepositPercentRes> result = new ArrayList<>();

        for (UpdateServiceDepositPercentRequest.DepositUpdateEntry entry : request.getDeposits()) {
            ServiceDeposit deposit = serviceDepositRepository.findById(String.valueOf(entry.getServiceDepositId()))
                    .orElseThrow(() -> new EntityNotFoundException("ServiceDeposit not found with ID: " + entry.getServiceDepositId()));

            deposit.setPercent(entry.getNewPercent());
            deposit.setUpdatedAt(LocalDateTime.now());

            serviceDepositRepository.save(deposit);

            UpdateServiceDepositPercentRes res = new UpdateServiceDepositPercentRes();
            res.setDepositNumber(deposit.getDepositNumber());
            res.setPercent(deposit.getPercent());
            res.setDescription(deposit.getDescription());
            res.setStatus(deposit.getStatus());
            res.setCreatedAt(deposit.getCreatedAt());
            res.setUpdatedAt(deposit.getUpdatedAt());
            res.setServiceId(service.getServiceId());

            result.add(res);
        }

        return result;
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
                depositRes.setDepositNumber(deposit.getDepositNumber());
                depositRes.setDescription(deposit.getDescription());
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

