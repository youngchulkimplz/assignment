package assignment.plz.service;

import assignment.plz.dto.response.CartDto;
import assignment.plz.dto.response.OrdersDto;
import assignment.plz.dto.response.QCartDto;
import assignment.plz.dto.response.QOrdersDto;
import assignment.plz.model.QCart;
import assignment.plz.model.QItem;
import assignment.plz.model.QItemOption;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrdersDtoQuery {

  private final JPAQueryFactory queryFactory;

  public OrdersDto findByCartId(Integer cartId, Integer userId, int orderQuantity) {
    QItem item = QItem.item;
    QItemOption itemOption = QItemOption.itemOption;
    QCart cart = QCart.cart;

    return queryFactory.select(new QOrdersDto(
            new CaseBuilder()
                .when(item.optionYn.eq(true).and(itemOption.quantity.goe(orderQuantity)))
                .then(false)
                .when(item.optionYn.eq(false).and(item.quantity.goe(orderQuantity)))
                .then(false)
                .otherwise(true)
                .as("soldOut"),
            cart.quantity,
            cart.itemId)
        )
        .from(cart)
        .join(item).on(item.id.eq(cart.itemId))
        .leftJoin(itemOption).on(itemOption.itemId.eq(item.id))
        .where(cart.id.eq(cartId), cart.userId.eq(userId))
        .fetchOne();
  }
}
