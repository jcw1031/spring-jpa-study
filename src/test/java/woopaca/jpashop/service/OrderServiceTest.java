package woopaca.jpashop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import woopaca.jpashop.domain.Address;
import woopaca.jpashop.domain.Member;
import woopaca.jpashop.domain.Order;
import woopaca.jpashop.domain.OrderStatus;
import woopaca.jpashop.domain.item.Book;
import woopaca.jpashop.domain.item.Item;
import woopaca.jpashop.exception.NotEnoughStockException;
import woopaca.jpashop.repository.OrderRepository;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("상품 주문")
    void order() throws Exception {
        //given
        Member member = createMember();
        Item book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order findOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, findOrder.getStatus(), "상품 주문 시 상태는 ORDER");
        assertEquals(1, findOrder.getOrderItems().size(), "주문한 상품 종류 수");
        assertEquals(10000 * orderCount, findOrder.getTotalPrice(), "주문 가격은 가격 * 수량");
        assertEquals(10 - orderCount, book.getStockQuantity(), "주문 수량만큼 재고량 감소");
    }

    @Test
    @DisplayName("상품 주문 실패 - 재고 부족")
    void orderFail() throws Exception {
        //given
        Member member = createMember();
        Item book = createBook("시골 JPA", 10000, 10);

        //when
        int orderCount = 11;

        //then;
        NotEnoughStockException notEnoughStockException = assertThrows(NotEnoughStockException.class, () ->
                orderService.order(member.getId(), book.getId(), orderCount));
    }

    @Test
    @DisplayName("주문 취소")
    void orderCancel() throws Exception {
        //given
        Member member = createMember();
        Item book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order findOrder = orderRepository.findOne(orderId);

        assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).isEqualTo(10);
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("woopaca");
        member.setAddress(new Address("서울", "경기", "17040"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
}