package com.example.study.service;


import com.example.study.ifs.CrudInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component//상속받는 객체에서 AutoWired를 받기 위해서
public abstract class BaseService<Req,Res,Entity> implements CrudInterface<Req,Res> {
    //레파지로리를 받는 클래스'
    @Autowired(required = false)//false는 있을수도 없을수도 있고를 의미한다. 즉 필수는 아니라는것이다.
    protected JpaRepository<Entity,Long> baseRepository;


    //JpaRepository<Item,Long>
    /*public Header<List<UserApiResponse>> search(Pageable pageable) {

        Page<User> users = userRepository.findAll(pageable);

        List<UserApiResponse> userApiResponsesList = users.stream()
                .map(user-> response(user))
                .collect(Collectors.toList());
        //List<UserApiResponse>
        //Header<List<UserApiResponse>>
        return Header.OK(userApiResponsesList);

    }*/


}
