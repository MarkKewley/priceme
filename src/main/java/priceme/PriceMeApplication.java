package priceme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import priceme.persistence.Price;
import priceme.persistence.PriceRepository;


@SpringBootApplication
public class PriceMeApplication implements CommandLineRunner {
    @Autowired
    private PriceRepository priceRepository;

    public static void main(String[] args) {
        SpringApplication.run(PriceMeApplication.class, args);
    }

    /**
     * For the sake of the Case Study we only have a single product being stored. If this was a real life
     * application we would not be doing this.
     */
    @Override
    public void run(String... strings) throws Exception {
        priceRepository.deleteAll();

        priceRepository.save(new Price("13860428", 12.34, "USD"));
    }
}
