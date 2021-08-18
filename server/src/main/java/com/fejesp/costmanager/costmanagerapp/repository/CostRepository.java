package com.fejesp.costmanager.costmanagerapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.fejesp.costmanager.costmanagerapp.repository.model.Cost;
import org.springframework.data.repository.CrudRepository;

public interface CostRepository extends CrudRepository<Cost, String> {

    List<Cost> findAllByUserIdAndDateBetween(final String userId, final LocalDateTime startDate, final LocalDateTime endDate);

    List<Cost> findAllByUserId(final String userId);

    Cost findTopByUserIdOrderByDateDesc(final String userId);

    Cost findTopByUserIdOrderByDateAsc(final String userId);

}
