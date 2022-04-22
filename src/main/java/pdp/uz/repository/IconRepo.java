package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.Icon;

@Repository
public interface IconRepo extends JpaRepository<Icon, Long> {
}
