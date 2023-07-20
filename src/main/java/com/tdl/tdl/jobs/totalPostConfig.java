package com.tdl.tdl.jobs;

import com.tdl.tdl.entity.TotalPost;
import com.tdl.tdl.repository.PostRepository;
import com.tdl.tdl.repository.TotalPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


@Slf4j
@Configuration
@RequiredArgsConstructor
public class totalPostConfig {
    private final PostRepository repository;
    private final TotalPostRepository totalPostRepository;
    @Bean(name = "TotalPostJob")
    public Job TotalPostJob(JobRepository jobRepository, Step TotalPostJobStep){
        return new JobBuilder("TotalPostJob", jobRepository)
                .start(TotalPostJobStep)
                .build();

    }
    @Bean(name = "TotalPostJobStep")
    public Step TotalPostStep(JobRepository jobRepository ,Tasklet TotalPostJobTasklet,PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("TotalPostJobStep", jobRepository)
                .tasklet(TotalPostJobTasklet, platformTransactionManager).build();
    }
    @Bean(name = "TotalPostJobTasklet") // 하루마다 총 게시글 수가 쌓임
    public Tasklet TotalPostJobTasklet(){
        return ((contribution, chunkContext) -> {
//            LocalDateTime currentDateTime = LocalDateTime.now().minusHours(24);
            Long TotalPost = (long) repository.findAll().size();
//            Long eveTotalPost= (long) repository.findAllByCreatedAtIsBefore(currentDateTime).size();
//            Long tendency = TotalPost - eveTotalPost;
            TotalPost totalPost = new TotalPost(TotalPost);
            totalPostRepository.save(totalPost);
            return RepeatStatus.FINISHED;
        });
    }

}