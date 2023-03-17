package com.notification.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "email_scheduler_log_new")
public class EmailSchedulerLogNew implements Serializable {

    private static final long serialVersionUID = 5862417961556749629L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_scheduler_log_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobId", referencedColumnName = "job_id")
    private EmailScheduler emailScheduler;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id", referencedColumnName = "message_title")
//    private MailContent mailContent;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "mobile_number")
    private String mobileNumber;
    @Column(name = "receiver_email")
    private String email;
    @Column(name = "response_message")
    private String response;
    @Column(name = "executed_date_time")
    private Timestamp executedDateTime;
}
