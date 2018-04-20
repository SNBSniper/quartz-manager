package com.juliuskrah.quartz.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.juliuskrah.quartz.job.MeetingJob;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import static org.quartz.JobBuilder.*;

import java.util.*;

@Data
@Getter @Setter
@Builder
@Slf4j
public class MeetingJobDescriptor {


    @NotBlank
    private String name;

    private String group;
    @NotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Santiago")
    private Date start_date;
    @NotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Santiago")
    private  Date end_date;

    @JsonProperty("triggers")
    @Singular
    private List<TriggerDescriptor> triggerDescriptors;

    @JsonIgnore
    public Set<Trigger> buildTriggers(){
        Set<Trigger> triggers = new LinkedHashSet<>();
        if(triggerDescriptors == null)
            this.triggerDescriptors = new ArrayList<>();
        for(TriggerDescriptor i : triggerDescriptors){
            Trigger j = i.buildTrigger();
            triggers.add(j);
        }
        return triggers;
    }

    public JobDetail buildJobDetail(){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("start_date", start_date);
        jobDataMap.put("end_date", end_date);
        log.info("start_date for job"+start_date );
        log.info("end_date for job"+end_date );
        return newJob(MeetingJob.class)
                .requestRecovery(true)
                .withIdentity(this.getName(), this.getGroup())
                .usingJobData(jobDataMap)
                .storeDurably(true)
                .build();
    }
}
