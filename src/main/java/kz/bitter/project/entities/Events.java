package kz.bitter.project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "t_events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Events extends BaseEntity {

    @ManyToOne (fetch = FetchType.LAZY)
    Courses courses;

    @ManyToMany (fetch = FetchType.LAZY)
    List<Groups> groups;

}
