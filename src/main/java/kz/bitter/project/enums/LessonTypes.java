package kz.bitter.project.enums;

public enum LessonTypes {
    LECTURE ("Lecture"),
    PRACTISE ("Practise"),
    HOMEWORK ("Homework"),
    LABORATORY_WORK ("Labaratory work");
    private String type;

    private LessonTypes (String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
