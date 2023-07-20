package com.tdl.tdl.schedulers;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JobScheduler {
    @Autowired
    @Qualifier("TotalPostJob")
    private final Job TotalPostJob;

    @Autowired
    @Qualifier("TotalUserJob")
    private final Job TotalUserJob;

    private final JobLauncher jobLauncher;

    @Autowired
    @Qualifier("DayOfTheWeekPostJob")
    private final Job DayOfTheWeekPostJob;


    @Scheduled(cron  ="0 0 0 * * *")
    public void executeJob () {
        try {
            jobLauncher.run(
                    TotalPostJob,
                    new JobParametersBuilder()
                            .addString("datetime", LocalDateTime.now().toString())
                            .toJobParameters()  // job parameter 설정
            );
        } catch (JobExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    @Scheduled(cron  ="0 0 0 * * *")
    public void executeJobSecond () {
        try {
            jobLauncher.run(
                    TotalUserJob,
                    new JobParametersBuilder()
                            .addString("datetime", LocalDateTime.now().toString())
                            .toJobParameters()  // job parameter 설정
            );
        } catch (JobExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Scheduled(cron  ="0 0 0 * * 1") //"0 0 0 * * 1"
    public void executeJobThird() {
        try {
            jobLauncher.run(
                    DayOfTheWeekPostJob,
                    new JobParametersBuilder()
                            .addString("datetime", LocalDateTime.now().toString())
                            .toJobParameters()  // job parameter 설정
            );
        } catch (JobExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}