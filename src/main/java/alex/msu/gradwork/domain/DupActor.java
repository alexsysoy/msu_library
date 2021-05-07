package alex.msu.gradwork.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(exclude = {"actor"})
@Table(name = "dubActors")
public class DupActor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String patronymic;
    private String surname;
    private String memo;

    @OneToOne(fetch = FetchType.LAZY)
    private Actor actor;

}

