package fr.epita.assistants.jws.data.model;

import fr.epita.assistants.jws.utils.State;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "game")
public class GameModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToMany(targetEntity = PlayerModel.class,mappedBy = "game")
    public List<PlayerModel> Players;
    public String time;

    @Column(name = "map")
    @ElementCollection(fetch = FetchType.EAGER)
    public List<String> map;
    public State state;

}
