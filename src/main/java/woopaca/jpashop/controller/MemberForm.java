package woopaca.jpashop.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberForm {

    @NotEmpty(message = "회원의 이름은 비어있을 수 없습니다.")
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
