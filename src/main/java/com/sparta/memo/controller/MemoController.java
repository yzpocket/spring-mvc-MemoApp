package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// 현재 클라이언트는 HTML로 구성, 버튼의 js onclick함수를 통해 Ajax로 데이터 담아 요청하도록 구현되어 있다.
// 예로, 아래처럼 구현되어 있으며 각 기능 함수마다 메소드를 구분해두었다.
// 5. POST /api/memos 에 data를 전달합니다.
//      $.ajax({
//        type: "POST",
//        url: "/api/memos",
//        contentType: "application/json",
//        data: JSON.stringify(data),
// CREATE는 POST
// READ는 GET
// UPDATE는 PUT
// DELETE는 DELETE
// 다음과 같은 메소드로 구현해두었기 때문에, 나(백엔드) 또한 저 명세서에 맞추어 url, method, data 받을 방법을 선택해야 한다.
// Ajax를 통한 실시간 갱신은 구현되어있지 않고, window.location.reload(); 페이지 갱신 효과를 주고 있다. 따라서 나는 json으로 다시 반환하여 뿌려주기만 하면 된다.
// 프론트 팀원의 이해도 정도를 모르겠지만, 이전 과정에서 실시간 갱신 기능을 다뤄봤기 때문에, 협업 시 이 부분이 필요하다고 느껴지는 부분에 대해서 제안 할 수 있을 것 같다.

@RestController // "이 클래스는 컨트롤러 역할 입니다." 표기
@RequestMapping("/api") // 중복URL 제거
public class MemoController {
    //--------DB---------
    private final Map<Long, Memo> memoList = new HashMap<>(); // DB가 없기 때문에 우선 맵으로 DB대신 테스트용으로 사용.
    // << ------------- [질문] 테스트 코드를 만든다 할 때는 실제로 이런 배열이나 컬렉션 객체로 확인하는지? 아니면 DB 커넥션까지 해서 테스트하는지.
}
