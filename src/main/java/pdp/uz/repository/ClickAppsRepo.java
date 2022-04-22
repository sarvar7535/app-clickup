package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.ClickApps;

@Repository
public interface ClickAppsRepo extends JpaRepository<ClickApps, Long> {

    ClickApps getByName(String priority);

}
