package ua.unicyb.webchat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "blacklist")
@NoArgsConstructor
@Setter
@Getter
public class Ban {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User user;
}
