package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Project;

import java.util.UUID;

@Repository
public interface ProjectRepo extends JpaRepository<Project, UUID> {

    boolean existsByNameAndSpaceId(String name, UUID spaceId);
}
