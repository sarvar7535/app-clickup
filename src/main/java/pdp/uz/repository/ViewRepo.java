package pdp.uz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pdp.uz.entity.View;

@Repository
public interface ViewRepo extends JpaRepository<View, Long> {

    View getByName(String list);
}
