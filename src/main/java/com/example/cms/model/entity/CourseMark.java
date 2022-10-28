package com.example.cms.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "marks")
public class CourseMark {

    @EmbeddedId
    CourseMarkKey markId;

    @ManyToOne
    @MapsId("studetnId")
    @JoinColumn(name = "studentId")
    @JsonIgnoreProperties({"marks"})
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "courseCode")
    @JsonIgnoreProperties({"marks"})
    private Course course;

    @NotEmpty
    private int mark;

}

@Embeddable
@Getter
@Setter
@NoArgsConstructor
class CourseMarkKey implements Serializable {

    @Column(name = "studentId")
    Long studentId;

    @Column(name = "courseId")
    String courseId;

    @Override
    public int hashCode(){
        String concatString = String.valueOf(studentId.hashCode()) + String.valueOf(courseId.hashCode());
        return concatString.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if (o == this)
            return true;
        if (!(o instanceof CourseMarkKey))
            return false;
        CourseMarkKey other = (CourseMarkKey) o;
        return studentId.equals(other.studentId) && courseId.equals(other.courseId);
    }

}