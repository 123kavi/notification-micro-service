package com.notification.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "sent_sms_log")
public class SentSmsLog implements Serializable {
    @Id
    @Column(name = "sent_sms_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "message_content")
    private String messageContent;
    @Column(name = "sms_sent_status")
    private boolean sentStatus;
    @Column(name = "sms_response")
    private String smsResponse;
    @Column(name = "sent_date_time")
    private Timestamp sentDateTime;
}
