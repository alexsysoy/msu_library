package alex.msu.gradwork.controllers;

import alex.msu.gradwork.services.NoteService;
import alex.msu.gradwork.services.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService, NoteService noteService) {
        this.registerService = registerService;
    }

    @RequestMapping({"","/","/index"})
    public String getRegisterIndexPage(Model model){
        log.debug("Getting Register Index page!");
        model.addAttribute("registers", registerService.getRegisters());
        return "index";
    }


}
