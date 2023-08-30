package com.example.crawling;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsCrawlerScheduler {

    @Autowired
    private NewsCrawler newsCrawler;

    @Scheduled(fixedRate = 300000)
    public void scheduleCrawlNews() {
        newsCrawler.crawlNews();
    }
}
