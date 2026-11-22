package ru.bsuedu.cad.lab3.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "login_history")
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime loginDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Длина 45 символов хватает для IPv6-адреса. */
    @Column(nullable = false, length = 45)
    private String ipAddress;

    protected LoginHistory() {
    }

    public LoginHistory(User user, String ipAddress) {
        this.user = user;
        this.ipAddress = ipAddress;
        this.loginDate = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public LocalDateTime getLoginDate() { return loginDate; }
    public User getUser() { return user; }
    public String getIpAddress() { return ipAddress; }
}
