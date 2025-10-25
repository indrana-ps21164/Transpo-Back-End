package com.transpo.controller;

import com.transpo.repository.TicketRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner")
@CrossOrigin(origins = "*")
public class OwnerController {

    private final TicketRepository ticketRepo;

    public OwnerController(TicketRepository ticketRepo) { this.ticketRepo = ticketRepo; }

    @GetMapping("/{ownerId}/income")
    public ResponseEntity<?> income(@PathVariable Long ownerId) {
        Double total = ticketRepo.sumFareByOwnerId(ownerId);
        return ResponseEntity.ok(total == null ? 0.0 : total);
    }
}
