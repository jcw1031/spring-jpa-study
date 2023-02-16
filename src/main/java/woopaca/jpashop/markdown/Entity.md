- 다대다는 일대다와 다대일로 푼다.
- 설계 단계에서는 양방향 연관관계를 사용하지 않고 단방향 연관관계를 사용하여 설계하자.

## 테이블 설계

<img width="988" alt="스크린샷 2023-02-09 오후 7 39 00" src="https://user-images.githubusercontent.com/69714701/218437668-1146d2b9-3236-4f42-9ba7-dc5efcefa366.png">


- `Item` 테이블은 싱글 테이블 전략을 사용했다. → 한 테이블에 모든 정보를 넣고 구분 값(`dtype`)으로 구부하여 사용한다.
- `Orders` 테이블은 데이터베이스 예약어인 `ORDER` 때문에 관례상 `Orders`로 많이 사용한다.

## 연관관계 매핑

- 연관관계 매핑의 주인을 정하자. → **외래키를 가지는 객체(테이블)가 연관관계의 주인**이 된다. (회원과 주문의 경우 주문이 회원을 외래키로 가지므로 주문이 갖는 `member(_id)`가 연관관계 매핑의 주인이 된다.)
- 연관관계 매핑의 주인이 변경되어야 FK값이 변경된다.

## 엔티티 개발

> **이론적으로 Getter와 Setter를 모두 제공하지 않고 꼭 필요한 별도의 메서드만 제공하는 것이 가장 인상적이다. 하지만 실무에서 엔티티의 데이터는 조회할 일이 너무 많아서 Getter는 모두 열어두는 것이 편리하다. Setter는 값이 변경될 수 있어, 미래에 엔티티가 왜 변경되는지 추적하기 힘들어진다. 따라서 엔티티의 데이터를 변경할 때는 Setter 대신에 변경 지점이 명확하도록 하기 위한 별도의 비즈니스 메서드를 제공해야 한다.**
>
- 예전의 `Date` 형식을 사용하면 날짜 관련 애노테이션으로 매핑을 해야하지만, `LocalDateTime`을 사용하면 Hibernate가 자동으로 지원
- JPA가 자동으로 생성하는 테이블을 바로 사용하는 것은 좋지 않다. 참고정도 하여 디테일한 부분을 수정해 사용

### 값 타입

- 값은 변경되면 안 되기 때문에 Getter만 제공. 생성자를 통해 생성할 때 값을 지정하고 이후에 변하지 않도록
- JPA 구현 라이브러리가 리플렉션같은 기술을 사용할 수 있도록 지원하기 위해 기본 생성자도 추가 (JPA 스펙 상 접근 지정자를 `public` 또는 `protected`로 지정 → `protected`가 그나마 더 안전)

### 상속 관계 매핑

- 상속 관계 전략을 지정해야 한다. → 부모클래스에 지정 : `@Inheritance(strategy = )`
  - `JOINED` : 가장 정규화된 스타일
  - `SINGLE_TABLE` : 한 테이블에 다 넣는다.
  - `TABLE_PER_CLASS` : 자식클래스 테이블만 생성
- `@DiscriminatorColumn`, `@DiscriminatorValue` - 싱글 테이블인 경우, 저장 시 구분을 위해 사용

### Enum형

- `@Enumerated`의 default value는 `ORDINAL` → `Integer`형으로 들어간다.
  - 숫자로 들어가는 경우, 만약 중간에 다른 값이 추가되면 굉장히 치명적이다.

  → 따라서 `EnumType.STRING`으로 설정한다.


### 일대일 관계

- FK를 어디에 설정하든지 상관 없다. 각각 장단점 존재
  - access를 많이 하는 쪽에 두는 게 좋을 것 같다. (`Order`와 `Delivery` 중 `Order`를 통해 `Delivery`를 찾으므로 `Order`에 FK를 둔다.)
- `mappedBy`를 통해 연관관계 매핑의 주인을 설정.

### 다대다 관계 (실무에서는 거의 사용하지 않는다. 다대일, 일대다 관계로 풀어낸다.)

- `@JoinTable` 설정 필요.
  - `joinColumn`과 `inverseJoinColumn` 설정

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

// ------------------------------------------------------------------------------------

public class Item {

		...

		@ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

		...

}
```

### 계층 구조

- 자기 자신을 참조
- `@ManyToOne`과 `@OneToMany` 둘 다 설정

```java
public class Category {	

		...

		@ManyToOne
		@JoinColumn(name = "parent_id")
		private Category parent;
	
		@OneToMany(mappedBy = "parent")
		private List<Category> child = new ArrayList<>();

		...

}
```

## cascade

`@OneToMany(…, cascade = CascadeType.ALL)`

```java
// 원래는
em.persist(orderItemA);
em.persist(orderItemB);
em.persist(orderItemC);

em.persist(order);

// cascade를 설정하면
em.persist(order);

// Order 객체만 저장해도 Order 객체의 List<OrderItem>도 함께 저장(삭제도 마찬가지)
// persist()를 각각 따로 하지 않아도 된다.
```

### 연관관계 편의 메서드

연관관계 매핑의 주인을 떠나서, Member가 주문을 하면 List<Order>에 저장해야 한다. 또한 Order의 Member에도 저장을 해야한다. (연관관계 매핑의 주인에 설정하는 게 중요하지만, 둘 다 하는 게 좋다.)

```java
public class Order {

	...
	
	public void setMember(Member member) {
		this.member = member;
		member.getOrders().add(this);
	}
	
	...

}
```

> 이렇게까지 해야 하는 이유는 기본편에서 많이 설명하셨다고 한다.
>

## 엔티티 설계 시 주의점

### 엔티티에는 가급적 Setter는 사용하지 않는다.

### 모든 연관관계는 지연로딩으로 설정한다❗️

- 즉시 로딩(EAGER)은 예측이 어렵고 어떤 SQL이 실행될지 추적이 어렵다. 특히 JPQL을 실행할 때 N+1 문제가 자주 발생한다.

  > N+1 문제 : JPQL을 SQL로 변환하여 엔티티들을 가져온 후에, 만약 EAGER로 설정되어 있으면 다시 엔티티 개수만큼의 쿼리문이 날라간다. 그래서 처음 쿼리 1개 (+) 조회된 엔티티 개수 N개 만큼의 쿼리
>
- 실무에서 모든 연관관계는 지연(LAZY) 로딩으로 설정해야 한다.
- 연관된 엔티티를 함께 DB에서 조회해야 하면 fetch join 또는 엔티티 그래프 기능을 사용한다.
- (@xToOne) @OneToOne과 @ManyToOne 관계는 기본이 즉시 로딩이기 때문에 직접 지연 로딩으로 설정해야 한다.

### 컬렉션은 필드에서 초기화 한다.

- null 문제에서 안전하다.
- 하이버네이트는 엔티티를 영속화 할 때, 컬랙션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다. 만약 `getOrders()` 처럼 임의의 메서드에서 컬력션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문제가 발생할 수 있다. 따라서 필드 레벨에서 생성하는 것이 가장 안전하고 깔끔하다.

```java
Member member = new Member();
System.out.println(member.getOrders().getClass());
em.persist(team);
System.out.println(member.getOrders().getClass());

// 출력 결과

/*
class java.util.ArrayList
class org.hibernate.collection.internal. PersistentBag
*/
```

### 테이블, 컬럼명 생성 전략

1. 카멜 케이스 → 언더스코어 (memberPoint → member_point)
2. .(Dot) → _(언더스코어)
3. 대문자 → 소문자

> 직접 설정도 가능. naming strategy 검색
>
