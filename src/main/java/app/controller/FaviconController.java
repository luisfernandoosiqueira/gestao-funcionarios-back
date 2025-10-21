package app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaviconController {

    @GetMapping("favicon.ico")
    public void favicon() {
        // Retorna 200 OK sem corpo — evita erro "NoResourceFoundException"
    }
}
