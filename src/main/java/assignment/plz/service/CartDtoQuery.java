package assignment.plz.service;

import assignment.plz.dto.response.CartDto;
import assignment.plz.dto.response.QCartDto;
import assignment.plz.model.Cart;
import assignment.plz.model.QCart;
import assignment.plz.model.QItem;
import assignment.plz.model.QItemOption;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartDtoQuery {

  private final JPAQueryFactory queryFactory;

  public Page<CartDto> findByUserId(Integer userId, Pageable pageable) {
    QItem item = QItem.item;
    QItemOption itemOption = QItemOption.itemOption;
    QCart cart = QCart.cart;

    QueryResults<CartDto> results = queryFactory.select(new QCartDto(
            cart.userId,
            cart.itemId,
            cart.quantity,
            new CaseBuilder()
                .when(item.optionYn.eq(true).and(itemOption.quantity.goe(cart.quantity)))
                .then(false)
                .when(item.optionYn.eq(false).and(item.quantity.goe(cart.quantity)))
                .then(false)
                .otherwise(true)
                .as("soldOut"),
            cart.optionId))
        .from(cart)
        .leftJoin(item).on(item.id.eq(cart.itemId))
        .leftJoin(itemOption).on(itemOption.itemId.eq(item.id))
        .where(cart.userId.eq(userId))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    List<CartDto> content = results.getResults();
    long total = results.getTotal();
    return new PageImpl<>(content, pageable, total);
  }
}
