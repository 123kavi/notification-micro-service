package com.notification.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "email_scheduler_detail")
public class EmailSchedulerDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_scheduler_detail_id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "template_name")
    private String templateName;
    @Column(name = "mobile_no")
    private String mobileNo;
    @Column(name = "receiver_email")
    private String receiverEmail;
    @Column(name = "top_banner_ad_url")
    private String topBannerAdUrl;
    @Column(name = "orders_list", columnDefinition = "text")
    private String ordersList;
    @Column(name = "email_content", columnDefinition = "text")
    private String emailContent;
    @Column(name = "message_content", columnDefinition = "text")
    private String messageContent;
    @Column(name = "inserted_date_time")
    private Timestamp insertedDateTime;
    @ManyToOne
    @JoinColumn(name = "email_scheduler_id")
    private EmailScheduler emailScheduler;
}