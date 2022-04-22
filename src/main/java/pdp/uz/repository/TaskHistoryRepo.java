package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.TaskHistory;

import java.util.UUID;

@Repository
public interface TaskHistoryRepo extends JpaRepository<TaskHistory, UUID> {
}
