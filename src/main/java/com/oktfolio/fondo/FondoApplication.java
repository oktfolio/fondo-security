package com.oktfolio.fondo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/09/05
 */
@MapperScan(basePackages = "com.oktfolio.fondo.dao")
@SpringBootApplication
public class FondoApplication {
    public static void main(String[] args) {
        SpringApplication.run(FondoApplication.class, args);
    }
}
