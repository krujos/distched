package distsched;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class DistributedSchedulerApplication {

	@Scheduled(fixedDelay = 2000)
	private void tick() {
		System.out.println("tick");
	}

	@Scheduled(cron = "* * * * * *")
	private void tock() {
		System.out.println("tock");
	}

	public static void main(String[] args) {
		SpringApplication.run(DistributedSchedulerApplication.class, args);
		while (true) {
		}
	}
}
