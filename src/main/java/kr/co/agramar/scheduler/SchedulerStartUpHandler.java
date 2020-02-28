package kr.co.agramar.scheduler;

import kr.co.agramar.service.SchedulerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class SchedulerStartUpHandler implements ApplicationRunner {

    private SchedulerService schedulerService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info("Schedule all new scheduler jobs at app startup - starting");
            schedulerService.clearAndStartAllSchedulers();
            log.info("Schedule all new scheduler jobs at app startup - complete");
        } catch (Exception ex) {
            log.error("Schedule all new scheduler jobs at app startup - error", ex);
        }
    }
}
