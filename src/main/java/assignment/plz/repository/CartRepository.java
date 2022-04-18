package assignment.plz.repository;

import assignment.plz.model.Cart;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
  Optional<Cart> findByUserId(Integer id);
  List<Cart> findAllByUserId(Integer id);
}
