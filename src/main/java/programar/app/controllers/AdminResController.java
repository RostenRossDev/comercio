package programar.app.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import programar.app.entities.Parameter;
import programar.app.repositories.ParameterRepository;

@RestController
@Log4j2
@RequestMapping("/api/admin")
public class AdminResController {

    @Autowired
    private ParameterRepository paramaterRepository;

    @GetMapping("/updateSocials")
    public ResponseEntity<?> updateTitle(@RequestParam(name = "youtube") String youtube,
                                         @RequestParam(name = "twitter") String twitter,
                                         @RequestParam(name = "instagram") String instagram,
                                         @RequestParam(name = "facebok") String facebok){

        Parameter youtubeParam = paramaterRepository.findByName("youtube");
        Parameter facebookParam = paramaterRepository.findByName("facebook");
        Parameter twitterParam = paramaterRepository.findByName("twitter");
        Parameter instagramParam = paramaterRepository.findByName("instagram");

        if(youtube != null && !youtube.isEmpty()) youtubeParam.setActualValue("https://".concat(youtube));
        if(twitter != null && !twitter.isEmpty()) facebookParam.setActualValue("https://".concat(twitter));
        if(instagram != null && !instagram.isEmpty()) twitterParam.setActualValue("https://".concat(instagram));
        if(facebok != null && !facebok.isEmpty())  instagramParam.setActualValue("https://".concat(facebok));


        if(youtube != null && !youtube.isEmpty()) paramaterRepository.save(youtubeParam);
        if(twitter != null && !twitter.isEmpty()) paramaterRepository.save(facebookParam);
        if(instagram != null && !instagram.isEmpty()) paramaterRepository.save(twitterParam);
        if(facebok != null && !facebok.isEmpty()) paramaterRepository.save(instagramParam);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/updateSiteName")
    public ResponseEntity<?> updateSiteName(@RequestParam(name = "title") String title, @RequestParam(name = "name") String name){
        log.info("Titulo: " + title);
        Parameter param = paramaterRepository.findByName(name);
        param.setActualValue(title);
        paramaterRepository.save(param);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/updateTitle")
    public ResponseEntity<?> updateTitle(@RequestParam(name = "title") String title, @RequestParam(name = "name") String name){
        log.info("Titulo: " + title);
        Parameter param = paramaterRepository.findByName(name);
        param.setActualValue(title);
        paramaterRepository.save(param);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/updateContactPhone")
    public ResponseEntity<?> updatePhone(@RequestParam(name = "title") String phone, @RequestParam(name = "name") String name){
        log.info("phone: " + phone);
        Parameter param = paramaterRepository.findByName(name);
        param.setActualValue(phone);
        paramaterRepository.save(param);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/updateContactEmail")
    public ResponseEntity<?> updateEmail(@RequestParam(name = "title") String email, @RequestParam(name = "name") String name){
        log.info("email: " + email);
        Parameter param = paramaterRepository.findByName(name);
        param.setActualValue(email);
        paramaterRepository.save(param);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/updateProductTitle")
    public ResponseEntity<?> updateProductTitle(@RequestParam(name = "title") String ofertTitle, @RequestParam(name = "name") String name){
        log.info("heroTitle: " + ofertTitle);
        Parameter param = paramaterRepository.findByName(name);
        param.setActualValue(ofertTitle);
        paramaterRepository.save(param);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/updateOfertTitle")
    public ResponseEntity<?> updateOfertTitle(@RequestParam(name = "title") String ofertTitle, @RequestParam(name = "name") String name){
        log.info("heroTitle: " + ofertTitle);
        Parameter param = paramaterRepository.findByName(name);
        param.setActualValue(ofertTitle);
        paramaterRepository.save(param);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/updateHeroText")
    public ResponseEntity<?> updateHeroText(@RequestParam(name = "title") String heroText, @RequestParam(name = "name") String name){
        log.info("heroTitle: " + heroText);
        Parameter param = paramaterRepository.findByName(name);
        param.setActualValue(heroText);
        paramaterRepository.save(param);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/updateHeroTitle")
    public ResponseEntity<?> updateSectionSaleTitle(@RequestParam(name = "title") String heroTitle, @RequestParam(name = "name") String name){
        log.info("heroTitle: " + heroTitle);
        log.info("name: " + name);
        Parameter param = paramaterRepository.findByName(name);
        param.setActualValue(heroTitle);
        paramaterRepository.save(param);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/updateProductoButton")
    public ResponseEntity<?> updateProductoButton(@RequestParam(name = "title") String saleTitle, @RequestParam(name = "name") String name){
        log.info("Button text: " + saleTitle);
        log.info("name: " + name);
        Parameter param = paramaterRepository.findByName(name);
        param.setActualValue(saleTitle);
        paramaterRepository.save(param);
        return ResponseEntity.ok("OK");
    }
}
