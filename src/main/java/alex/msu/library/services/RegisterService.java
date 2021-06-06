package alex.msu.library.services;

import alex.msu.library.commands.RegisterCommand;
import alex.msu.library.domain.Register;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface RegisterService {


    Long getTotalRegisters();

    Page<Register> findPaginated(final int pageNumber, final int pageSize,
                                 final String sortField, final String sortDirection);

    Set<Register> getRegisters();

    Register findById(Long l);

    RegisterCommand findCommandById(Long l);

    RegisterCommand saveRegisterCommand(RegisterCommand command);

    void deleteById(Long idToDelete);

}
