package com.juliuskrah.quartz.service;


import com.juliuskrah.quartz.model.JobDescriptor;
import com.juliuskrah.quartz.model.MeetingJobDescriptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {

    @Autowired
    private Scheduler scheduler;

    public MeetingJobDescriptor createJob(String group, MeetingJobDescriptor descriptor){
        descriptor.setGroup(group);
        JobDetail jobDetail = descriptor.buildJobDetail();
        Set<Trigger> triggersForJob = descriptor.buildTriggers();

        log.info("About to save job with key - {}", jobDetail.getKey());

        try {
            //jobdetail,triggers,replace
            scheduler.scheduleJob(jobDetail,triggersForJob, true);
            log.info("Job with key - {} saved sucessfully", jobDetail.getKey());
        } catch (SchedulerException e) {
            log.error("Could not save job with key - {} due to error - {}", jobDetail.getKey(), e.getLocalizedMessage());
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }

        return descriptor;
    }

    public MeetingJobDescriptor findJob(String group, String name) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(name,group));
            if(jobDetail != null)
                return MeetingJobDescriptor.builder()
                        .name(jobDetail.getKey().getName())
                        .group(jobDetail.getKey().getGroup())
                        .start_date((Date) jobDetail.getJobDataMap().get("start_date"))
                        .end_date((Date) jobDetail.getJobDataMap().get("end_date"))
                        .build();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return null;
    }
}
