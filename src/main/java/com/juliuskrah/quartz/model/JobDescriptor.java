package com.juliuskrah.quartz.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import static org.quartz.JobBuilder.*;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.juliuskrah.quartz.job.EmailJob;

import lombok.Data;

@Data
@Getter
@Setter
public class JobDescriptor {
	// TODO add boolean fields for HTML and Attachments
	@NotBlank
	private String name;

	private String group;
	@NotEmpty
	private String subject;
	@NotEmpty
	private String messageBody;
	@NotEmpty
	private List<String> to;
	private List<String> cc;
	private List<String> bcc;
	private Map<String, Object> data = new LinkedHashMap<>();
	@JsonProperty("triggers")
	private List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();


	/**
	 * Convenience method for building Triggers of Job
	 * 
	 * @return Triggers for this JobDetail
	 */
	@JsonIgnore
	public Set<Trigger> buildTriggers() {
		Set<Trigger> triggers = new LinkedHashSet<>();
		for (TriggerDescriptor triggerDescriptor : triggerDescriptors) {
			triggers.add(triggerDescriptor.buildTrigger());
		}

		return triggers;
	}

	/**
	 * Convenience method that builds a JobDetail
	 * 
	 * @return the JobDetail built from this descriptor
	 */
	public JobDetail buildJobDetail() {
		// @formatter:off
		JobDataMap jobDataMap = new JobDataMap(getData());
		jobDataMap.put("subject", subject);
		jobDataMap.put("messageBody", messageBody);
		jobDataMap.put("to", to);
		jobDataMap.put("cc", cc);
		jobDataMap.put("bcc", bcc);
		return newJob(EmailJob.class)
                .withIdentity(getName(), getGroup())
                .usingJobData(jobDataMap)
                .build();
		// @formatter:on
	}

	/**
	 * Convenience method that builds a descriptor from JobDetail and Trigger(s)
	 * 
	 * @param jobDetail
	 *            the JobDetail instance
	 * @param triggersOfJob
	 *            the Trigger(s) to associate with the Job
	 * @return the JobDescriptor
	 */
	@SuppressWarnings("unchecked")
	public static JobDescriptor buildDescriptor(JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {
		// @formatter:off
		List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();

		for (Trigger trigger : triggersOfJob) {
			triggerDescriptors.add(TriggerDescriptor.builder()
					.name(trigger.getKey().getName())
					.group(trigger.getKey().getGroup())
					.fireTime((Date) trigger.getJobDataMap().get("fireTime"))
					.cron(trigger.getJobDataMap().getString("cron"))
					.build());
		}

		JobDescriptor jobDescriptor = new JobDescriptor();
		jobDescriptor.setGroup(jobDetail.getKey().getGroup());
		jobDescriptor.setName(jobDetail.getKey().getName());
		jobDescriptor.setSubject(jobDetail.getJobDataMap().getString("subject"));
		jobDescriptor.setMessageBody(jobDetail.getJobDataMap().getString("messageBody"));
		jobDescriptor.setTo((List<String>)jobDetail.getJobDataMap().get("to"));
		jobDescriptor.setCc((List<String>)jobDetail.getJobDataMap().get("cc"));
		jobDescriptor.setBcc((List<String>)jobDetail.getJobDataMap().get("bcc"));
		jobDescriptor.setData(jobDetail.getJobDataMap().getWrappedMap());
		jobDescriptor.setTriggerDescriptors(triggerDescriptors);
		return jobDescriptor;
		// @formatter:on
	}
}
