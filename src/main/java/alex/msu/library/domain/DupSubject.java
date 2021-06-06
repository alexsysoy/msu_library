package alex.msu.library.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(exclude = {"subject"})
@Table(name = "dubSubjects")
public class DupSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String memo;

    @OneToOne(fetch = FetchType.LAZY)
    private Subject subject;

}
