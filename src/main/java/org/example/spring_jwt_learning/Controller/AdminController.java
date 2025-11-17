package org.example.spring_jwt_learning.Controller;

import org.example.spring_jwt_learning.JoinDTO.UserJoinDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class AdminController { // 메니저전공 컨트롤러

    @GetMapping("/admin")
    public String adminp()
    {
        return "dmin Controller";
    }


}
