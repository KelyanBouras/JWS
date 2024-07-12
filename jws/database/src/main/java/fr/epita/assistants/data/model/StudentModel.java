package fr.epita.assistants.data.model;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.yaml.snakeyaml.events.Event;

import javax.persistence.*;
import java.util.List;

@Entity @Table(name = "student_model")
public class StudentModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String name;
    @ManyToOne @JoinColumn(name = "course_id")
    public CourseModel course;

}
