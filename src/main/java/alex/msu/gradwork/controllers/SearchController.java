package alex.msu.gradwork.controllers;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.services.NoteService;
import alex.msu.gradwork.services.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@Controller
public class SearchController {

    private final NoteService noteService;
    private final RegisterService registerService;

    public SearchController(NoteService noteService, RegisterService registerService) {
        this.noteService = noteService;
        this.registerService = registerService;
    }


    // Расширенный поиск единиц хранения
    @GetMapping
    @RequestMapping("/reports/{registerId}/searchBox")
    public String viewSearchBox(@PathVariable String registerId, Model model) {

        NoteCommand noteCommand = new NoteCommand();
        noteCommand.setRegisterId(Long.valueOf(registerId));

        model.addAttribute("note", noteCommand);

        return "reports/searchBox";
    }

    // Обработка поиска, вывод результата поиска
    @PostMapping("/reports/{registerId}/searchResult")
    public String find(@ModelAttribute NoteCommand command,
                       @PathVariable String registerId,
                       Model model){

        Set<Note> notes = noteService.searchNotes(command);
        model.addAttribute("notes", notes);
        model.addAttribute("register", registerService.findById(Long.valueOf(registerId)));

        return "reports/searchResult";
    }
}
