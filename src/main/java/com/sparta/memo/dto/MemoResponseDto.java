package com.sparta.memo.dto;

import com.sparta.memo.entity.Memo;
import lombok.Getter;
//Memo 클래스와 비슷하게 생겼다. 요청에대한 반환 부분이다. 컨트롤러 생성 후 이것을 작성한다.
@Getter
public class MemoResponseDto {
    private Long id;
    private String username;
    private String contents;

    public MemoResponseDto(Memo memo) {
        this.id = memo.getId();
        this.username = memo.getUsername();
        this.contents = memo.getContents();
    }
}