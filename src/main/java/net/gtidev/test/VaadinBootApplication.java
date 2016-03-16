package net.gtidev.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.GregorianCalendar;

@Slf4j
@EnableJpaRepositories
@SpringBootApplication
public class VaadinBootApplication {

  public static void main(String[] args) {
    SpringApplication.run(VaadinBootApplication.class, args);
  }

  @Bean
  public GregorianCalendar calendar() {
    return new GregorianCalendar();
  }

}
