package SP25SE026_GSP48_WDCRBP_API.repository;

import SP25SE026_GSP48_WDCRBP_API.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
