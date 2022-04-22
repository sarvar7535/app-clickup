package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.SpaceUser;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpaceUserRepo extends JpaRepository<SpaceUser, UUID> {

    List<SpaceUser> findAllBySpaceId(UUID spaceId);

    boolean existsByMemberIdAndSpaceId(UUID uuid, UUID id);
}
