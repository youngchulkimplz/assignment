package assignment.plz.model;

import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class Orders {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Integer id;

  @Column(name = "item_id")
  @NotNull
  private Integer itemId;

  @Column(name = "option_id")
  private Integer optionId;

  @Column(name = "user_id")
  private Integer userId;

  @Column(columnDefinition = "integer default 0")
  @NotNull
  private int quantity;

  @Column(columnDefinition = "varchar(140)")
  @NotNull
  private String remark;

  @UpdateTimestamp
  @NotNull
  @Column(name = "order_date")
  private LocalDateTime orderDate;
}

