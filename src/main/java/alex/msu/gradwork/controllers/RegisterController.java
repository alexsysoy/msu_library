package alex.msu.gradwork.controllers;

import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.services.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    // URL - http://localhost:8080/
    //Направляем на сортировку и на постраничный просмотр
    @GetMapping(value = "/")
    public String viewIndexPage() {
        return "redirect:page/1?sort-field=id&sort-dir=asc";
    }

    // URL - http://localhost:8080/page/1?sort-field=firstName&sort-dir=desc
    @GetMapping(value = "/page/{page-number}")
    public String findPaginated(@PathVariable(name = "page-number") final int pageNo,
                                @RequestParam(name = "sort-field") final String sortField,
                                @RequestParam(name = "sort-dir") final String sortDir,
                                final Model model) {
        // Устанавливаем количество записей на странице
        final int pageSize = 15;
        final Page<Register> page = registerService.findPaginated(pageNo, pageSize, sortField, sortDir);
        final List<Register> registerList = page.getContent();

        // Параметры постраничного ввода
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        // Параметры сортировки
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        // список Описей
        model.addAttribute("registerList", registerList);
        return "main";
    }

}
