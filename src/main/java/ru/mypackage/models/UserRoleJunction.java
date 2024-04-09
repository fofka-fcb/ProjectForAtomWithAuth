package ru.mypackage.models;

import jakarta.persistence.*;

@Entity
@IdClass(UserRoleJunctionId.class)
@Table(name = "user_role_junction")
public class UserRoleJunction {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_user")
    private ApplicationUser user;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;

    public UserRoleJunction() {
    }

    public UserRoleJunction(ApplicationUser user, Role role) {
        this.user = user;
        this.role = role;
    }

    public Integer getUser() {
        return user.getId();
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Integer getRole() {
        return role.getId();
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
