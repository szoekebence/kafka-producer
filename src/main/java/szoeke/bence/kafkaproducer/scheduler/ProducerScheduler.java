package szoeke.bence.kafkaproducer.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProducerScheduler {

    @Scheduled(fixedDelayString = "${spring.schedule.fixedDelayMs}",
            initialDelayString = "${spring.schedule.initialDelayMs}")
    public void run() {
        
    }
}
