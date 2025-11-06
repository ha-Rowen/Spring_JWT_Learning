package org.example.spring_jwt_learning;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class mainController {

    @GetMapping("/")
    public String mainp()
    {
        return "main Controller";
    }
}
