package com.example.crawling;

import com.beust.ah.A;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public void crawlNews() {
        if (System.getProperty("os.name").toLowerCase().contains("mac"))
            System.setProperty("webdriver.chrome.driver", "./chromedriver-mac-arm64/chromedriver");
        else if (System.getProperty("os.name").toLowerCase().contains("win"))
            System.setProperty("webdriver.chrome.driver", "./chromedriver-win64/chromedriver");

        // 크롬 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://news.naver.com/main/list.naver?mode=LS2D&mid=shm&sid1=102&sid2=252");

        WebElement webElement = driver.findElement(By.xpath("//*[@id=\"main_content\"]/div[2]/ul[1]"));

        List<WebElement> liList = webElement.findElements(By.tagName("li"));

        List<News> newsList = new ArrayList<>();
        for (WebElement element : liList) {
            String title = "";
            String link = "";
            String photo = "";
            String writer = "";

            List<WebElement> aTags = element.findElements(By.tagName("a"));
            if (!aTags.isEmpty()) {
                link = aTags.get(0).getAttribute("href");
                title = aTags.get(aTags.size() - 1).getText();
            }

            WebElement photoClass = element.findElement(By.className("photo"));
            if (photoClass != null)
                photo = photoClass.findElement(By.tagName("img")).getAttribute("src");

            WebElement writerClass = element.findElement(By.className("writing"));
            if (writerClass != null)
                writer = writerClass.getText();

            News news = News.builder()
                    .title(title)
                    .writer(writer)
                    .link(link)
                    .photo(photo)
                    .build();
            newsRepository.save(news);
            newsList.add(news);
        }


    }
}
