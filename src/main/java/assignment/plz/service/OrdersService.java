package assignment.plz.service;

import assignment.plz.dto.request.OrdersRequest;
import assignment.plz.dto.response.OrdersDto;
import assignment.plz.model.Item;
import assignment.plz.model.ItemOption;
import assignment.plz.model.Orders;
import assignment.plz.repository.ItemOptionRepository;
import assignment.plz.repository.ItemRepository;
import assignment.plz.repository.OrdersRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdersService {

  private final ItemRepository itemRepository;
  private final ItemOptionRepository itemOptionRepository;
  private final OrdersDtoQuery ordersDtoQuery;
  private final OrdersRepository ordersRepository;

  public HttpStatus requestOrder(OrdersRequest ordersRequest, Integer userId) {
    Item item = itemRepository.findById(ordersRequest.getItemId()).orElse(null);
    OrdersDto orders = ordersDtoQuery.findByCartId(ordersRequest.getCartId(), userId, ordersRequest.getQuantity());

    if (orders.getSoldOut() || ordersRequest.getQuantity() < 1) {

      return HttpStatus.BAD_REQUEST;
    } else if (ordersRequest.getOptionId() != null) {
      ItemOption itemOption = itemOptionRepository.findById(ordersRequest.getOptionId())
          .orElse(null);

      ordersRepository.save(Orders.builder()
          .itemId(ordersRequest.getItemId()).optionId(ordersRequest.getOptionId()).userId(userId)
          .quantity(ordersRequest.getQuantity()).remark(ordersRequest.getRemark())
          .build());

      itemOptionRepository.save(
          ItemOption.builder().id(ordersRequest.getOptionId()).itemId(ordersRequest.getItemId())
              .quantity(itemOption.getQuantity() - ordersRequest.getQuantity())
              .description(itemOption.getDescription()).name(itemOption.getName())
              .price(itemOption.getPrice()).build());

      return HttpStatus.OK;
    } else {
      ordersRepository.save(Orders.builder()
          .itemId(ordersRequest.getItemId()).optionId(ordersRequest.getOptionId())
          .quantity(ordersRequest.getQuantity()).remark(ordersRequest.getRemark()).userId(userId)
          .build());

      item = Item.builder().id(item.getId()).name(item.getName()).price(item.getPrice())
          .quantity(item.getQuantity() - ordersRequest.getQuantity())
          .build();
      itemRepository.save(item);

      return HttpStatus.OK;
    }
  }
}
