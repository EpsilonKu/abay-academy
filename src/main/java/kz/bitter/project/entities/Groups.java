package kz.bitter.project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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


}
