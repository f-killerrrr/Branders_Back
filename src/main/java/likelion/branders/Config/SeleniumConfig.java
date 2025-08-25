package likelion.branders.Config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {
    @Value("${chrome.driver.path}")
    private String chromeDriverPath;
    
    @Bean
    public WebDriver webDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments(new String[]{"--headless"});
        options.addArguments(new String[]{"--no-sandbox"});
        options.addArguments(new String[]{"--disable-dev-shm-usage"});
        return new ChromeDriver(options);
    }
}
