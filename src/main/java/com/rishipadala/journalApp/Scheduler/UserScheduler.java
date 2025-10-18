package com.rishipadala.journalApp.Scheduler;

import com.rishipadala.journalApp.Entity.JournalEntry;
import com.rishipadala.journalApp.Entity.User;
import com.rishipadala.journalApp.Repository.UserRepoImpl;
import com.rishipadala.journalApp.Service.EmailService;
import com.rishipadala.journalApp.Service.SentimentConsumerService;
import com.rishipadala.journalApp.cache.AppCache;
import com.rishipadala.journalApp.enums.Sentiment;
import com.rishipadala.journalApp.model.SentimentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    private static final Logger log = LoggerFactory.getLogger(UserScheduler.class); // For checking

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepoImpl userRepo;

    @Autowired
    private SentimentConsumerService sentimentConsumerService;

    @Autowired
    private AppCache appCache;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    //Purpose: Periodically sends an email to users summarizing their most frequent sentiment from journal entries in the last 7 days.
//    @Scheduled(cron = "0 0 9 ? * SUN")
    @Scheduled(cron = "0 * * ? * *")
    public void fetchUsersAndSendSentiMail(){
        log.info(":-) --- Scheduler is running! Fetching users for sentiment analysis... ---");
        List<User> users = userRepo.getUsersbySA(); //fetch users
        for(User user: users){
             List<JournalEntry> journalEntries = user.getJournalEntries(); //Gets their journal entries.
            //Filters entries from the last 7 days.
             List<Sentiment> sentiments  = journalEntries.stream().filter(x -> x.getDateTime()
                     .isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                     .map(x -> x.getSentiment())
                     .collect(Collectors.toList());


            Map<Sentiment ,Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment: sentiments) {
                if (sentiments != null) {
                    /*
                    sentimentCounts.getOrDefault(sentiment, 0) gets the current count for this sentiment, or 0 if it doesn't exist.
                     + 1 increments the count.
                     put stores the new count back in the map.
                     */
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }

            Sentiment mostFrequentSentiment = null; //  Initializes a variable to keep track of the sentiment with the highest count.
            int maxCount = 0; //Initializes a variable to keep track of the highest count found.
            for (Map.Entry<Sentiment,Integer> entry : sentimentCounts.entrySet()){
                if(entry.getValue() > maxCount){
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if (mostFrequentSentiment != null){
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Your Sentiment for last 7 Days : " + mostFrequentSentiment).build();
                //Encountering Kafka FallBack by doing HardCode in the try-catch block.
                try{
                    kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail() , sentimentData);
                } catch (Exception e) {
                    emailService.sendMail(sentimentData.getEmail(),"Your Sentiment For Previous Week!",sentimentData.getSentiment());
                }
            }

        }
    }

    @Scheduled(cron = "0 0/10 * 1/1 * ?")
    public void ClearAppCache(){
        appCache.init();
    }
}
