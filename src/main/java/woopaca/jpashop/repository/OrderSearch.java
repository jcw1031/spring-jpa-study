package woopaca.jpashop.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import woopaca.jpashop.domain.OrderStatus;

@Getter
@Setter
@ToString
public class OrderSearch {

    private String memberName; // 회원 이름
    private OrderStatus orderStatus; // 주문 상태
}
