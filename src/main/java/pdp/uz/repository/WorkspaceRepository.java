package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pdp.uz.entity.Workspace;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    boolean existsByOwnerIdAndName(UUID owner_id, String name);

    boolean existsByOwnerIdAndNameAndIdNot(UUID owner_id, String name, Long id);

    List<Workspace> findAllByOwnerId(UUID owner_id);

    Optional<Workspace> findByIdAndOwnerId(Long id, UUID owner_id);
}
