package com.sparta.memo.entity;
// 이부분, 예전에 했었던 MemoVO 이거랑 개념이 살짝 다르다
// DTO는 계층간 데이터 전송&이동을 위해 사용되는 객체 (Data Transfer Object)
// - 순수 자바 클래스인데 데이터이동을 위해 사용되는 것이구나!
// -- 서버 계층간? 계층은뭘까 레이어드 아키텍쳐
// VO는 값을 갖는 순수한 도메인
// Entity는 이를 DB테이블과 매핑하는객체. (DTO로 변환되서 이동해야한다?)
// 잘이해가 안된다. 좀 구현하고 찾아보도록하자.
// https://youtu.be/J_Dr6R0Ov8E?si=yiyzhHaur2uHfzNF << 테코톡 우아한테크 라흐의 DTO vs VO

// DTO 클래스 명칭은 RequestDTO, ResponseDTO 처럼 한다.
import com.sparta.memo.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Memo {
    private Long id; //메모 구분용 인덱스
    private String username; //작성자
    private String contents; //내용

    public Memo(MemoRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }
}
