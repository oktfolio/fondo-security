package com.oktfolio.fondo.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @author oktfolio oktfolio@gmail.com
 * @date 2019/09/05
 */
@Configuration
public class ValidateConfig {

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                // 开启快速校验--默认校验所有参数，false校验全部
                .addProperty("hibernate.validator.fail_fast", "false")
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}