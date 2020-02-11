package com.example.study.controller.api;


import com.example.study.ifs.CrudInterface;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.UserApiResponse;
import com.example.study.model.network.response.UserOrderInfoApiResponse;
import com.example.study.service.UserApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserApiController implements CrudInterface<UserApiRequest, UserApiResponse> {

    @Autowired
    private UserApiLogicService userApiLogicService;

    @GetMapping("/{id}/orderInfo")
    public Header<UserOrderInfoApiResponse> orderInfo(@PathVariable Long id){
        return userApiLogicService.orderInfo(id);
    }



//paging을 위한

    @GetMapping("")
    public Header<List<UserApiResponse>> findAll(@PageableDefault(sort = "id",direction = Sort.Direction.DESC) Pageable pageable){
        System.out.println("UserApiController들어온다 ");

        log.info("{}",pageable);
        return userApiLogicService.search(pageable);
    }


    @Override
    @PostMapping("")//          /api/user
    public Header<UserApiResponse> create(@RequestBody  Header<UserApiRequest> request) {
        log.info("{}",request);
        System.out.println("들어왔다");
        return userApiLogicService.create(request);
    }

    @Override
    @GetMapping("{id}") //       /api/user/{id}
    public Header<UserApiResponse> read(@PathVariable(name = "id") Long id) {
        log.info("read : {}", id);
        return userApiLogicService.read(id);
    }

    @Override
    @PutMapping("")//          /api/user
    public Header<UserApiResponse> update(@RequestBody Header<UserApiRequest> request) {
        return userApiLogicService.update(request);
    }

    @Override
    @DeleteMapping("{id}")//        /api/user/{id}
    public Header delete(@PathVariable(name ="id") Long id) {

        return userApiLogicService.delete(id);
    }

    @Override
    public Header<List<UserApiResponse>> search(Pageable pageable) {
        return null;
    }


}
