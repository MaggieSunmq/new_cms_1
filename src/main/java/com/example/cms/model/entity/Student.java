package com.example.cms.model.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "students")
public class Student extends Person {

    @OneToMany(mappedBy = "student")
    @Nullable
    private List<CourseMark> marks  = new ArrayList<>();

    @ManyToOne
    @Nullable
    @JoinColumn(name = "deptId")
    private Department department;

}
