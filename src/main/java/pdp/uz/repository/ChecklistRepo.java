package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.CheckList;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChecklistRepo extends JpaRepository<CheckList, UUID> {

    List<CheckList> findAllByTaskId(UUID taskId);
}
