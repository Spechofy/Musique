package Spechofy.Musique.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class IndexController {

    @GetMapping("/")
    public String home() {
        return "Tester /spotify, /deezer ou /apple après l'URL";
    }

}
