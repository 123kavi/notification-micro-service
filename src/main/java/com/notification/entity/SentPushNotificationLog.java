package com.notification.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "sent_push_notification_log")
public class SentPushNotificationLog {

    @Id
    @Column(name = "sent_push_notification_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "message_title")
    private String messageTitle;
    @Column(name = "message_body")
    private String messageBody;
    @Column(name = "success_count")
    private Integer successCount;
    @Column(name = "failure_count")
    private Integer failureCount;
    @Column(name = "sent_date_time")
    private Timestamp sentDateTime;
}
