package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Category;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepo extends JpaRepository<Category, UUID> {

    List<Category> findAllByProjectId(UUID projectId);

    boolean existsByNameAndProjectId(String name, UUID project_id);

    boolean existsByNameAndProjectIdAndIdNot(String name, UUID project_id, UUID id);

}
