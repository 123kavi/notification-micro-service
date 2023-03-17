
package com.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Audited(targetAuditMode = NOT_AUDITED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedule_mail_content")
public class Content implements Serializable {

    private static final long serialVersionUID = 6293245257381842020L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_mail_content_id")
    private Long id;
    @Column(name = "template_name")
    private String templateName;

    @Column(name = "mail_html_content", columnDefinition = "text")
    private String content;

    @Column(name = "top_banner_advertisement_url")
    private String topBannerAdUrl;
    @Column(name = "mail_send_by")
    private String sendBy; // regards
    @Column(name = "last_updated_user")
    private String lastUpdatedUser;
    @Column(name = "last_updated_date_time")
    private Timestamp lastUpdatedDateTime;


    @OneToMany(mappedBy = "mailContent1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEmailRequest> scheduleEmailRequests = new ArrayList<>();

}
