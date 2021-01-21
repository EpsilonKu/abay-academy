package kz.bitter.project.entities;

import kz.bitter.project.enums.Gender;
import kz.bitter.project.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.Date;
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

    @Column (name = "role")
    @Enumerated (EnumType.ORDINAL)
    private Roles role;

    @Column (name  = "avatar")
    private String avatar;

    @ManyToMany (fetch = FetchType.LAZY)
    List <Groups> groups;
}
