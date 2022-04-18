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
@Builder
@AllArgsConstructor
@Getter
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  private Integer id;

  @Column(columnDefinition = "varchar(45)")
  @NotNull
  private String name;

  @Column(columnDefinition = "integer default 0")
  @NotNull
  private int price;

  @Column(columnDefinition = "integer default 0")
  @NotNull
  private int quantity;

  @Column(columnDefinition = "boolean default false")
  @NotNull
  private boolean optionYn;
}
