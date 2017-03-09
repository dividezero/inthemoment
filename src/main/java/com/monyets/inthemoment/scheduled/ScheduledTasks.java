package com.monyets.inthemoment.scheduled;

import com.monyets.inthemoment.config.Constants;
import com.monyets.inthemoment.domain.Message;
import com.monyets.inthemoment.service.SlackApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by hazlan on 23/2/2017.
 */

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

    //    @Scheduled(fixedRate = 60000) // one minute
    @Scheduled(fixedRate = 10000)
    public void cleanChannel() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        //todo do REST request here

        Date date = new Date(System.currentTimeMillis() - 2 * 3600 * 1000);

        List<Message> messages = SlackApiService.deleteMessagesBeforeDateByCHannelId(date, Constants.DEFAULT_CHANNEL_ID);
        System.out.println("Deleted: \n"+messages.toString());
    }
}
