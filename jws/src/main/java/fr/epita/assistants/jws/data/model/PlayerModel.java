package fr.epita.assistants.jws.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "player")
public class PlayerModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String lastbomb;
    public String lastmovement;
    public int lives;
    public String name;
    public int posx;
    public int posy;
    public int position;

    @ManyToOne @JoinColumn(name = "game_id")
    public GameModel game;
}
