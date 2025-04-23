package Spechofy.Musique.controller.Apple;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/apple")
public class AppleController {

     @GetMapping("/")
    public String home() {
        return "🚧 Partie du site en travaux, veuillez patienter s'il vous plaît, je vous remercie fortement 😉";
    }

}
