package assignment.plz.service;

import assignment.plz.dto.request.AddItemRequest;
import assignment.plz.dto.response.CartDto;
import assignment.plz.model.Cart;
import assignment.plz.model.Item;
import assignment.plz.model.ItemOption;
import assignment.plz.repository.CartRepository;
import assignment.plz.repository.ItemOptionRepository;
import java.util.Objects;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

  private final ItemOptionRepository itemOptionRepository;
  private final CartRepository cartRepository;
  private final ItemQuery itemQuery;
  private final CartDtoQuery cartDtoQuery;

  public HttpStatus addItemToCart(AddItemRequest addItemRequest) {
    Item findItem = itemQuery.findByNameAndCheckStock(addItemRequest.getItemId(),
        addItemRequest.getQuantity());
    if (findItem == null || addItemRequest.getQuantity() < 1) {
      return HttpStatus.BAD_REQUEST;
    }

    if (addItemRequest.getOptionYn()) {
      ItemOption findItemOptions = itemOptionRepository.findById(addItemRequest.getItemOptionId())
          .orElse(null);

      if (!Objects.equals(findItemOptions.getItemId(), addItemRequest.getItemId())) {

        return HttpStatus.BAD_REQUEST;
      }

      cartRepository.save(Cart.builder()
          .quantity(findItemOptions.getQuantity()).itemId(findItem.getId())
          .userId(addItemRequest.getUserId()).optionId(addItemRequest.getItemOptionId())
          .build());
    } else {
      cartRepository.save(Cart.builder()
          .quantity(addItemRequest.getQuantity()).itemId(findItem.getId())
          .userId(addItemRequest.getUserId()).optionId(addItemRequest.getItemOptionId())
          .build());
    }
    return HttpStatus.OK;
  }

  public Page<CartDto> findCartsList(Integer userId, Pageable pageable) {
    return cartDtoQuery.findByUserId(userId, pageable);
  }

  public Object deleteCart(Integer cartId) {
    cartRepository.deleteById(cartId);

    return HttpStatus.OK;
  }
}
