package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.RegisterCommand;
import alex.msu.gradwork.domain.Register;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface RegisterService {

//    Page<Register> listAll();


    Long getTotalRegisters();

    Page<Register> findPaginated(final int pageNumber, final int pageSize,
                                 final String sortField, final String sortDirection);

    Set<Register> getRegisters();

    Register findById(Long l);

    RegisterCommand findCommandById(Long l);

    RegisterCommand saveRegisterCommand(RegisterCommand command);

    void deleteById(Long idToDelete);

}
