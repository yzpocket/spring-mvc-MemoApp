package com.sparta.memo;
/*Static 폴더에 index.html은 localhost:8080/ 으로(root) Spring에서 기본 페이지로 지정해두었다. */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //<- @ComponentScan이 해당 MemoApplication 이 위치한 하위 패키지들을 모두 스캔하면서 @Component들을 찾아내서 빈으로 등록해준다.
public class MemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemoApplication.class, args);
    }

}
