package io.hhplus.reserve.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class TestContainerSupport {

    private static final JdbcDatabaseContainer MYSQL = new MySQLContainer("mysql:8");

    private static final GenericContainer REDIS = new GenericContainer("redis:6.2.6").withExposedPorts(6379);

    private static final KafkaContainer KAFKA = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest")
                    .asCompatibleSubstituteFor("apache/kafka")
    );

    @BeforeAll
    public static void startContainers() {
        log.info("Starting containers...");
        KAFKA.start();
        MYSQL.start();
        REDIS.start();
        log.info("Containers started successfully.");
    }

    @AfterAll
    public static void stopContainers() {
        log.info("Stopping containers...");
        KAFKA.stop();
        MYSQL.stop();
        REDIS.stop();
        log.info("Containers stopped successfully.");
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        // kafka
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("spring.kafka.consumer.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", KAFKA::getBootstrapServers);

        // mysql
        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.~username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);

        // redis
        registry.add("spring.data.redis.host", REDIS::getHost);
        registry.add("spring.data.redis.port", () -> REDIS.getMappedPort(6379));
    }

    static {
        MYSQL.waitingFor(Wait.forListeningPort());
        REDIS.waitingFor(Wait.forListeningPort());
        KAFKA.waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60)));
    }
}
