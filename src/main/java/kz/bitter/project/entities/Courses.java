package kz.bitter.project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "t_courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Courses extends BaseEntity{

    @Column (name = "name")
    private String name;

    @Column (name = "description")
    private String description;

}
