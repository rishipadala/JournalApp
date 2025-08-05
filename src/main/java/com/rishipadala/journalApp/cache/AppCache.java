package com.rishipadala.journalApp.cache;

import com.rishipadala.journalApp.Entity.ConfigJournalAppEntity;
import com.rishipadala.journalApp.Repository.ConfigJournalAppRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys{
        WEATHER_API;
    }

    @Autowired
    private ConfigJournalAppRepo configJournalAppRepo;

    public Map<String,String> appCache;

    //The @PostConstruct method runs after Spring injects dependencies (like configJournalAppRepo),
    // so you can safely use those dependencies.
    @PostConstruct
    //Initializing appCache inside init ensures it does not contain any old or stale data if init is called again (for example, in tests or if the bean is reloaded).
    public void init(){ //init for initialization
        appCache = new HashMap<>();
        List<ConfigJournalAppEntity> all = configJournalAppRepo.findAll(); // We fetch all the key-value pairs from Mongo.
        for (ConfigJournalAppEntity configJournalAppEntity : all){
            appCache.put(configJournalAppEntity.getKey() , configJournalAppEntity.getValue());
            //For every item in the list all, take its key and value, and put them into the appCache map.
            //So, after this loop, appCache will have all the configuration data from the database, ready to use in your app.
        }
    }

}
