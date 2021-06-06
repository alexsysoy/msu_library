package alex.msu.library.controllers;

import alex.msu.library.domain.Role;
import alex.msu.library.domain.User;
import alex.msu.library.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;

    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ADMIN')")
    @GetMapping("/registration")
    public String registration(){
        return "auth/registration";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ADMIN')")
    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){

        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "Данный пользователь уже существует");
            return registration();
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);

        return registration();
    }
}
