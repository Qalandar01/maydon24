package uz.ems.maydon24.models.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.ems.maydon24.models.base.BaseEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@SQLRestriction("visibility=true")
public class User extends BaseEntity implements UserDetails {

    @Column(nullable = false)
    private Long telegramId;

    private String telegramUsername;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phoneNumber;

    private Integer messageId;

    private Integer verifyCode;

    private LocalDateTime verifyCodeExpiration;

    @Builder.Default
    @Column(nullable = false)
    private boolean visibility = true;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public boolean isEnabled() {
        return this.visibility;
    }

    @Override
    public String getPassword() {
        return "[PROTECTED]";
    }

    @Override
    public String getUsername(){
        return this.phoneNumber;
    }
}
