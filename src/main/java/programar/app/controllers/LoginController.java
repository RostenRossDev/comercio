package programar.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import programar.app.entities.Parameter;
import programar.app.repositories.ParameterRepository;

@Controller
public class LoginController {

    @Autowired
    private ParameterRepository parameterRepository;

    @RequestMapping(value="/login", method= RequestMethod.GET)
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        Model model) {
        Parameter paramProductButton = parameterRepository.findByName("productButton");
        Parameter paramHeroTitle = parameterRepository.findByName("heroTitle");
        Parameter paramHeroText = parameterRepository.findByName("heroText");
        Parameter paramOfertSection = parameterRepository.findByName("ofertSection");
        Parameter paramProductSection = parameterRepository.findByName("productSection");
        Parameter paramPhone = parameterRepository.findByName("phone");
        Parameter paramEmail = parameterRepository.findByName("email");
        Parameter paramYoutube = parameterRepository.findByName("youtube");
        Parameter paramTwitter = parameterRepository.findByName("twitter");
        Parameter paramInstagram = parameterRepository.findByName("instagram");
        Parameter paramFacebook = parameterRepository.findByName("facebook");
        Parameter paramSiteName = parameterRepository.findByName("siteName");
        Parameter paramCalle = parameterRepository.findByName("calle");
        Parameter paramAltura = parameterRepository.findByName("altura");
        Parameter paramCiudad = parameterRepository.findByName("ciudad");
        Parameter paramProvincia = parameterRepository.findByName("provincia");
        Parameter paramPais = parameterRepository.findByName("pais");

        model.addAttribute("calle", paramCalle);
        model.addAttribute("altura", paramAltura);
        model.addAttribute("ciudad", paramCiudad);
        model.addAttribute("provincia", paramProvincia);
        model.addAttribute("pais", paramPais);
        model.addAttribute("productButton", paramProductButton);
        model.addAttribute("siteName", paramSiteName);
        model.addAttribute("facebook", paramFacebook);
        model.addAttribute("instagram", paramInstagram);
        model.addAttribute("twitter", paramTwitter);
        model.addAttribute("youtube", paramYoutube);
        model.addAttribute("email", paramEmail);
        model.addAttribute("phone", paramPhone);
        model.addAttribute("productSection", paramProductSection);
        model.addAttribute("ofertSection", paramOfertSection);
        model.addAttribute("heroText", paramHeroText);
        model.addAttribute("heroTitle", paramHeroTitle);
        if (logout != null){
            model.addAttribute("logout", "Sessión cerrada con exito");
        }

        if(error != null){
            model.addAttribute("error", "Nombre de usuario o contraseña incorrectos");
        }

        return "login";
    }
}
