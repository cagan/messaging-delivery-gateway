package com.cagan.messaginggateway.task;

import com.cagan.messaginggateway.repository.DailyMessageQuotaRepository;
import com.cagan.messaginggateway.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@Component
@AllArgsConstructor
public class DailyQuotaResetTask {
    private final UserRepository userRepository;
    private final DailyMessageQuotaRepository dailyMessageQuotaRepository;

    @Scheduled(cron = "${tasks.reset-quota}")
    public void resetDailyQuota() {
        userRepository.findAll()
                .forEach(user -> dailyMessageQuotaRepository.findByUser(user)
                        .ifPresent(quota -> {
                            quota.setCurrentQuota(0);
                            dailyMessageQuotaRepository.save(quota);
                        }));

        log.info("Quota has been reset at: {}", LocalTime.now());
    }
}
