package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.TaskTag;

import java.util.UUID;

@Repository
public interface TaskTagRepo extends JpaRepository<TaskTag, UUID> {

    boolean existsByTagIdAndTaskId(UUID tag_id, UUID task_id);

    void deleteByTagIdAndTaskId(UUID tag_id, UUID task_id);
}
