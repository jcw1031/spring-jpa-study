package woopaca.jpashop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import woopaca.jpashop.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository // Spring Bean으로 등록
@RequiredArgsConstructor
public class MemberRepository {

    /*// EntityManagerFactory를 직접 주입받을 수도 있다.
    @PersistenceUnit
    private EntityManagerFactory emf;*/

    //    @PersistenceContext // JPA가 제공하는 표준 Annotation. Spring이 EntityManager를 만들어서 주입
    private EntityManager em;

    public void save(Member member) {
        // persist() -> 영속성 컨텍스트에 Member Entity를 넣는다. (나중에 transaction이 commit되는 시점에 DB에 insert 쿼리가 날라간다.)
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        // JPQL은 Entity 객체를 대상으로 쿼리
        return em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("SELECT m FROM Member m WHERE m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
