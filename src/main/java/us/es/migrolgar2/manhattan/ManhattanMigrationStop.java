package us.es.migrolgar2.manhattan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"migrate", "render-migrate"})
public class ManhattanMigrationStop implements CommandLineRunner {
	
	@Autowired
    private ApplicationContext appContext;
	
    @Override
    public void run(String... args) throws Exception {
    	SpringApplication.exit(appContext, () -> 0);
    }
    
}
