package kz.bitter.project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "t_groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Groups extends BaseEntity {
    @Column (name = "name")
    String name;

    @Column (name = "description")
    String description;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Courses> courses;

}
