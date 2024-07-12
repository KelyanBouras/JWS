package fr.epita.assistants.data.model;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;

@Entity @Table(name = "course_model")
public class CourseModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") public long id;
    @Column(name = "name") public String name;
    @OneToMany(targetEntity = StudentModel.class,mappedBy = "course")
    public List<Long> student;
    @ElementCollection @CollectionTable(name = "course_model_tags",joinColumns = @JoinColumn(name = "course_id"))
    public List<String> tag;


}
