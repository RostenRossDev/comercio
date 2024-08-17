package programar.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @RequestMapping(value="/login", method= RequestMethod.GET)
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        Model model) {

        if (logout != null){
            model.addAttribute("logout", "Sessión cerrada con exito");
        }

        if(error != null){
            model.addAttribute("error", "Nombre de usuario o contraseña incorrectos");
        }

        return "login";
    }
}
