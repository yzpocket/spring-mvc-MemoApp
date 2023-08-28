package com.sparta.memo;
/*Static 폴더에 index.html은 localhost:8080/ 으로(root) Spring에서 기본 페이지로 지정해두었다. */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemoApplication.class, args);
    }

}
