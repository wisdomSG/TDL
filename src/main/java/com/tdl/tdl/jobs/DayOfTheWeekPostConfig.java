package com.tdl.tdl.jobs;

import com.tdl.tdl.entity.DayOfTheWeekPost;
import com.tdl.tdl.repository.DayOfTheWeekPostRepository;
import com.tdl.tdl.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor

public class DayOfTheWeekPostConfig {
    private final PostRepository repository;
    private final DayOfTheWeekPostRepository dayOfTheWeekPostRepository;
    @Bean(name = "DayOfTheWeekPostJob")
    public Job DayOfTheWeekPostJob(JobRepository jobRepository, Step DayOfTheWeekPostJobStep){
        return new JobBuilder("DayOfTheWeekPostJob", jobRepository)
                .start(DayOfTheWeekPostJobStep)
                .build();

    }
    @Bean(name = "DayOfTheWeekPostJobStep")
    public Step DayOfTheWeekPostJobStep(JobRepository jobRepository , Tasklet DayOfTheWeekPostJobTasklet, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("DayOfTheWeekPostJobStep", jobRepository)
                .tasklet(DayOfTheWeekPostJobTasklet, platformTransactionManager).build();
    }
    @Bean(name = "DayOfTheWeekPostJobTasklet") // 하루마다 총 게시글 수가 쌓임
    public Tasklet DayOfTheWeekPostJobTasklet(){
        return ((contribution, chunkContext) -> {
            Long sun = 0L;
            Long mon = 0L;
            Long tue = 0L;
            Long wed = 0L;
            Long thu = 0L;
            Long fri = 0L;
            Long sat = 0L;
            List<Map<String,Object>> temp =repository.findDayOfWeek(LocalDateTime.now().minusWeeks(1),LocalDateTime.now().minusMinutes(1));
            for (Map<String, Object> stringObjectMap : temp) {
                String count = stringObjectMap.get("count").toString();
                String dats = stringObjectMap.get("date").toString();
                LocalDate localDate2 = LocalDate.parse(dats, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                DayOfWeek dayOfWeek = localDate2.getDayOfWeek();
                int dayOfWeekNumber = dayOfWeek.getValue();
                if (dayOfWeekNumber == 1){
                    sun = Long.valueOf((count));
                } else if (dayOfWeekNumber == 2) {
                    mon = Long.valueOf((count));
                } else if (dayOfWeekNumber == 3) {
                    tue = Long.valueOf((count));
                } else if (dayOfWeekNumber == 4) {
                    wed = Long.valueOf((count));
                } else if (dayOfWeekNumber == 5) {
                    thu = Long.valueOf((count));
                } else if (dayOfWeekNumber == 6) {
                    fri = Long.valueOf((count));
                } else if (dayOfWeekNumber == 7) {
                    sat = Long.valueOf((count));
                }
            }
            DayOfTheWeekPost dayOfTheWeekPost = new DayOfTheWeekPost(sun,mon,tue,wed,thu,fri,sat);
            dayOfTheWeekPostRepository.save(dayOfTheWeekPost);
            return RepeatStatus.FINISHED;
        });
    }
}
