package alex.msu.gradwork.services;

import alex.msu.gradwork.domain.Actor;
import alex.msu.gradwork.domain.DupActor;
import alex.msu.gradwork.domain.DupSubject;
import alex.msu.gradwork.domain.Subject;
import alex.msu.gradwork.repositories.ActorRepository;
import alex.msu.gradwork.repositories.DupActorRepository;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DupActorService {

    private final DupActorRepository dupActorRepository;
    private final ActorRepository actorRepository;

    public DupActorService(DupActorRepository dupActorRepository, ActorRepository actorRepository) {
        this.dupActorRepository = dupActorRepository;
        this.actorRepository = actorRepository;
    }

    @Transactional
    @Synchronized
    public DupActor createDup(Long actorId) {

        Actor actor = actorRepository.findById(actorId).get();
        DupActor dupActor = new DupActor();
        dupActor.setName(actor.getName());
        dupActor.setPatronymic(actor.getPatronymic());
        dupActor.setSurname(actor.getSurname());
        dupActor.setMemo(actor.getMemo());
        dupActor.setActor(actor);
        actor.setDupActor(dupActor);
        actorRepository.save(actor);

        return dupActorRepository.save(dupActor);
    }

    public void deleteById(Long dupId, Long actId) {
        log.debug("Удаляем дубль указателя id {} и id указателя {}", dupId, actId);
        Actor actor = actorRepository.findById(actId).get();
        actor.setDupActor(null);

        dupActorRepository.deleteById(dupId);
    }

    public DupActor findById(Long dupActorId) {
        return dupActorRepository.findById(dupActorId).get();
    }

    @Transactional
    @Synchronized
    public DupActor save(DupActor dupActor, Long actorId) {

        DupActor savedDup = dupActorRepository.findById(dupActor.getId()).get();
        savedDup.setName(dupActor.getName());
        savedDup.setPatronymic(dupActor.getPatronymic());
        savedDup.setSurname(dupActor.getSurname());
        savedDup.setMemo(dupActor.getMemo());

        return savedDup;
    }
}

