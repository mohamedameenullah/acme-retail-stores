package com.acme.retail.acmeretailstores.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

@RestController
public class JobController {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @GetMapping("/runJob")
    public BatchStatus loadData() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("timestamp", Calendar.getInstance().getTime())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        while (jobExecution.isRunning()) {
            System.out.println("..................");
        }
        return jobExecution.getStatus();
    }


}
