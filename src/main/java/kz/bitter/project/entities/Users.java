package kz.bitter.project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table (name = "t_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users extends BaseEntity{

    @Column (name = "email")
    private String email;

    @Column (name = "username")
    private String username;

    @Column (name = "password")
    private String password;

    @Column (name = "fullname")
    private String name;

    @Column (name  = "pfp")
    private String pfp;

    @ManyToMany (fetch = FetchType.LAZY)
    private List <Groups> groups;

    @ManyToMany (fetch = FetchType.LAZY)
    private List<Courses> courses;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Roles> roles = new ArrayList<>();
}
