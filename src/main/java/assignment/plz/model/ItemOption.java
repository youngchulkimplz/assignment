package assignment.plz.model;

import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class ItemOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "option_id")
  private Integer id;

  @Column(name = "item_id")
  @NotNull
  private Integer itemId;

  @Column(columnDefinition = "varchar(60)")
  @NotNull
  private String name;

  @Column(columnDefinition = "varchar(140)")
  @NotNull
  private String description;

  @Column(columnDefinition = "integer default 0")
  @NotNull
  private int price;

  @Column(columnDefinition = "integer default 0")
  @NotNull
  private int quantity;

}
