package com.notification.entity;

import com.notification.enums.ActiveStatus;
import com.notification.enums.DevicePlatform;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Audited
@Getter
@Setter
@Entity
@Table(name = "notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 3655675803974923350L;

    @Id
    @Column(name = "notification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "firebase_client_token")
    private String token;
    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private DevicePlatform platform;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActiveStatus activeStatus;
    @Column(name = "last_updated_date_time")
    private Timestamp lastUpdatedDateTime;
    @Column(name = "last_updated_user")
    private String lastUpdatedUser;
}
