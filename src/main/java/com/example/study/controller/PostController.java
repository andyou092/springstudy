package com.example.study.controller;

import com.example.study.model.SearchParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class PostController {
    //POST는 HTML <Form>
    //      ajax 검색
    // json, xml multipart-form / text-plain
    //RequestBody 는 통신시 Body에 데이터를 담아서 보내겠습니다.
    //@RequestMapping(Request.POST, path="/postMathod") ex> @PostMapping("/postMethod", produces ={"application-json"})
    @PostMapping("/postMethod")
    public SearchParam postMethod(@RequestBody SearchParam searchParam){

        return searchParam;
    }

    @PutMapping
    public void put(){

    }

    @PatchMapping
    public void pat(){

    }

}
