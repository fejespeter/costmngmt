package com.fejesp.costmanager.costmanagerapp.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import lombok.Data;

@Data
@Entity
@Table
public class Cost {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 500)
    private String userId;

    @Column()
    private Double cost;

    @Column()
    private LocalDateTime date;

}
