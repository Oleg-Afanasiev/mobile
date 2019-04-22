package com.academy.mobile;

import com.academy.mobile.config.WebSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * The Application starter using spring boot engine
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
//@Import(WebSecurityConfig.class)
public class Application {
    /**
     * Entry point for running application
     *
     * @param args additional running params
     */
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");

        SpringApplication.run(Application.class, args);
    }
}
