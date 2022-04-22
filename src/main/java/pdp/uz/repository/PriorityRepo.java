package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Priority;

@Repository
public interface PriorityRepo extends JpaRepository<Priority, Long> {
}
