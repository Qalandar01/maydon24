package uz.ems.maydon24.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.ems.maydon24.models.base.BaseEntity;

import java.util.Collection;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
@SQLRestriction("visibility=true")
public class User extends BaseEntity implements UserDetails {

    private String tgUsername;

    private String fullName;

    private String phone;

    private Integer messageId;

    private Integer verifyCode;

    private boolean visibility = true;

    @ManyToMany
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
        return this.phone;
    }
}
