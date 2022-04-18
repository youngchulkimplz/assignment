package assignment.plz.repository;

import assignment.plz.model.Item;
import assignment.plz.model.QItem;
import assignment.plz.model.QItemOption;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ItemQueryRepository {
  private final JPAQueryFactory queryFactory;

  public Item findByNameAndCheckStock(Integer itemId, int quantity){
    QItem qItem = QItem.item;
    QItemOption qItemOption = QItemOption.itemOption;

    return queryFactory.selectFrom(qItem)
        .leftJoin(qItemOption)
        .on(qItemOption.itemId.eq(qItem.id))
        .where(qItem.quantity.goe(quantity)
            .or(qItemOption.quantity.goe(quantity)).and(qItem.id.eq(itemId)))
        .fetchOne();
  }
}
