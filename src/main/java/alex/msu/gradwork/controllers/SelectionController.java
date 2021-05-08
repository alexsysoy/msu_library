package alex.msu.gradwork.controllers;

import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.User;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.UserRepository;
import alex.msu.gradwork.services.UserService;
import alex.msu.gradwork.tools.exelFileUploadTool.service.ExelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@Slf4j
public class SelectionController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final NoteRepository noteRepository;
    private final ExelService exelService;

    public SelectionController(UserRepository userRepository, UserService userService, NoteRepository noteRepository, ExelService exelService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.noteRepository = noteRepository;
        this.exelService = exelService;
    }


    @GetMapping
    @RequestMapping("/selections/showSelection")
    public String create(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        model.addAttribute("notes", user.getNotes());
        model.addAttribute("user", user);

        return "selections/showSelection";
    }

    @GetMapping
    @RequestMapping("/selections/removeSelection")
    public String remove(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        user.setNotes(null);
        userRepository.save(user);

        return "selections/showSelection";
    }

    // На файл
    @GetMapping
    @RequestMapping("/selections/toFileSelection")
    public String printToFile(HttpServletRequest request, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        exelService.saveSelectionFile(user.getNotes());

        return getPreviousPageByRequest(request).orElse("/");
    }

    @GetMapping
    @RequestMapping("/selections/{UnitId}/addSelection")
    public String add(HttpServletRequest request, Model model, @PathVariable Long UnitId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());
        Note note = noteRepository.findById(UnitId).get();

        user.getNotes().add(note);
        userRepository.save(user);
        log.debug("Добавлено в выборку дело id {} из описи id {}", note.getId(), note.getRegister().getId());

        return getPreviousPageByRequest(request).orElse("/");
    }

    protected Optional<String> getPreviousPageByRequest(HttpServletRequest request)
    {
        return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
    }

}
