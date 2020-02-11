package com.example.study.controller;


import com.example.study.model.SearchParam;
import com.example.study.model.network.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")// http://localhost:8080/api
public class GetController {

  //  @Autowired
  //  UserRepositorys userRepository;

    @RequestMapping(method = RequestMethod.GET, path = "/getMethod")//http://localhost:8080/api/getMethod
    public String getRequest(){
        return "Hi getMethod";
    }

    @GetMapping("/getParameter")// http://localhost:8080/api/getParameter?id=1234&password=abcd
    public String getParameter(@RequestParam String id, @RequestParam String password){
        System.out.println("id : "+ id);
        System.out.println("password : " + password);
        return id + password;
    }

    @GetMapping("/getParameter1")// http://localhost:8080/api/getParameter?id=1234&password=abcd
    public String getParameter1(@RequestParam String id, @RequestParam(name = "password") String qwd ){
        String password ="bbbb";
        System.out.println("id : "+ id);
        System.out.println("password : " + password);
        return id + password;
    }

    // http://localhost:8080/api/getMultiParameter?accout=aaaa&email=ffff&page
    @GetMapping("/getMultiParameter")
    public SearchParam getMultiParameter(SearchParam searchParam){
        System.out.println("들어온다 getMultiparameter");
    //    System.out.println(searchParam.getAccount());
    //    System.out.println(searchParam.getEmail());
    //    System.out.println(searchParam.getPage());
    //네트워크 통신을 할때에는 json형식으로 통신을 한다 {"account : "", "email" : "", "page" : 0}
        return searchParam;
    }
    @GetMapping("/header")
    public Header getHeader(){
        // {"resultCode : OK" , "description : OK"}
        return Header.builder().resultCode("OK").description("OK").build();
    }


}
