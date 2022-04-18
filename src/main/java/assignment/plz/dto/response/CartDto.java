package assignment.plz.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartDto {

  private Integer userId;
  private Integer itemId;
  private int quantity;
  private Boolean soldOut;
  private Integer optionId;

  @QueryProjection
  public CartDto(
      Integer userId,
      Integer itemId,
      int quantity,
      Boolean soldOut,
      Integer optionId
  ) {
    this.userId = userId;
    this.itemId = itemId;
    this.quantity = quantity;
    this.soldOut = soldOut;
    this.optionId = optionId;
  }
}
