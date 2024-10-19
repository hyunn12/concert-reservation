package io.hhplus.reserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HhplusServerReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(HhplusServerReservationApplication.class, args);
    }

}
