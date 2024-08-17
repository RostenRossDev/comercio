package programar.app.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/api/admin")
public class AdminResController {

    @GetMapping("/updateTitle")
    public ResponseEntity<?> updateTitle(@RequestParam(name = "title") String title){
        log.info("Titulo: " + title);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/updateContactPhone")
    public ResponseEntity<?> updatePhone(@RequestParam(name = "title") String phone){
        log.info("phone: " + phone);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/updateContactEmail")
    public ResponseEntity<?> updateEmail(@RequestParam(name = "title") String email){
        log.info("email: " + email);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/updateHeroTitle")
    public ResponseEntity<?> updateHeroTitle(@RequestParam(name = "title") String heroTitle){
        log.info("heroTitle: " + heroTitle);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/updateSectionSaleTitle")
    public ResponseEntity<?> updateSectionSaleTitl(@RequestParam(name = "title") String saleTitle){
        log.info("heroTitle: " + saleTitle);
        return ResponseEntity.ok("OK");
    }
}
