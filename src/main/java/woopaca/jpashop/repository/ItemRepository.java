package woopaca.jpashop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import woopaca.jpashop.domain.item.Item;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item); // update와 유사.
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        em.createQuery("DELETE FROM Item i WHERE i.id = :id").executeUpdate();
        return em.createQuery("SELECT i FROM Item i", Item.class)
                .getResultList();
    }
}
