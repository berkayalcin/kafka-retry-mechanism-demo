package com.example.kafkaretrymechanismdemo.jobs;

import com.example.kafkaretrymechanismdemo.service.EmailOutboxItemService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Daemon implements Job {
    private final EmailOutboxItemService emailOutboxItemService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        emailOutboxItemService.process();
    }
}
