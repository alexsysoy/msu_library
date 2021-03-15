package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.RegisterCommand;
import alex.msu.gradwork.domain.Register;

import java.util.Set;

public interface RegisterService {

    Set<Register> getRegisters();

    Register findById(Long l);

    RegisterCommand findCommandById(Long l);

    RegisterCommand saveRegisterCommand(RegisterCommand command);

    void deleteById(Long idToDelete);

}
