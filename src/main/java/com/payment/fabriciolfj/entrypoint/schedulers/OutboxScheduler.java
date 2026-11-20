package com.payment.fabriciolfj.entrypoint.schedulers;

import com.payment.fabriciolfj.adapter.outbox.OutboxSchedulerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.payment.fabriciolfj.constants.MdcConstants.CORRELATION_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxSchedulerAdapter outboxSchedulerAdapter;

    @Scheduled(cron = "0 */5 * * * *")
    public void execute() {
        MDC.put(CORRELATION_ID, UUID.randomUUID().toString());
        try {
            log.info("init process scheduler");
            outboxSchedulerAdapter.process();
        } finally {
            MDC.clear();
        }
    }
}
