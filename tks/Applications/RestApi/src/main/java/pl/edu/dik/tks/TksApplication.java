package pl.edu.dik.tks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"pl.edu.dik.utils.config",
		"pl.edu.dik.adapters.config",       // Configurations
		"pl.edu.dik.adapters",    // Adapters
		"pl.edu.dik.application.services",  // Service implementations
		"pl.edu.dik.tks",        // Controllers
		"pl.edu.dik.ports"       // Service interfaces
})
public class TksApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(TksApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
