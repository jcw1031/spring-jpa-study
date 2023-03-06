package woopaca.jpashop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import woopaca.jpashop.domain.item.Book;

import javax.persistence.EntityManager;

@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("수정 테스트")
    void updateTest() throws Exception {
        //given
        Book book = em.find(Book.class, 1L);

        //when

        //then

    }
}
