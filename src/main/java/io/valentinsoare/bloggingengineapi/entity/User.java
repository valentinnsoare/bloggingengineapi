package io.valentinsoare.bloggingengineapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
@Table(name = "user", schema = "news_outlet_db")
public class User implements Comparable<User> {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank(message = "Username is mandatory and should be unique.")
    private String username;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Email is mandatory and should be unique.")
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is mandatory")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
        role.addUser(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.removeUser(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;
        return Objects.equals(user.username, username);
    }

    @Override
    public int hashCode() {
        int hash = 17;

        hash = 31 * hash + (username == null ? 0 : username.hashCode());
        return hash;
    }

    @Override
    public int compareTo(User o) {
        if (o == null) return -2;
        return o.getUsername().compareTo(this.getUsername());
    }

    @Override
    public String toString() {
        return "User [" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ']';
    }
}
