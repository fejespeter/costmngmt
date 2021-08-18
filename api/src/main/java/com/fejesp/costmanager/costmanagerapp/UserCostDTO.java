package com.fejesp.costmanager.costmanagerapp;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserCostDTO {

    private Integer id;

    private String userId;

    private Double cost;

    private LocalDateTime date;

}
