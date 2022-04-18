package assignment.plz.dto.request;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddItemRequest {
  @NotNull
  private Integer itemId;

  @NotNull
  private int quantity;

  @NotNull
  private Boolean optionYn;

  @NotNull
  private Integer userId;

  private Integer itemOptionId;
}
