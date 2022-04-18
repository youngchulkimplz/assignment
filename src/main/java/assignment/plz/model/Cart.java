package assignment.plz.model;

import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_id")
  private Integer id;

  @Column(name = "user_id")
  @NotNull
  private Integer userId;

  @Column(name = "item_id")
  @NotNull
  private Integer itemId;

  @Column(columnDefinition = "integer default 0")
  @NotNull
  private int quantity;

  @Column(columnDefinition = "integer")
  private Integer optionId;
}
