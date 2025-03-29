package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Contract;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    Contract findContractByServiceOrder(ServiceOrder serviceOrder);
}
