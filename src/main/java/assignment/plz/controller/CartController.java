package assignment.plz.controller;

import assignment.plz.dto.request.AddItemRequest;
import assignment.plz.dto.response.CartDto;
import assignment.plz.service.CartService;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder.In;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

  private final CartService cartService;

  /*
 장바구니에 상품 추가하기
 재고 없을 시 bad request return
 옵션 여부 체크 후 선택옵션 존재 시 옵션테이블의 재고 확인
  */
  @PostMapping("")
  @ResponseBody
  public ResponseEntity addCart(@RequestBody AddItemRequest addItemRequest) {
    return new ResponseEntity(cartService.addItemToCart(addItemRequest));
  }

  /*
  장바구니 조회하기
  남은 갯수가 주문하려는 갯수보다 적게 남은 경우 품절 여부 체크
  페이징 적용
  */
  @GetMapping("/{id}")
  public ResponseEntity<Page<CartDto>> findCartList(@PathVariable Integer id, Pageable pageable) {
    return ResponseEntity.ok(cartService.findCartsList(id, pageable));
  }

  /*
  장바구니 삭제
   */
  @DeleteMapping("/{cartId}")
  public ResponseEntity deleteCart(@PathVariable Integer cartId) {
    return ResponseEntity.ok(cartService.deleteCart(cartId));
  }
}
