package org.example.spring_jwt_learning.Controller;

import org.example.spring_jwt_learning.JoinDTO.UserJoinDTO;
import org.example.spring_jwt_learning.Service.JoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class mainController {

    private JoinService joinService;
    public mainController(JoinService joinService) {
        this.joinService = joinService;
    }

    @GetMapping("/")
    public String mainp()
    {
        return "main Controller";
    }

    @PostMapping("/Join")
    public ResponseEntity<String> Join(@ModelAttribute UserJoinDTO userJoin)
    {

        int JoinCheck=joinService.UserJoin(userJoin);

        if(JoinCheck==1)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // 현재는 그냥 성공이라고 전공한다. Repository에서 add 기능을 수행하고 결과를 바탕으로 성공여부를 출력해야 한다.
    }
}
