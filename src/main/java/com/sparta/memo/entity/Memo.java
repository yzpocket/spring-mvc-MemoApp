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
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "memo") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    public Memo(MemoRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }

    public void update(MemoRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }
}