package com.cagan.messaginggateway.service;

import com.cagan.messaginggateway.domain.DailyMessageQuota;
import com.cagan.messaginggateway.domain.User;
import com.cagan.messaginggateway.repository.DailyMessageQuotaRepository;
import com.cagan.messaginggateway.security.AuthoritiesConstants;
import com.cagan.messaginggateway.security.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DailyMessageQuotaService {
    private final DailyMessageQuotaRepository dailyMessageQuotaRepository;

    public boolean isDailyQuotaExceeded(@NotNull User user) {
        if (SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.ADMIN)) {
            return false;
        }

        int userDailyMessageQuota = user.getDailyMessageQuota();

        Optional<DailyMessageQuota> dailyMessageQuota = dailyMessageQuotaRepository.findByUser(user);

        if (dailyMessageQuota.isEmpty()) {
            return false;
        }

        return dailyMessageQuota.get().getCurrentQuota() >= userDailyMessageQuota;
    }

    public Optional<DailyMessageQuota> createUserInitialDailyMessageQuota(User user) {
        if (dailyMessageQuotaRepository.findByUser(user).isPresent()) {
            return Optional.empty();
        }

        DailyMessageQuota dailyMessageQuota = new DailyMessageQuota();
        dailyMessageQuota.setCurrentQuota(0);
        dailyMessageQuota.setUser(user);
        dailyMessageQuotaRepository.save(dailyMessageQuota);
        return Optional.of(dailyMessageQuota);
    }

    public void incrementQuotaUsage(User user) {
        dailyMessageQuotaRepository.findByUser(user)
                .ifPresent(dailyMessageQuota -> {
                    int currentQuota = dailyMessageQuota.getCurrentQuota() + 1;
                    dailyMessageQuota.setCurrentQuota(currentQuota);
                    dailyMessageQuotaRepository.save(dailyMessageQuota);
                    log.info("[USER: {}], [DAILY_MESSAGE: {}]", user, currentQuota);
                });
    }
}
