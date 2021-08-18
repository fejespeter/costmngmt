package com.fejesp.costmanager.costmanagerapp;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCostRequestParamDTO {

    private String userId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
