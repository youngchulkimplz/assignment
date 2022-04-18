package assignment.plz.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdersDto {

  private Boolean soldOut;
  private int quantity;
  private Integer itemId;
  @QueryProjection
  public OrdersDto(
      Boolean soldOut,
      int quantity,
      Integer itemId
  ){
    this.soldOut = soldOut;
    this.quantity = quantity;
    this.itemId = itemId;
  }
}
