package alex.msu.gradwork.services;

import alex.msu.gradwork.domain.DupSubject;
import alex.msu.gradwork.domain.Subject;
import alex.msu.gradwork.repositories.DupSubjectRepository;
import alex.msu.gradwork.repositories.SubjectRepository;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class DupSubjectService {

    private final DupSubjectRepository dupSubjectRepository;
    private final SubjectRepository subjectRepository;

    public DupSubjectService(DupSubjectRepository dupSubjectRepository, SubjectRepository subjectRepository) {
        this.dupSubjectRepository = dupSubjectRepository;
        this.subjectRepository = subjectRepository;
    }

    @Transactional
    @Synchronized
    public DupSubject createDup(Long subjectId) {

        Subject subject = subjectRepository.findById(subjectId).get();
        DupSubject dupSubject = new DupSubject();
        dupSubject.setName(subject.getName());
        dupSubject.setMemo(subject.getMemo());
        dupSubject.setSubject(subject);
        subject.setDupSubject(dupSubject);
        subjectRepository.save(subject);

        return dupSubjectRepository.save(dupSubject);
    }


    public DupSubject findById(Long subjectId) {
        return dupSubjectRepository.findById(subjectId).get();
    }

    @Transactional
    @Synchronized
    public DupSubject save(DupSubject dupSubject, Long subjectId) {

        DupSubject savedDup = dupSubjectRepository.findById(dupSubject.getId()).get();
        savedDup.setMemo(dupSubject.getMemo());
        savedDup.setName(dupSubject.getName());

        return savedDup;
    }


    public void deleteById(Long dupId, Long subId) {
        log.debug("Удаляем дубль указателя id {} и id указателя {}", dupId, subId);
        Subject subject = subjectRepository.findById(subId).get();
        subject.setDupSubject(null);

        dupSubjectRepository.deleteById(dupId);
    }
}
