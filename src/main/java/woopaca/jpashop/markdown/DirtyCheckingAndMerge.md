<aside>
❗ 매우 중요한 내용

</aside>

트랜잭션 내에 영속성으로 관리되는 엔티티의 데이터가 변경되면, 트랜잭션이 커밋되는 시점에 JPA가 변경을 감지해 DB에 반영한다. (Dirty Checking)

## 준영속 엔티티

영속성 컨텍스트가 더는 관리하지 않는 엔티티

> 예제의 경우, 상품 수정 기능에 있는 `itemService.saveItem(book)`에서 수정을 시도하는 Book 객체가 준영속 엔티티라고 볼 수 있다. `Book` 객체는 이미 DB에 한 번 저장되어 식별자가 존재한다. 이렇게 임의로 만들어낸 엔티티도 기존 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있다.
>

### 준영속 엔티티를 수정하는 방법

- 변경 감지 기능 사용 (이 방법이 더 나은 방법이다.)

  영속성 컨텍스트에서 엔티티를 조회한 후에 데이터 수정

    ``` java
    @Transactional
    public void updateItem(Long itemId, Book param) {
    	Item findItem = itemRepository.findOne(itemId);
    	findItem.setName(param.getName());
    	findItem.setPrice(param.getPrice());
    	findItem.setStockQuantity(param.getStockQuantity());
    	...
    	// itemRepository.save(findItem)을 호출할 필요가 없다.
    }
    ```

- 병합(merge) 사용

  준영속 엔티티를 영속 상태로 변경할 때 사용하는 기능

  <img width="80%" alt="스크린샷 2023-03-06 19 39 02" src="https://user-images.githubusercontent.com/69714701/223117589-70b27b15-b509-4046-bdff-fbf6afdb1acc.png">

  > 파라미터로 전달된 엔티티가 영속 상태의 엔티티가 되는 것은 아니고, `merge()` 메서드가 반환한 엔티티가 영속 상태 엔티티이다.
  >

❗ 변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만, 병합을 사용하면 모든 속성이 변경된다. 병합 시 값이 없으면 `null`로 업데이트 할 위험도 있다. (병합은 모든 필드 교체)

## 가장 좋은 변경 방법

### 엔티티를 변경할 때는 항상 변경 감지를 사용하자.

- 컨트롤러에서 어설프게 엔티티를 생성하지 않는다.
- 트랜잭션이 있는 서비스 계층에 식별자(id)와 변경할 데이터를 명확하게 전달한다. (파라미터 또는 DTO)
- 트랜잭션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회하고 엔티티의 데이터를 직접 변경한다.
- 트랜잭션 커밋 시점에 변경 감지가 실행된다.
- 엔티티에 의미있는 메서드를 만들어 필드를 변경하는 방법이 좋다.
