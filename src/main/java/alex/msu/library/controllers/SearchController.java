package alex.msu.library.controllers;

import alex.msu.library.commands.NoteCommand;
import alex.msu.library.domain.Note;
import alex.msu.library.services.NoteService;
import alex.msu.library.services.RegisterService;
import alex.msu.library.services.SearchService;
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
    private final SearchService searchService;

    public SearchController(NoteService noteService, RegisterService registerService, SearchService searchService) {
        this.noteService = noteService;
        this.registerService = registerService;
        this.searchService = searchService;
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


    // Обработка поиска везде, вывод результата поиска
    @PostMapping("/reports/searchResult")
    public String quickSearch(@RequestParam String wordToSearch,
                       Model model){

        Set<Note> notes = searchService.searchNotes(wordToSearch.toLowerCase().strip());
        model.addAttribute("notes", notes);

        return "reports/searchResult";
    }
}
