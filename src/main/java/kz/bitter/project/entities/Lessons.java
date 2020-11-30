package kz.bitter.project.entities;

import kz.bitter.project.entities.BaseEntity;
import kz.bitter.project.enums.LessonTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table (name = "t_lessons")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lessons extends BaseEntity {

    @Column (name = "name")
    private String name;

    @Column (name = "content", columnDefinition = "TEXT")
    private String content;

    @Column (name = "lesson_type")
    @Enumerated (EnumType.ORDINAL)
    private LessonTypes lessonType;

    @Column (name = "chapter_id")
    private Long chapter_id;



}
