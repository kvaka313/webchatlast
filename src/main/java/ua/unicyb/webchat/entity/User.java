package ua.unicyb.webchat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_users")
@Setter
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name="login", nullable = false, unique = true)
    private String login;

    @OneToOne(mappedBy = "user")
    private Ban ban;
}
