package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoService {
    private final MemoRepository memoRepository; // <- Memo 타입으로 SimpleRepository 구현체 객체가 들어옴

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장
        Memo saveMemo = memoRepository.save(memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(saveMemo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        // DB 조회
        return memoRepository.findAll().stream().map(MemoResponseDto::new).toList(); //전체 목록 가져오는것
        // <Stream 부분 해석, Memo가 하나씩 추출되고 map()의해 변환이 되는데,
        // MemoResponseDto의 생성자 중에서 Memo를 파라미터로 가지고 있는 생성자가 호출되고 하나씩 변환되고,
        // toList()에 의해 리스트로 변환된다.
    }

    public List<MemoResponseDto> getMemosByKeyword(String keyword) {
        // DB 조회 by 키워드
        return memoRepository.findAllByContentsContainsOrderByModifiedAtDesc(keyword).stream().map(MemoResponseDto::new).toList();
    }

    @Transactional //변경감지->업데이트하려면 꼭 Transaction 환경으로 객체가 영속성을 가지도록 (MANAGED) 상태가 되도록 꼭 붙여줘야함 -> 없엔 상태로 테스트했더니 업데이트가 되지 않음.
    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        // memo 내용 수정
        memo.update(requestDto); //변경 감지가 적용됨

        return id;
    }

    public Long deleteMemo(Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        // memo 삭제
        memoRepository.delete(memo); //지울 객체 넣어준다.

        return id;
    }
    //update, delete에서 중복되기 때문에 별도 메소드로 구성

    private Memo findMemo(Long id){
        return  memoRepository.findById(id).orElseThrow(()->
            new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")
        );
    }
}



/*

SPRING BOOT 이전의 공부 주석

//@RequiredArgsConstructor 또는 @Injection 등 다른 방법으로도 주입하기도 한다.
//@Component // <- bean으로 등록한다는 것. 주입될 대상이라는것. Spring의 편의성이다. Spring의 @ComponentScan이 @Component로 등록된 것들을 모두 찾아서 IoC 컨테이너에 등록해준다.
@Service// Service bean으로 등록한다는 것. @Component와 다른바없지만 더 자세하게 계층을 구분해주는것
public class MemoService {
    //private final JdbcTemplate jdbcTemplate;//<- 이부분 중복된다.
    private final MemoRepository memoRepository; //MemoRepository 타입의 객체를 생성 하는데~ 아래로.

    //@Autowired
    //private MemoRepository memoRepository1; //필드도 주입 할 수 있지만 객체의 불변성을 지키기 위해 final 객체의 생성자 주입을 권장
    //*** (컨트롤러가 직접 메모 서비스를 만들고 있다 - 손님이 음식을 직접만들고 있다 = 강한결합상태 좋지않다..)
    //*** 서비스가 메모 레포지토리를 직접 만들고 있다(제어권) - 손님이 음식을 직접 만들고 있다 = 강한결합상태 좋지않다.

    //IoC 컨테이너에 직접 주입하는 방법
    //public MemoService(ApplicationContext context){ //ApplicationContext쉽게 IoC컨테이너라 생각하면됨
    //    //1. 'Bean'이름으로 가져오기
    //    //MemoRepository memoRepository = (MemoRepository) context.getBean("memoRepository");
    //
    //    //2. 'Bean'클래스 형식으로 가져오기
    //    MemoRepository memoRepository = context.getBean(MemoRepository.class);
    //    this.memoRepository = memoRepository;
    //}

    @Autowired
    // Spring 4.3버전 이후로는 IoC 컨테이너에 주입 할 때, 이 어노테이션이 생략 될 수 있도록 개선되었다. 단 생성자인 경우와 그 생성자가 1개인 경우, 객체의 불변성을 유지하기 위해서 생성자 주입을 추천한다.
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

 */