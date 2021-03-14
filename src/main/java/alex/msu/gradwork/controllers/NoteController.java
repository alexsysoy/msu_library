package alex.msu.gradwork.controllers;

import alex.msu.gradwork.services.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/note/{id}/show")
    public String showById(@PathVariable String id, Model model){

        model.addAttribute("note", noteService.findById(Long.valueOf(id)));
        System.out.println("NoteController show");
        return "note/show";

    }


}
