package uz.ems.maydon24.models.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.ems.maydon24.models.base.BaseEntity;
import uz.ems.maydon24.models.enums.Roles;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
@SQLRestriction("visibility=true")
public class User extends BaseEntity implements UserDetails {

    private Long telegramId;

    private String telegramUsername;
    private String username;
    private String password;

    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    private Integer messageId;

    private Integer verifyCode;

    private LocalDateTime verifyCodeExpiration;

    @Builder.Default
    @Column(nullable = false)
    private boolean visibility = true;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return this.visibility;
    }
}
