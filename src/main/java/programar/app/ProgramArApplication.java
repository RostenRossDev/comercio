package programar.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import programar.app.services.IUploadFileService;

@EnableScheduling
@SpringBootApplication
public class ProgramArApplication implements CommandLineRunner {
	@Autowired
	IUploadFileService uploadService;

	public static void main(String[] args) {
		SpringApplication.run(ProgramArApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		uploadService.deleteAll();
		uploadService.init();
	}
}
