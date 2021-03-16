package alex.msu.gradwork.controllers;

import alex.msu.gradwork.services.NoteService;
import alex.msu.gradwork.services.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class NoteController {

    private final NoteService noteService;
    private final RegisterService registerService;

    public NoteController(NoteService noteService, RegisterService registerService) {
        this.noteService = noteService;
        this.registerService = registerService;
    }

    @GetMapping
    @RequestMapping("/register/{registerId}/list")
    public String listNotes(@PathVariable String registerId, Model model){
        log.debug("Getting Note list for Register id: " + registerId + " number of notes: " + registerService.findById(Long.valueOf(registerId)).getNotes().size());

        model.addAttribute("register", registerService.findById(Long.valueOf(registerId)));

        return "register/list";
    }



    @GetMapping
    @RequestMapping("/register/{registerId}/note/{noteId}/show")
    public String showNote(@PathVariable String registerId,
                           @PathVariable String noteId, Model model){
        log.debug("Getting Note id: " + noteId + " from Register id: " + registerId);
        model.addAttribute("note", noteService.findByRegisterIdAndNoteId(Long.valueOf(registerId), Long.valueOf(noteId)));

        return "register/note/show";
    }


//    @GetMapping("register/{RegisterId}/show")
//    public String showById(@PathVariable String id, Model model){
//
//        model.addAttribute("note", noteService.findById(Long.valueOf(id)));
//        System.out.println("NoteController show");
//        return "register/note/show";
//
//    }

//    @GetMapping("register/{RegisterId}/show")
//    public String showById(@PathVariable String id, Model model){
//
//        model.addAttribute("note", noteService.findById(Long.valueOf(id)));
//        System.out.println("NoteController show");
//        return "register/note/show";
//    }

//    @GetMapping("register/note/list")
//    public String showCRUD(Model model) {
//
//        return "showCRUD";
//    }


}
