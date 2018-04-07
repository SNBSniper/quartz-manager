package com.juliuskrah.quartz.model;

import static java.time.ZoneId.systemDefault;
import static java.util.UUID.randomUUID;
import static org.quartz.CronExpression.isValidExpression;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;
import org.quartz.JobDataMap;
import org.quartz.Trigger;


@Builder
@Getter @Setter
@Accessors(fluent = true)
@ToString
public class TriggerDescriptor {
	@NotBlank
	private String name;
	private String group;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Santiago")
	private Date fireTime;
	private String cron;

	/**
	 * Convenience method for building a Trigger
	 * 
	 * @return the Trigger associated with this descriptor
	 */
	public Trigger buildTrigger() {
		// @formatter:off
		if (!isEmpty(cron)) {
			if (!isValidExpression(cron))
				throw new IllegalArgumentException("Provided expression " + cron + " is not a valid cron expression");
			return newTrigger()
					.withIdentity(name(), group)
					.withSchedule(cronSchedule(cron)
							.withMisfireHandlingInstructionFireAndProceed()
							.inTimeZone(TimeZone.getTimeZone(systemDefault())))
					.usingJobData("cron", cron)
					.build();
		} else if (!isEmpty(fireTime)) {

			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("fireTime", fireTime);
			return newTrigger()
					.withIdentity(name(), group)
					.withSchedule(simpleSchedule()
							.withMisfireHandlingInstructionNextWithExistingCount())
					.startAt(fireTime)
					.usingJobData(jobDataMap)
					.build();
		}
		// @formatter:on
		throw new IllegalStateException("unsupported trigger descriptor " + this);
	}

	/**
	 * 
	 * @param trigger
	 *            the Trigger used to build this descriptor
	 * @return the TriggerDescriptor
	 */
//	public static TriggerDescriptor buildDescriptor(Trigger trigger) {
//		// @formatter:off
//		return new TriggerDescriptor()
//				.name(trigger.getKey().getName())
//				.setGroup(trigger.getKey().getGroup())
//				.setFireTime((LocalDateTime) trigger.getJobDataMap().get("fireTime"))
//				.setCron(trigger.getJobDataMap().getString("cron"));
//		// @formatter:on
//	}
}
