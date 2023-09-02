package com.sparta.memo.repository;

import com.sparta.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findAllByOrderByModifiedAtDesc();
    List<Memo> findAllByContentsContainsOrderByModifiedAtDesc(String keyword);

    //JpaRepository<"@Entity 클래스", "@Id 의 데이터 타입">를 상속받는 interface 로 선언하면
    //지금 삭제된 소스코드들 모두 기본적인 틀이 작성되어있는 프레임워크를 사용만 하면 될 정도로 간편해진다.
    //따라서 현재 이 MemoRepository가 클래스에서 추상화 인터페이스로 변경되면서 삭제된 코드들은
    //해당 인터페이스의 실제 구현체는 Spring이 자동으로 만들어두었으며, ***** SimpleJpaRepository.java *****에서
    //추상인터페이스를 받는 구현체의 실질 메소드들을 확인 할 수 있다.  <---- 이전 코드들은
    //그것들을 기준으로 비슷한 형태로 실습 했던 것이기 때문에 비교하면서 구현체에서 필요한 기능들을 확인하고 사용하면 되는 것이다.
    //제네릭으로 변할 수 있는 타입들을 대명사로 구현해두었기 때문에, 구현체 메소드 사용 시 전달되는 타입도 커버되는 상황이다.
    //그럼 정확한 목적에 따라 구현체에 있는 어떤 기능들을 사용해야 하는지 생각하는 것이 이제 개발자의 할 일이다.
}


/*
SPRING BOOT 이전의 공부 주석

//@Component // <- bean으로 등록한다는 것. 주입될 대상이라는것. Spring의 편의성이다. Spring의 @ComponentScan이 @Component로 등록된 것들을 모두 찾아서 IoC 컨테이너에 등록해준다.
@Repository// Repository bean으로 등록한다는 것. @Component와 다른바없지만 더 자세하게 계층을 구분해주는것
public class MemoRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Memo save(Memo memo) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, memo.getUsername());
                    preparedStatement.setString(2, memo.getContents());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        memo.setId(id);

        return memo;
    }

    public List<MemoResponseDto> findAll() {
        // DB 조회
        String sql = "SELECT * FROM memo";

        return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                return new MemoResponseDto(id, username, contents);
            }
        });
    }

    public void update(Long id, MemoRequestDto requestDto) {
        String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM memo WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Memo findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM memo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Memo memo = new Memo();
                memo.setUsername(resultSet.getString("username"));
                memo.setContents(resultSet.getString("contents"));
                return memo;
            } else {
                return null;
            }
        }, id);
    }

    //트랜잭션 전파 예시
    //@Transactional //기본 Required 옵션, 부모메서드에 트랜잭션이 되있으면, 자손메소드도 트랜잭션도 이어진다(전파된다)
    public Memo createMemo(EntityManager em) {
        Memo memo = em.find(Memo.class, 1);
        memo.setUsername("Robbie");
        memo.setContents("@Transactional 전파 테스트 중!");

        System.out.println("createMemo 메서드 종료");
        return memo;
    }
}

 */