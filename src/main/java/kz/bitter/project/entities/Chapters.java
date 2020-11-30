package kz.bitter.project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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

    @Column (name = "course_id")
    private Long id;

//    @Column (name = "order_place")
//    private
}
