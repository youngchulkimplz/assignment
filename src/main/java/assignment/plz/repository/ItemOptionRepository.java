package assignment.plz.repository;

import assignment.plz.model.ItemOption;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemOptionRepository extends JpaRepository<ItemOption, Integer> {

  Optional<ItemOption> findByItemId(Integer itemId);
}
