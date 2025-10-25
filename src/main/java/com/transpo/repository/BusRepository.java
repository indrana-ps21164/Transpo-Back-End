package com.transpo.repository;

import com.transpo.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findByOwnerId(Long ownerId);
}
