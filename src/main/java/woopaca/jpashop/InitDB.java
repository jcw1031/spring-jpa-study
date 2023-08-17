package woopaca.jpashop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import woopaca.jpashop.domain.Address;
import woopaca.jpashop.domain.Delivery;
import woopaca.jpashop.domain.Member;
import woopaca.jpashop.domain.Order;
import woopaca.jpashop.domain.OrderItem;
import woopaca.jpashop.domain.item.Book;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Component
public class InitDB {

    private final InitService initService;

    @PostConstruct
    void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Transactional
    @RequiredArgsConstructor
    @Component
    static class InitService {

        private final EntityManager entityManager;

        public void dbInit1() {
            Member member = createMember("userA", "서울", "1", "11111");
            entityManager.persist(member);

            Book book1 = createBook("JPA1 BOOK", 10_000, 100);
            entityManager.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 20_000, 100);
            entityManager.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10_000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20_000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            entityManager.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "진주", "2", "22222");
            entityManager.persist(member);

            Book book1 = createBook("SPRING1 BOOK", 20_000, 200);
            entityManager.persist(book1);

            Book book2 = createBook("SPRING2 BOOK", 40_000, 300);
            entityManager.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20_000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40_000, 4);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            entityManager.persist(order);
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private Book createBook(String bookName, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(bookName);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }
}
