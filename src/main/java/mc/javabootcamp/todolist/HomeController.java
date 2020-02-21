package mc.javabootcamp.todolist;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    ToDoListRepository toDoListRepository;
    @Autowired
    CloudinaryConfig cloudc;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("alltodolist", toDoListRepository.findAll());
        return "list";
    }

    @GetMapping("/form")
    public String listForm(Model model) {
        model.addAttribute("todo", new ToDoList());
        return "form";
    }

    @PostMapping("/form")
    public String processImage(@ModelAttribute ToDoList todolist,
                               @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "redirect:/list";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
            todolist.setPicture(uploadResult.get("url").toString());
            toDoListRepository.save(todolist);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/list";
        }
        return "redirect:/";
    }


    @GetMapping("/about")
    public String about() {

        return "about";
    }
}
