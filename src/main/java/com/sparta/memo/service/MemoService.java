package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class MemoService {

    //private final JdbcTemplate jdbcTemplate;//<- 이부분 중복된다.
    private final MemoRepository memoRepository; //MemoRepository 타입의 객체를 생성 하는데~ 아래로.
    //*** (컨트롤러가 직접 메모 서비스를 만들고 있다 - 손님이 음식을 직접만들고 있다 = 강한결합상태 좋지않다..)
    //*** 서비스가 메모 레포지토리를 직접 만들고 있다(제어권) - 손님이 음식을 직접 만들고 있다 = 강한결합상태 좋지않다.

    //public MemoService(JdbcTemplate jdbcTemplate) {//강한 결합이라 문제가 있다.
    public MemoService(MemoRepository memoRepository) {//강한 결합이라 문제가 있다.
        //this.jdbcTemplate = jdbcTemplate;//<- 이부분 중복된다.
        //this.memoRepository = new MemoRepository(jdbcTemplate); //여기로, 이 객체가 생성 될 때, //강한 결합이라 문제가 있다.
        this.memoRepository = memoRepository; //여기로, 이 객체가 생성 될 때, //강한 결합이라 문제가 있다.
        // jdbc를 가진(인자->생성자의 매개변수로전달) 상태의 객체가 생성 된다. 이러면 아래 중복부분들이 모두 필요없어지게 된다.
        // 왜냐하면 memoRepository.save(memo); 이렇게 jdbcTemplate를 가진 객체를 통해 save라는 메소드를 사용하고 있기 때문에
        // jdbc를 가지고 있어서 코드 중복을 막게 되는 것이다.
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장
        //MemoRepository memoRepository = new MemoRepository(jdbcTemplate);//<- 이부분 중복된다. -> jdbc를 포함한 객체가 위에서 생성됬으니 메소드마다 생성 할 필요가 없다.
        Memo saveMemo = memoRepository.save(memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(saveMemo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        // DB 조회
        //MemoRepository memoRepository = new MemoRepository(jdbcTemplate);//<- 이부분 중복된다.-> jdbc를 포함한 객체가 위에서 생성됬으니 메소드마다 생성 할 필요가 없다.
        return memoRepository.findAll();
    }

    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        //MemoRepository memoRepository = new MemoRepository(jdbcTemplate);//<- 이부분 중복된다.-> jdbc를 포함한 객체가 위에서 생성됬으니 메소드마다 생성 할 필요가 없다.
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = memoRepository.findById(id);
        if (memo != null) {
            // memo 내용 수정
            memoRepository.update(id, requestDto);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    public Long deleteMemo(Long id) {
        //MemoRepository memoRepository = new MemoRepository(jdbcTemplate);//<- 이부분 중복된다.-> jdbc를 포함한 객체가 위에서 생성됬으니 메소드마다 생성 할 필요가 없다.
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = memoRepository.findById(id);
        if (memo != null) {
            // memo 삭제
            memoRepository.delete(id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
}