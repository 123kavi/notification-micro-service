package com.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sent_email_log")
public class SentEmailLog implements Serializable {
    private static final long serialVersionUID = 1994423903302962875L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sent_email_log_id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "email_content", columnDefinition = "text")
    private String emailContent; // Refers column 'mail_html_content' of mail_content
    @Column(name = "sent_date_time")
    private Timestamp sentDateTime;
}
