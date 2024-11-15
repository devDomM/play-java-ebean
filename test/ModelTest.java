import io.ebean.PagedList;
import models.Computer;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;
import repository.ComputerRepository;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class ModelTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    private String formatted(Date date) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    @Test
    public void findById() {
        final ComputerRepository computerRepository = app.injector().instanceOf(ComputerRepository.class);
        final Optional<Computer> macintosh = computerRepository.lookup(21L);

        await().atMost(1, SECONDS).until(() -> {
            return macintosh
                .map(mac -> mac.getName().equals("Macintosh") && formatted(mac.getIntroduced()).equals("1984-01-24"))
                .orElseGet(() -> false);
        });
    }
    
    @Test
    public void pagination() {
        final ComputerRepository computerRepository = app.injector().instanceOf(ComputerRepository.class);
        PagedList<Computer> page = computerRepository.page(1, 20, "name", "ASC", "");

        // Test the completed result
        await().atMost(1, SECONDS).until(() -> {
            return page.getTotalCount() == 574 &&
                    page.getTotalPageCount() == 29 &&
                    page.getList().size() == 20;
        });
    }
    
}
