package Spechofy.Musique.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class IndexController {

    @GetMapping("/")
    public String home() {
        return "Tester /spotify/ pour avoir accès aux commandes de spotify, /deezer/ pour avoir accès aux commandes de deezer ou /apple/ pour avoir accès aux commandes de apple music après l'URL";
    }

}
