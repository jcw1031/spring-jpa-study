- 다대다는 일대다와 다대일로 푼다.
- 설계 단계에서는 양방향 연관관계를 사용하지 않고 단방향 연관관계를 사용하여 설계하자.

## 테이블 설계

![스크린샷 2023-02-09 오후 7.39.00.png](..%2F..%2F..%2F..%2F..%2F..%2F..%2F..%2FLibrary%2FMobile%20Documents%2Fcom%7Eapple%7ECloudDocs%2FDownloads%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202023-02-09%20%EC%98%A4%ED%9B%84%207.39.00.png)

- Item 테이블은 싱글 테이블 전략을 사용했다. → 한 테이블에 모든 정보를 넣고 구분 값(dtype)으로 구부하여 사용한다.
- Orders 테이블은 데이터베이스 예약어인 ORDER 때문에 관례상 Orders로 많이 사용한다.

## 연관관계 매핑

- 연관관계 매핑의 주인을 정하자. → **외래키를 가지는 객체(테이블)가 연관관계의 주인**이 된다. (회원과 주문의 경우 주문이 회원을 외래키로 가지므로 주문이 갖는 member(_id)가 연관관계 매핑의 주인이 된다.)
- 연관관계 매핑의 주인이 변경되어야 FK값이 변경된다.

## 엔티티 개발

> **이론적으로 Getter와 Setter를 모두 제공하지 않고 꼭 필요한 별도의 메서드만 제공하는 것이 가장 인상적이다. 하지만 실무에서 엔티티의 데이터는 조회할 일이 너무 많아서 Getter는 모두 열어두는 것이 편리하다. Setter는 값이 변경될 수 있어, 미래에 엔티티가 왜 변경되는지 추적하기 힘들어진다. 따라서 엔티티의 데이터를 변경할 때는 Setter 대신에 변경 지점이 명확하도록 하기 위한 별도의 비즈니스 메서드를 제공해야 한다.**
>
- 예전의 Date 형식을 사용하면 날짜 관련 애노테이션으로 매핑을 해야하지만, LocalDateTime을 사용하면 Hibernate가 자동으로 지원

### 상속 관계 매핑

- 상속 관계 전략을 지정해야 한다. → 부모클래스에 지정 : `@Inheritance(strategy = )`
    - JOINED : 가장 정규화된 스타일
    - SINGLE_TABLE : 한 테이블에 다 넣는다.
    - TABLE_PER_CLASS : 자식클래스 테이블만 생성
- `@DiscriminatorColumn`, `@DiscriminatorValue` - 싱글 테이블인 경우, 저장 시 구분을 위해 사용

### Enum형

- @Enumerated의 default value는 ORDINAL → Integer형으로 들어간다.
    - 숫자로 들어가는 경우, 만약 중간에 다른 값이 추가되면 굉장히 치명적이다.

  → 따라서 EnumType.STRING으로 설정한다.


### 일대일 관계

- FK를 어디에 설정하든지 상관 없다. 각각 장단점 존재
    - access를 많이 하는 쪽에 두는 게 좋을 것 같다. (Order와 Deliver 중 Order를 통해 Delivery를 찾으므로 Order에 FK를 둔다.)
- mappedBy를 통해 연관관계 매핑의 주인을 설정.

### 다대다 관계 (실무에서는 거의 사용하지 않는다. 다대일, 일대다 관계로 풀어낸다.)

- @JoinTable 설정 필요.
    - joinColumn과 inverseJoinColumn 설정

```java
public class Category {

    ...
		
    @ManyToMany
    @JoinTable(name = "category_item",
          joinColumns = @JoinColumn(name = "category_id"), // 자신의 조인키 설정
          inverseJoinColumns = @JoinColumn(name = "item_id") // Item 쪽의 조인키 설정
    )
    private List<Item> items = new ArrayList<>();
		
    ...

}

// -------------------------------------------------------------------------------

public class Item {

    ...

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    ...

}
```

### 계층 구조

- 자기 자신을 참조
- @ManyToOne과 @OneToMany 둘 다 설정

```java
import javax.persistence.FetchType;

public class Category {	

    ...

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent")
  private List<Category> child = new ArrayList<>();

    ...

}
```