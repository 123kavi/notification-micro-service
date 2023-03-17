package com.notification.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "shedlock")
public class Shedlock implements Serializable {
    @Id
    @Column(name = "name")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;
    @Column(name = "lock_until")
    private Timestamp lockUntil;
    @Column(name = "locked_at")
    private Timestamp lockedAt;
    @Column(name = "locked_by")
    private String lockedBy;
}
