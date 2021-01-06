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

    @Column (name = "html_content", columnDefinition = "TEXT")
    private String htmlContent;

    @Column (name = "lesson_type")
    @Enumerated (EnumType.ORDINAL)
    private LessonTypes lessonType;

    @Column (name = "order_place")
    private Long orderPlace;

    @ManyToOne (fetch = FetchType.LAZY )
    private Chapters chapter;


}
