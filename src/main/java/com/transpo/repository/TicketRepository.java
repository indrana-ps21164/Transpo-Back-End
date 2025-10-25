package com.transpo.repository;

import com.transpo.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT COALESCE(SUM(t.fare), 0) FROM Ticket t WHERE t.bus.owner.id = :ownerId")
    Double sumFareByOwnerId(Long ownerId);
}
