package assignment.plz.controller;

import static org.assertj.core.api.Assertions.*;

import assignment.plz.dto.request.AddItemRequest;
import assignment.plz.dto.request.OrdersRequest;
import assignment.plz.dto.response.CartDto;
import assignment.plz.model.Cart;
import assignment.plz.model.Item;
import assignment.plz.model.ItemOption;
import assignment.plz.model.User;
import assignment.plz.repository.CartRepository;
import assignment.plz.repository.ItemOptionRepository;
import assignment.plz.repository.ItemRepository;
import assignment.plz.repository.OrdersRepository;
import assignment.plz.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private ItemOptionRepository itemOptionRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private OrdersRepository ordersRepository;

  @BeforeEach
  public void testEntity() {
    ordersRepository.deleteAll();
    cartRepository.deleteAll();
    itemOptionRepository.deleteAll();
    itemRepository.deleteAll();
    userRepository.deleteAll();

    User user1 = User.builder()
        .username("Kim")
        .password("1234")
        .build();
    userRepository.save(user1);
    User user2 = User.builder()
        .username("Lee")
        .password("1234")
        .build();
    userRepository.save(user2);

    Item noOptionItem = Item.builder()
        .name("watermelon")
        .price(10000)
        .quantity(10)
        .optionYn(false)
        .build();
    itemRepository.save(noOptionItem);

    Item existItem = Item.builder()
        .name("shirts")
        .optionYn(true)
        .build();
    Item soldOutItem = Item.builder()
        .name("pants")
        .optionYn(true)
        .build();
    itemRepository.save(existItem);
    itemRepository.save(soldOutItem);

    existItem = itemRepository.findByName("shirts").orElse(null);
    soldOutItem = itemRepository.findByName("pants").orElse(null);

    ItemOption option1 = ItemOption.builder()
        .itemId(existItem.getId())
        .name("medium")
        .description("Size 1")
        .price(20000)
        .quantity(5)
        .build();
    ItemOption option2 = ItemOption.builder()
        .itemId(soldOutItem.getId())
        .name("large")
        .description("Size 2")
        .price(25000)
        .quantity(0)
        .build();
    itemOptionRepository.save(option1);
    itemOptionRepository.save(option2);

    Cart cart = Cart.builder()
        .itemId(existItem.getId())
        .quantity(4)
        .userId(user1.getId())
        .optionId(option1.getId())
        .build();
    Cart soldOutCart = Cart.builder()
        .itemId(soldOutItem.getId())
        .quantity(3).userId(user1.getId())
        .optionId(option2.getId())
        .build();
    Cart noOptionCart = Cart.builder()
        .itemId(noOptionItem.getId())
        .quantity(3).userId(user1.getId())
        .build();
    Cart anotherUsersCart = Cart.builder()
        .itemId(noOptionItem.getId())
        .quantity(3)
        .userId(user2.getId())
        .build();
    cartRepository.save(cart);
    cartRepository.save(soldOutCart);
    cartRepository.save(noOptionCart);
    cartRepository.save(anotherUsersCart);
  }

  @Test
  @DisplayName("장바구니 추가 성공 옵션있음")
  public void 장바구니_추가_옵션있음() {
    String url = "http://localhost:" + port + "/api/v1/carts";
    User user = userRepository.findByUsername("Kim").orElse(null);
    Item item = itemRepository.findByName("shirts").orElse(null);

    ItemOption option = itemOptionRepository.findByItemId(item.getId()).orElse(null);

    AddItemRequest addItem = AddItemRequest.builder()
        .optionYn(item.isOptionYn())
        .userId(user.getId())
        .itemOptionId(option.getId())
        .quantity(3)
        .itemId(item.getId()).build();

    ResponseEntity<Long> addSuccess = restTemplate.postForEntity(url, addItem, Long.class);
    assertThat(addSuccess.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("장바구니 추가 성공 옵션없음")
  public void 장바구니_추가_옵션없음() {
    String url = "http://localhost:" + port + "/api/v1/carts";
    User user = userRepository.findByUsername("Kim").orElse(null);
    Item item = itemRepository.findByName("watermelon").orElse(null);

    AddItemRequest addItem = AddItemRequest.builder()
        .optionYn(item.isOptionYn())
        .userId(user.getId())
        .quantity(3)
        .itemId(item.getId())
        .build();

    ResponseEntity<Long> addSuccess = restTemplate.postForEntity(url, addItem, Long.class);
    assertThat(addSuccess.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("장바구니 추가 실패 상품 품절")
  public void 장바구니_추가_품절실패() {
    String url = "http://localhost:" + port + "/api/v1/carts";

    User user = userRepository.findByUsername("Kim").orElse(null);
    Item item = itemRepository.findByName("shirts").orElse(null);
    ItemOption option = itemOptionRepository.findByItemId(item.getId()).orElse(null);

    AddItemRequest addEmptyItem = AddItemRequest.builder()
        .optionYn(item.isOptionYn())
        .userId(user.getId())
        .itemOptionId(option.getId())
        .quantity(8)
        .itemId(item.getId()).build();

    ResponseEntity<Long> emptyFail = restTemplate.postForEntity(url, addEmptyItem, Long.class);
    assertThat(emptyFail.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("장바구니 추가 실패 잘못된 옵션 선택")
  public void 잘못된_옵션_선택() {
    String url = "http://localhost:" + port + "/api/v1/carts";
    User user = userRepository.findByUsername("Kim").orElse(null);
    Item item = itemRepository.findByName("shirts").orElse(null);
    Item otherItem = itemRepository.findByName("pants").orElse(null);

    ItemOption otherOption = itemOptionRepository.findByItemId(otherItem.getId()).orElse(null);

    AddItemRequest differentItemOption = AddItemRequest.builder()
        .optionYn(item.isOptionYn())
        .userId(user.getId())
        .itemOptionId(otherOption.getId())
        .quantity(3)
        .itemId(item.getId())
        .build();

    ResponseEntity<Long> differentOptionFail = restTemplate.postForEntity(url,
        differentItemOption,
        Long.class);
    assertThat(differentOptionFail.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("장바구니 추가 실패 초과 수량")
  public void 초과_수량() {
    String url = "http://localhost:" + port + "/api/v1/carts";
    User user = userRepository.findByUsername("Kim").orElse(null);
    Item item = itemRepository.findByName("shirts").orElse(null);

    ItemOption itemOption = itemOptionRepository.findByItemId(item.getId()).orElse(null);

    AddItemRequest moreThanExsists = AddItemRequest.builder()
        .optionYn(item.isOptionYn())
        .userId(user.getId())
        .itemOptionId(itemOption.getId())
        .quantity(999)
        .itemId(item.getId())
        .build();

    ResponseEntity<Long> differentOptionFail = restTemplate.postForEntity(url,
        moreThanExsists,
        Long.class);
    assertThat(differentOptionFail.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("장바구니 조회 성공")
  public void 장바구니_조회() {
    User user = userRepository.findByUsername("Kim").orElse(null);
    List<Cart> cart = cartRepository.findAllByUserId(user.getId());

    String url = "http://localhost:" + port + "/api/v1/carts/" + user.getId() + "?page=0&size=2";
    ResponseEntity<RestResponsePage<CartDto>> findCarts = restTemplate.exchange(url, HttpMethod.GET,
        null,
        new ParameterizedTypeReference<RestResponsePage<CartDto>>() {
        });
    Page<CartDto> carts = findCarts.getBody();
    List<CartDto> cartList = carts.getContent();

    org.junit.jupiter.api.Assertions.assertEquals(user.getId(), cartList.get(0).getUserId());
    org.junit.jupiter.api.Assertions.assertEquals(cart.get(0).getItemId(),
        cartList.get(0).getItemId());
    org.junit.jupiter.api.Assertions.assertEquals(false, cartList.get(0).getSoldOut());
  }

  @Test
  @DisplayName("장바구니 품절상품 조회 성공")
  public void 장바구니_품절상품_조회() {
    User user = userRepository.findByUsername("Kim").orElse(null);
    List<Cart> cart = cartRepository.findAllByUserId(user.getId());

    String url = "http://localhost:" + port + "/api/v1/carts/" + user.getId() + "?page=0&size=2";
    ResponseEntity<RestResponsePage<CartDto>> findCarts = restTemplate.exchange(url, HttpMethod.GET,
        null,
        new ParameterizedTypeReference<RestResponsePage<CartDto>>() {
        });
    Page<CartDto> carts = findCarts.getBody();
    List<CartDto> cartList = carts.getContent();

    org.junit.jupiter.api.Assertions.assertEquals(user.getId(), cartList.get(1).getUserId());
    org.junit.jupiter.api.Assertions.assertEquals(cart.get(1).getItemId(),
        cartList.get(1).getItemId());
    org.junit.jupiter.api.Assertions.assertEquals(true, cartList.get(1).getSoldOut());
  }

  @Test
  @DisplayName("주문 성공 옵션있음")
  public void 장바구니_주문_성공_옵션있음() {
    User user = userRepository.findByUsername("Kim").orElse(null);
    List<Cart> cart = cartRepository.findAllByUserId(user.getId());

    OrdersRequest ordersRequest = OrdersRequest.builder().itemId(cart.get(0).getItemId())
        .optionId(cart.get(0).getOptionId())
        .quantity(cart.get(0).getQuantity())
        .cartId(cart.get(0).getId())
        .remark("빨리 배송 해 주세요")
        .build();

    String url = "http://localhost:" + port + "/api/v1/orders/" + user.getId();
    ResponseEntity<Long> orderSuccess = restTemplate.postForEntity(url, ordersRequest, Long.class);
    assertThat(orderSuccess.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @DisplayName("주문 성공 옵션없음")
  public void 장바구니_주문_성공_옵션없음() {
    User user = userRepository.findByUsername("Kim").orElse(null);
    List<Cart> cart = cartRepository.findAllByUserId(user.getId());
    Item preOrderItem = itemRepository.findByName("watermelon").orElse(null);

    OrdersRequest ordersRequest = OrdersRequest.builder()
        .itemId(cart.get(2).getItemId())
        .optionId(cart.get(2).getOptionId())
        .quantity(cart.get(2).getQuantity())
        .cartId(cart.get(2).getId())
        .remark("빨리 배송 해 주세요22")
        .build();

    String url = "http://localhost:" + port + "/api/v1/orders/" + user.getId();
    ResponseEntity<Long> orderSuccess = restTemplate.postForEntity(url, ordersRequest, Long.class);
    assertThat(orderSuccess.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertEquals(7, preOrderItem.getQuantity() - cart.get(2).getQuantity());
  }

  @Test
  @DisplayName("주문 실패 0개 주문")
  public void 장바구니_주문_실패_갯수_0() {
    User user = userRepository.findByUsername("Kim").orElse(null);
    List<Cart> cart = cartRepository.findAllByUserId(user.getId());

    OrdersRequest ordersRequest = OrdersRequest.builder()
        .itemId(cart.get(2).getItemId())
        .optionId(cart.get(2).getOptionId())
        .quantity(0)
        .cartId(cart.get(2).getId())
        .remark("빨리 배송 해 주세요www")
        .build();

    String url = "http://localhost:" + port + "/api/v1/orders/" + user.getId();
    ResponseEntity<Long> orderSuccess = restTemplate.postForEntity(url, ordersRequest, Long.class);
    assertThat(orderSuccess.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("주문 실패 초과 주문")
  public void 장바구니_주문_초과주문() {
    User user = userRepository.findByUsername("Kim").orElse(null);
    List<Cart> cart = cartRepository.findAllByUserId(user.getId());

    OrdersRequest ordersRequest = OrdersRequest.builder().itemId(cart.get(2).getItemId())
        .optionId(cart.get(2).getOptionId())
        .quantity(999)
        .cartId(cart.get(2).getId())
        .remark("빨리 배송 해 주세요www")
        .build();

    String url = "http://localhost:" + port + "/api/v1/orders/" + user.getId();
    ResponseEntity<Long> orderSuccess = restTemplate.postForEntity(url, ordersRequest, Long.class);
    assertThat(orderSuccess.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("카트 삭제 성공")
  public void 삭제_성공() {
    User user = userRepository.findByUsername("Kim").orElse(null);
    List<Cart> cart = cartRepository.findAllByUserId(user.getId());

    String url = "http://localhost:" + port + "/api/v1/carts/" + cart.get(0).getId();
    ResponseEntity<Void> deleteSuccess = restTemplate.exchange(url, HttpMethod.DELETE, null,
        Void.class);

    Assertions.assertEquals(deleteSuccess.getStatusCode(), HttpStatus.OK);
  }

  @AfterEach
  void clearDB() {
    ordersRepository.deleteAll();
    cartRepository.deleteAll();
    itemOptionRepository.deleteAll();
    itemRepository.deleteAll();
    userRepository.deleteAll();
  }
}
