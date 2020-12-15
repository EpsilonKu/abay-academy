package kz.bitter.project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "t_chapters")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chapters extends BaseEntity{
    @Column (name = "name")
    private String name;

    @Column (name = "description")
    private String description;

    @Column (name = "order_place")
    private Long orderPlace;

    @ManyToOne (fetch = FetchType.LAZY)
    private Courses course;
}
