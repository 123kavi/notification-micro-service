//package com.cloudofgoods.notification.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.envers.Audited;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;
//
//@Audited(targetAuditMode = NOT_AUDITED)
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "mail_content")
//public class MailContent implements Serializable {
//
//    private static final long serialVersionUID = 6293245257381842020L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "mail_content_id")
//    private Long id;
//    @Column(name = "template_name")
//    private String templateName;
//    @Column(name = "mail_subject")
//    private String subject;
//    @Column(name = "mail_html_content", columnDefinition = "text")
//    private String content;
//    @Column(name = "message_title", columnDefinition = "text")
//    private String msgTitle;
//    @Column(name = "message_body", columnDefinition = "text")
//    private String msgBody;
//    @Column(name = "top_banner_advertisement_url")
//    private String topBannerAdUrl;
//    @Column(name = "mail_send_by")
//    private String sendBy; // regards
//    @Column(name = "last_updated_user")
//    private String lastUpdatedUser;
//    @Column(name = "last_updated_date_time")
//    private Timestamp lastUpdatedDateTime;
//
//
//    @OneToMany(mappedBy = "mailContent", cascade = { CascadeType.MERGE}, fetch = FetchType.LAZY)
//    private List<ScheduleEmailRequest> scheduleEmailRequests = new ArrayList<>();
////    @OneToMany(mappedBy = "mailContent", cascade = { CascadeType.MERGE}, fetch = FetchType.LAZY)
////    private List<EmailSchedulerLog> emailSchedulerLogs = new ArrayList<>();
//
//
//
//
//
//
//}
package com.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Audited(targetAuditMode = NOT_AUDITED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mail_content")
public class MailContent implements Serializable {

    private static final long serialVersionUID = 6293245257381842020L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_content_id")
    private Long id;
    @Column(name = "template_name")
    private String templateName;
    @Column(name = "mail_subject")
    private String subject;
    @Column(name = "mail_html_content", columnDefinition = "text")
    private String content;
    @Column(name = "message_title", columnDefinition = "text")
    private String msgTitle;
    @Column(name = "message_body", columnDefinition = "text")
    private String msgBody;
    @Column(name = "top_banner_advertisement_url")
    private String topBannerAdUrl;
    @Column(name = "mail_send_by")
    private String sendBy; // regards
    @Column(name = "last_updated_user")
    private String lastUpdatedUser;
    @Column(name = "last_updated_date_time")
    private Timestamp lastUpdatedDateTime;


//    @OneToMany(mappedBy = "mailContent", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ScheduleEmailRequest> scheduleEmailRequests = new ArrayList<>();

}
