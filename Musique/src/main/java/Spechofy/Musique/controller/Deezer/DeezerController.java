package Spechofy.Musique.controller.Deezer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/deezer")
public class DeezerController {

    @GetMapping("/")
    public String home() {
        return "🚧 Partie du site en travaux, veuillez patienter s'il vous plaît, je vous remercie fortement 😉";
    }

}
