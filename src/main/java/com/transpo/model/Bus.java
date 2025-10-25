package com.transpo.model;

import jakarta.persistence.*;

@Entity
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String route;
    private String regNumber;
    private String qrCodePath; // Optional: store the QR file path

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; // if you have user/owner relation

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }

    public String getRegNumber() { return regNumber; }
    public void setRegNumber(String regNumber) { this.regNumber = regNumber; }

    public String getQrCodePath() { return qrCodePath; }
    public void setQrCodePath(String qrCodePath) { this.qrCodePath = qrCodePath; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}
