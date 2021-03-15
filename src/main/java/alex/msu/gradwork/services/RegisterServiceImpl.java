package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.RegisterCommand;
import alex.msu.gradwork.converters.RegisterCommandToRegister;
import alex.msu.gradwork.converters.RegisterToRegisterCommand;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.repositories.RegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {

    private final RegisterRepository registerRepository;
    private final RegisterCommandToRegister registerCommandToRegister;
    private final RegisterToRegisterCommand registerToRegisterCommand;

    public RegisterServiceImpl(RegisterRepository registerRepository, RegisterCommandToRegister registerCommandToRegister, RegisterToRegisterCommand registerToRegisterCommand) {
        this.registerRepository = registerRepository;
        this.registerCommandToRegister = registerCommandToRegister;
        this.registerToRegisterCommand = registerToRegisterCommand;
    }


    @Override
    public Set<Register> getRegisters() {
        log.debug("I'm in the RegisterServiceImp");

        Set<Register> registerSet = new HashSet<>();
        registerRepository.findAll().iterator().forEachRemaining(registerSet::add);
        return registerSet;
    }

    @Override
    public Register findById(Long l) {

        Optional<Register> registerOptional = registerRepository.findById(l);

        if (registerOptional.isEmpty()){
            throw  new RuntimeException("Register not found");
        }

        return registerOptional.get();
    }

    @Override
    @Transactional
    public RegisterCommand findCommandById(Long l) {
        return registerToRegisterCommand.convert(findById(l));
    }

    @Override
    @Transactional
    public RegisterCommand saveRegisterCommand(RegisterCommand command) {
        Register detachedRegister =registerCommandToRegister.convert(command);

        Register savedRegister = registerRepository.save(detachedRegister);
        log.debug("Save RegisterId:" + savedRegister.getId());

        return registerToRegisterCommand.convert(savedRegister);
    }

    @Override
    public void deleteById(Long idToDelete) {
        registerRepository.deleteById(idToDelete);
    }
}
