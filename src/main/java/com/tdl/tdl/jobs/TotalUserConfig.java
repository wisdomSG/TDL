package com.tdl.tdl.jobs;
import com.tdl.tdl.entity.TotalUser;
import com.tdl.tdl.repository.TotalUserRepository;
import com.tdl.tdl.repository.UserRepository;
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

public class TotalUserConfig {
    private final UserRepository repository;
    private final TotalUserRepository totalUserRepository;

    @Bean(name = "TotalUserJob")
    public Job TotalUserJob(JobRepository jobRepository, Step TotalUserJobStep) {
        return new JobBuilder("TotalUserJob", jobRepository)
                .start(TotalUserJobStep)
                .build();

    }
    @Bean(name = "TotalUserJobStep")
    public Step TotalUserJobStep(JobRepository jobRepository, Tasklet TotalUserJobTasklet,PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("TotalUserJobStep", jobRepository)
                .tasklet(TotalUserJobTasklet, platformTransactionManager)
                .build();
    }
    @Bean(name = "TotalUserJobTasklet")
    public Tasklet TotalUserJobTasklet(){
        return ((contribution, chunkContext) -> {
            Long TotalUser = (long) repository.findAll().size();
//            Long eveTotalPost= (long) repository.findAllByCreatedAtIsBefore(currentDateTime).size();
//            Long tendency = TotalPost - eveTotalPost;
            TotalUser totalUser = new TotalUser(TotalUser);
            totalUserRepository.save(totalUser);
            return RepeatStatus.FINISHED;
        });
    }
}