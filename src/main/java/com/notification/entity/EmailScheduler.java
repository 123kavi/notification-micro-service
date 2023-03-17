package com.notification.entity;

import com.notification.enums.ScheduledStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "email_scheduler")
public class EmailScheduler implements Serializable {

    private static final long serialVersionUID = 5870871156623264901L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_scheduler_id")
    private Long id;
    @NaturalId
    @Column(name = "job_id")
    private String jobId;
    @Column(name = "cron_expression")
    private String cronExp;
    @Column(name = "minutes")
    private Integer minute;
    @Column(name = "hour")
    private Integer hour;
    @Column(name = "day_of_month")
    private Integer dayOfMonth;
    @Column(name = "month")
    private Integer month;
    @Column(name = "day_of_week")
    private Integer dayOfWeek;
    @Column(name = "year")
    private Integer year;
    @Enumerated(EnumType.STRING)
    @Column(name = "scheduled_status")
    private ScheduledStatus scheduledStatus;
    @Column(name = "scheduled_email_count")
    private Integer scheduledEmailCount;
    @Column(name = "is_active")
    private boolean activeStatus;
    @Column(name = "is_multiple_template_group")
    private boolean isMultipleTemplateGroup;
    @Column(name = "scheduled_date_time")
    private Timestamp scheduledDatetime;
    @Column(name = "last_updated_date_time")
    private Timestamp lastUpdatedDateTime;
    @OneToMany(mappedBy = "emailScheduler", fetch = FetchType.LAZY)
    private List<EmailSchedulerDetail> schedulerDetails;
}
