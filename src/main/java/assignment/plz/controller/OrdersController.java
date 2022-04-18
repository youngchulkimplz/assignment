package assignment.plz.controller;

import assignment.plz.dto.request.OrdersRequest;
import assignment.plz.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrdersController {
  private final OrdersService ordersService;

  /*
  주문 하기
  주문 시 품절 여부 체크
  주문 성공 시 기존 아이템 갯수 - 주문 갯수
   */
  @PostMapping("/{id}")
  public ResponseEntity requestOrder(@RequestBody OrdersRequest ordersRequest, @PathVariable Integer id){
    return new ResponseEntity(ordersService.requestOrder(ordersRequest, id));
  }
}
