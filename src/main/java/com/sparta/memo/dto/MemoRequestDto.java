package com.sparta.memo.dto;

import lombok.Getter;
//Memo 클래스와 비슷하게 생겼다. 요청에 대하여 매개변수로 받아내는 부분이다. 컨트롤러 생성 후 이것을 작성한다.
@Getter
public class MemoRequestDto {
    private String username;
    private String contents;
}