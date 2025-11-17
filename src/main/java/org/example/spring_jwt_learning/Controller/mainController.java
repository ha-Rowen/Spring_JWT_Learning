package org.example.spring_jwt_learning.Controller;

import org.apache.coyote.Response;
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
    public ResponseEntity<String> MainPage()
    {
        return new ResponseEntity<>(HttpStatus.OK); // 메인페이지 test용이다.
    }

    @PostMapping("/Join")
    public ResponseEntity<String> Join(@ModelAttribute UserJoinDTO userJoin)
    {

        int JoinCheck=joinService.UserJoin(userJoin);

        if(JoinCheck==1)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // retository에 결과를 바탕으로 저장 여부를 파악한뒤 그 결과코드를 프론트에게 전송한다.
    }

    @PostMapping("login")
    public ResponseEntity<String> Login(@ModelAttribute UserJoinDTO userJoin)
    {

        return new ResponseEntity<>(HttpStatus.OK);
        // 로그인 로직 만들어야한다.
    }


}
