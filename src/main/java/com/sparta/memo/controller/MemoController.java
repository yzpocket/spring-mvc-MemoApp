package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.service.MemoService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    //--------JDBC이후 DB 커넥션 부분---------
    private final JdbcTemplate jdbcTemplate;
    public MemoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------3Layer 분리 이후 CREATE-------------
    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        MemoService memoService = new MemoService(jdbcTemplate);
        return memoService.createMemo(requestDto);
    }

    // -------------3Layer 분리 이후 READ-------------
    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        MemoService memoService = new MemoService(jdbcTemplate);
        return memoService.getMemos();
    }

    // -------------3Layer 분리 이후 UPDATE-------------
    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        MemoService memoService = new MemoService(jdbcTemplate);
        return memoService.updateMemo(id, requestDto);
    }

    // -------------3Layer 분리 이후 DELETE-------------
    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        MemoService memoService = new MemoService(jdbcTemplate);
        return memoService.deleteMemo(id);
    }
}
    //--------DB전에 맵에 저장하던것---------
    //private final Map<Long, Memo> memoList = new HashMap<>(); // DB가 없기 때문에 우선 맵으로 DB대신 테스트용으로 사용.
    // << ------------- [질문] 테스트 코드를 만든다 할 때는 실제로 이런 배열이나 컬렉션 객체로 확인하는지? 아니면 DB 커넥션까지 해서 테스트하는지.



    /*-------------JDBC 이전 맵에 저장하던 코드-------------
    //--------CREATE API---------
    @PostMapping("/memos")//[0] CREATE API는 Post 메소드를 사용한다. 처리할 URL을 입력한다.
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto){ // [1]Json이 올꺼다? @RequestBody로 Dto로 받을 매개변수 바로 생각해야함.
        // [2]그리고 요청에 대한 매개변수로 받을 RequestDto와 작업 후 응답에 사용 될 ResponseDto 클래스를 생성해야 한다.
        // [3] RequestDto 를 -> Entity 실질 객체로 생성 ------- 데이터 받고 처리 하기 위한 준비 --------
        Memo memo = new Memo(requestDto); //매개변수로 받은 requestDto를 인스턴스에 인자로 전달하며 받은 데이터를 기반으로 하는 객체를 만든다.

        // [4] Memo Max Idx 부여 등 Entity 객체의 가공을 진행한다.
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1; //이건 컬렉션 기능으로 해쉬셋의 데이터를 확인하고 몇개인지 보고 최대값을 보는것, 데이터가없으면 1을 넣어라.
        memo.setId(maxId); // idx 넣기(위 Memo객체 requestDto 매개변수를 받는 생성자는 index 초기화가 없다.

        // [5] DB 저장 가공된 객체를 DB에 저장 --DB대신 여기선 컬렉션 해쉬맵을 쓰고있다.--
        memoList.put(memo.getId(), memo);

        // [6] Entity 객체를 -> ResponseDto 로 변환 ------- 반환하기 위한 준비 -------
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo); //실질 객체 처리 된 것을 다시 ResponseDto 로 변환
        return memoResponseDto; //Dto 객체로 반환->클라이언트로 응답보냄

        //[질문] 번거로워 보이는데 이것을 하는 이유는? 자답.
        //결국 클라이언트와 서버가 HTTP 통신을 할 때 Json으로 주고받아야 하기 때문에
        //어떠한 API에서 DATA는 출입 할때 Json->객체, 반환 할 때 객체->Json 변환 과정이 필요한 것으로 정리가 된다.
        //이는 Flask Framework에서도 HTTP 통신하는 fetch 함수에서 data를 jsonify로 변환하던 과정과 동일하다는 것이다.
    }
    */

    // -------------3Layer 분리 이전 코드-------------
    //    // RequestDto -> Entity
    //    Memo memo = new Memo(requestDto);
    //
    //    // DB 저장
    //    KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체
    //
    //    //String sql DML문장이 실행됨
    //    String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)"; // <--- username, contents 각각 동적으로 아래 setString으로 결정하게함
    //    jdbcTemplate.update( con -> {
    //                PreparedStatement preparedStatement = con.prepareStatement(sql,
    //                        Statement.RETURN_GENERATED_KEYS);
    //
    //                preparedStatement.setString(1, memo.getUsername()); // <--- 이것이 username
    //                preparedStatement.setString(2, memo.getContents()); // <--- 이것이 contents
    //                return preparedStatement;
    //            },
    //            keyHolder);
    //
    //    // DB Insert 후 받아온 기본키 확인
    //    Long id = keyHolder.getKey().longValue();
    //    memo.setId(id);
    //
    //    // Entity -> ResponseDto
    //    MemoResponseDto memoResponseDto = new MemoResponseDto(memo);
    //
    //    return memoResponseDto;
    //}

    // -------------JDBC 이전 맵에서 읽던 코드-------------
    //--------READ API---------
    //@GetMapping("/memos")//[0] READ API는 Get 메소드를 사용한다. 처리할 URL을 입력한다.
    //public List<MemoResponseDto> getMemos(){
        //[1] DB로부터 데이터(Entity 실질 객체 상태) 가져오고
        // Entity 객체를 -> ResponseDto 로 변환 ------- 반환하기 위한 준비 -------
        //여기선 DB대신 맵을 쓰고 있다.
        //Map -> MemoResponseDto타입의 List 변환
        //List<MemoResponseDto> responseList = memoList.values().stream()
        //        .map(MemoResponseDto::new).toList();
        //memoList부분은 DB의 모든 메모를 가져옴
        //.values().stream()부분은 반복문처럼 모두 순회하면서
        //.map(MemoResponseDto::new)부분은 하나씩 나온 것들을 MemoResponseDto 생성자를 통해 MemoResponseDto 타입의 객체로 만들고
        //.toList()로 그 객체들을 모은(MemoResponseDto타입의 모든 메모들)을 List에 담음

        //스트림 어려우면 반복문으로 동일한 로직 짜도됨.
    //
    //    return responseList;//ResponseDto 타입 객체들을 담은 리스트를 반환함 -> 요청에 대한 응답
    //}
    //


    // -------------3Layer 분리 이전 코드-------------
    //
    //
    //    // DB 조회
    //    String sql = "SELECT * FROM memo";
    //
    //    return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
    //        @Override
    //        public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException { // <--- mapRow가 마치 반복문처럼 메모 전체를 순회하는데,
    //            // memo 전체를 돌면서 각 행의 데이터를 ResultSet rs에 담아서,
    //            // 아래 변수에 할당하게 되는데 이는 rs.get()을 통해 각 column에 해당하는, java로 따지면 객체의 필드에 해당하는 값들을 할당하면서
    //            // 1개의 열 row, 이는 곧 1개의 Dto 객체를 완성시킴 * 전체 메모들을 순회하면서 Dto 리스트를 완성해서 해당 메서드 반환 타입인 List<MemoResponseDto>가 반환됨
    //            // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
    //            Long id = rs.getLong("id");
    //            String username = rs.getString("username");
    //            String contents = rs.getString("contents");
    //            return new MemoResponseDto(id, username, contents);
    //        }
    //    });
    //}


    /* -------------JDBC 이전 맵에서 수정하던 코드-------------
    //---------UPDATE API----------
    @PutMapping("/memos/{id}") // 받아올 데이터의 인덱스
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto){
        // @PathVariable로는 클라이언트 URL로부터 /{id}가 넘어오니 PathVariable로 받아오고,
        // 업데이트할 내용 등은 Body로부터 Json형태 데이터가 넘어 올 것이니 Create랑 동일하게 @RequestBody로 받아온다.

        // 해당 메모가 DB에 존재하는지 확인
        if(memoList.containsKey(id)) {//key값에 해당 index메모가 있는지
            // 해당 메모 가져오기
            Memo memo = memoList.get(id);
            // memo 수정
            memo.update(requestDto);
            return memo.getId();
        }else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
    */

        // -------------3Layer 분리 이전 코드-------------

        //    // 해당 메모가 DB에 존재하는지 확인 (중복되서 아래 메모 존재 확인 메소드를 따로 만듬)
    //    Memo memo = findById(id);
    //    if(memo != null) {
    //        // memo 내용 수정
    //        String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?";
    //        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);
    //
    //        return id;
    //    } else {
    //        throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
    //    }
    //}
    /* -------------JDBC 이전 맵에서 수정하던 코드-------------
    //--------DELETE API-----------
    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id){
        // 해당 메모가 DB로부터 데이터(Entity 실질 객체 상태) 가 있는지 확인
        if(memoList.containsKey(id)){
            //해당 메모 삭제하기
            memoList.remove(id);
            return id; //[질문] 왜 id를 반환하는것??
        }else{
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
    */

        // -------------3Layer 분리 이전 코드-------------

        //    // 해당 메모가 DB에 존재하는지 확인 (중복되서 아래 메모 존재 확인 메소드를 따로 만듬)
    //    Memo memo = findById(id);
    //    if(memo != null) {
    //        // memo 삭제
    //        String sql = "DELETE FROM memo WHERE id = ?";
    //        jdbcTemplate.update(sql, id);
    //
    //        return id;
    //    } else {
    //        throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
    //    }
    //}
        //Service로 이동.
    //---------------JDBC 커넥션 연결 이후 메모 찾기 추가--------------
    //private Memo findById(Long id) {
    //    // DB 조회
    //    String sql = "SELECT * FROM memo WHERE id = ?";
    //
    //    return jdbcTemplate.query(sql, resultSet -> {
    //        if(resultSet.next()) {
    //            Memo memo = new Memo();
    //            memo.setUsername(resultSet.getString("username"));
    //            memo.setContents(resultSet.getString("contents"));
    //            return memo;
    //        } else {
    //            return null;
    //        }
    //    }, id);
    //}