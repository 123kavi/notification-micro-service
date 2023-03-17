package com.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feature_flag")
public class FeatureFlag implements Serializable {

    private static final long serialVersionUID = 8145001703224019665L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_flag_id")
    private Long id;
    @Column(name = "feature_flag_name")
    private String name; // References template_name column in mail_content table
    @Column(name = "feature_flag_description")
    private String description;
    @Column(name = "status", nullable = false)
    private boolean status;
    @Column(name = "last_updated_date_time")
    private Timestamp lastUpdatedDateTime;
    @Column(name = "last_updated_user")
    private String lastUpdatedUser;
}
