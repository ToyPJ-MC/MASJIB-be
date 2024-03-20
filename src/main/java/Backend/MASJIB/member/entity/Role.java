package Backend.MASJIB.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum Role {
    NONE,ROLE_USER,ROLE_ADMIN;

    @JsonCreator(mode=JsonCreator.Mode.DELEGATING)
    public static Role fromString(String value) { // String을 Role로 변환
        return Stream.of(Role.values()) // Role의 모든 값을 가져옴
                .filter(r -> r.name().equals(value)) // Role의 name과 일치하는 값만 필터링
                .findFirst() // 첫번째 값 반환
                .orElseThrow(IllegalArgumentException::new); // 일치하는 값이 없으면 예외처리
    }
}
