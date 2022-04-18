package assignment.plz.dto.request;

import com.sun.istack.NotNull;
import javax.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class OrdersRequest {
  @NotNull
  private Integer itemId;

  private Integer optionId;

  @NotNull
  private Integer cartId;

  @NotNull
  private int quantity;

  @NotNull
  private String remark;
}
