package programar.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProgramArApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgramArApplication.class, args);
	}

}
