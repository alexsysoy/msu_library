package alex.msu.gradwork.controllers;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.commands.RegisterCommand;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.services.NoteService;
import alex.msu.gradwork.services.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@Controller
public class NoteController {

    private final NoteService noteService;
    private final RegisterService registerService;

    public NoteController(NoteService noteService, RegisterService registerService) {
        this.noteService = noteService;
        this.registerService = registerService;
    }

    // Выводит список всех Дел данной Описи
    // URL - http://localhost:8080/register/{registerId}/notes
    //Направляем на сортировку и на постраничный просмотр
    @GetMapping
    @RequestMapping(value = "page/register/{registerId}/notes")
    public String viewNoteList(@PathVariable String registerId, Model model){
        //model.addAttribute("register", registerService.findById(Long.valueOf(registerId)));
        return "redirect:/registers/" + registerId + "/noteList/page/1?sort-field=id&sort-dir=asc";
    }



    // URL - http://localhost:8080/register/{registerId}/notes/page/1?sort-field=firstName&sort-dir=desc
    @GetMapping
    @RequestMapping(value = "/registers/{registerId}/noteList/page/{page-number}")
    public String findPaginated(@PathVariable(name = "registerId") String  registerId,
                                @PathVariable(name = "page-number") final int pageNo,
                                @RequestParam(name = "sort-field") final String sortField,
                                @RequestParam(name = "sort-dir") final String sortDir,
                                final Model model) {
        // Устанавливаем количество записей на странице
        final int pageSize = 10;
        final Page<Note> page = noteService.findPaginated(pageNo, pageSize, sortField, sortDir);
        final List<Note> noteList = page.getContent();

        // Параметры постраничного ввода
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        // Параметры сортировки
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        // Список дел
        model.addAttribute("noteList", noteList);
        // Номер Описи
        model.addAttribute("register", registerService.findById(Long.valueOf(registerId)));
        return "registers/noteList";
    }


//    @GetMapping
//    @RequestMapping("page/register/{registerId}/notes")
//    public String listNotes(@PathVariable String registerId, Model model){
//
//        model.addAttribute("register", registerService.findById(Long.valueOf(registerId)));
//
//        return "register/list";
//    }

    @GetMapping
    @RequestMapping("/register/{registerId}/note/{noteId}/show")
    public String showNote(@PathVariable String registerId,
                           @PathVariable String noteId, Model model){
        log.debug("Getting Note id: " + noteId + " from Register id: " + registerId);
        model.addAttribute("note", noteService.findByRegisterIdAndNoteId(Long.valueOf(registerId), Long.valueOf(noteId)));

        return "register/note/show";
    }

    @PostMapping("register/{registerId}/note")
    public String saveOrUpdate(@ModelAttribute NoteCommand command){

        NoteCommand savedCommand = noteService.saveNoteCommand(command);

        //return "index";
        return "redirect:/register/" + savedCommand.getRegisterId() + "/note/" + savedCommand.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("register/{registerId}/note/{noteId}/update")
    public String updateRecipeNote(@PathVariable String registerId,
                                   @PathVariable String noteId, Model model){
        model.addAttribute("note", noteService.findByRegisterIdAndNoteId(Long.valueOf(registerId), Long.valueOf(noteId)));
        return "register/note/noteform";
    }

    @GetMapping
    @RequestMapping("register/{registerId}/note/{id}/delete")
    public String deleteNote(@PathVariable String registerId,
                                   @PathVariable String id){

        log.debug("deleting note id:" + id);
        noteService.DeleteById(Long.valueOf(registerId),Long.valueOf(id));

        return "redirect:/register/" + registerId + "/notes";
    }

    @GetMapping
    @RequestMapping("register/{registerId}/note/new")
    public String newNote(@PathVariable String registerId, Model model){

        //make sure we have a good id value
        RegisterCommand registerCommand = registerService.findCommandById(Long.valueOf(registerId));
        //todo raise exception if null

        //need to return back parent id for hidden form property
        NoteCommand noteCommand = new NoteCommand();
        noteCommand.setRegisterId(Long.valueOf(registerId));
        model.addAttribute("note", noteCommand);

        return "register/note/noteform";
    }

    @GetMapping
    @RequestMapping("register/{registerId}/note/find")
    public String findNote(@PathVariable String registerId, Model model){

        //make sure we have a good id value
        RegisterCommand registerCommand = registerService.findCommandById(Long.valueOf(registerId));
        //todo raise exception if null

        //need to return back parent id for hidden form property
        NoteCommand noteCommand = new NoteCommand();
        noteCommand.setRegisterId(Long.valueOf(registerId));
        model.addAttribute("note", noteCommand);

        return "register/note/notefind";
    }

    @PostMapping("register/{registerId}/findnote")
    public String find(@ModelAttribute NoteCommand command,
                       Model model){


        Set<Note> notes = noteService.findNoteCommand(command);
        model.addAttribute("notes", notes);

        log.debug("Set findnotes size: " + notes.size());
        return "register/find";
    }

}
