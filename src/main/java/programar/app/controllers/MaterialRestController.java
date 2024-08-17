package programar.app.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import programar.app.entities.Material;
import programar.app.services.MaterialService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/material")
public class MaterialRestController {

    @Autowired
    private MaterialService materialService;


    @GetMapping
    public ResponseEntity<List<Material>> findAll(){
        List<Material> materals = materialService.findAll();
        return ResponseEntity.ok(materals);
    }
}
