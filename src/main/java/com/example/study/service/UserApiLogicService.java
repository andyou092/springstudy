package com.example.study.service;


import com.example.study.ifs.CrudInterface;
import com.example.study.model.entity.OrderGroup;
import com.example.study.model.entity.User;
import com.example.study.model.enumclass.UserStatus;
import com.example.study.model.network.Header;
import com.example.study.model.network.Pagination;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import com.example.study.model.network.response.OrderGroupApiResponse;
import com.example.study.model.network.response.UserApiResponse;
import com.example.study.model.network.response.UserOrderInfoApiResponse;
import com.example.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserApiLogicService implements CrudInterface<UserApiRequest, UserApiResponse> {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private OrderGroupApiLogicService   orderGroupApiLogicService;

    @Autowired
    private ItemApiLogicService itemApiLogicService;

    //1. request data를 가져오기
    //2. user를 생성
    //3. 생성된 데이터를 기준으로 userApiResponse return
    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {
    //1. request data
        UserApiRequest userApiRequest = request.getData();

    //2. user 생성
        User user = User.builder()
                .account(userApiRequest.getAccount())
                .password(userApiRequest.getPassword())
                .status(UserStatus.REGISTERED)
                .phoneNumber(userApiRequest.getPhoneNumber())
                .email(userApiRequest.getEmail())
                .registeredAt(LocalDateTime.now())
                .build();
        User newUser = userRepository.save(user);
        //3. 생성된 데이터 -> userApiResponse return

        return Header.OK(response(newUser)) ;
    }

    @Override
    public Header<UserApiResponse> read(Long id) {
        System.out.println("들어온다.");
        //id - > repository getOne , getById
        // user -> userApiResponse return
       return userRepository.findById(id)
               .map(user -> response(user))
               .map(userApiResponse -> Header.OK(userApiResponse))
              //Header :: OK
               .orElseGet(
                       ()->Header.ERROR("데이터 없음")
               );
    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> request) {
        //1.data
        UserApiRequest userApiRequest = request.getData();
        //2. id - > user 데이터를 찾고
        Optional<User>  optional = userRepository.findById(userApiRequest.getId());
        return optional.map(user->{
            //3. data -> update
            //id
            user.setAccount(userApiRequest.getAccount())
                 .setPassword(userApiRequest.getPassword())
                 .setStatus(userApiRequest.getStatus())
                 .setPhoneNumber(userApiRequest.getPhoneNumber())
                 .setEmail(userApiRequest.getEmail())
                 .setRegisteredAt(userApiRequest.getRegisteredAt())
                 .setUnregisteredAt(userApiRequest.getUnRegisteredAt());
                return user;
                })
                .map(user -> userRepository.save(user))         //update
                .map(updateUser -> response(updateUser))        //userApiResponse
                .map(Header::OK)
                .orElseGet(()->Header.ERROR("데이터없음"));
    }

    @Override
    public Header delete(Long id) {
        //id - > repositoru를 통해서 user를 찾고
        Optional<User> optional = userRepository.findById(id);

        // repository를 통해서 delete를 해주고
        return optional.map(user ->{
            userRepository.delete(user);
            return  Header.OK();
        })
         .orElseGet(()->Header.ERROR("데이터 없음"));

        // response로 return 해주면 된다

    }

//나중에도 계속 사용 할 것이기에
    private  UserApiResponse response(User user){
        //user - > userApiResponse
        UserApiResponse userApiResponse = UserApiResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword())//todo 암호화, 길이
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .registeredAt(user.getRegisteredAt())
                .unRegisteredAt(user.getUnregisteredAt())
        // Header + data return
                .build();
        return  userApiResponse;
    }

//페이징을 위해서
    public Header<List<UserApiResponse>> search(Pageable pageable) {

        Page<User> users = userRepository.findAll(pageable);

        List<UserApiResponse> userApiResponseList = users.stream()
                .map(user-> response(user))
                .collect(Collectors.toList());
        Pagination pagination = Pagination.builder()
                .totalPages(users.getTotalPages())
                .totalElements(users.getTotalElements())
                .currentPage(users.getNumber())
                .currentElements(users.getNumberOfElements())
                .build();

        return Header.OK(userApiResponseList,pagination);
    }

    public Header<UserOrderInfoApiResponse> orderInfo(Long id) {

        //user
        User user = userRepository.getOne(id);
        UserApiResponse userApiResponse = response(user);


        //orderGroup
        List<OrderGroup> orderGroupList = user.getOrderGroupList();
        List<OrderGroupApiResponse> orderGroupApiResponseList =orderGroupList.stream()
                .map(orderGroup -> {
                  OrderGroupApiResponse orderGroupApiResponse=  orderGroupApiLogicService.response(orderGroup);

               //item
                  List<ItemApiResponse> itemApiResponseList =orderGroup.getOrderDetailList().stream()
                          .map(detail -> detail.getItem())
                          .map(item -> itemApiLogicService.response(item))
                          .collect(Collectors.toList());

                  orderGroupApiResponse.setItemApiResponseList(itemApiResponseList);
                  return orderGroupApiResponse;
                })
                .collect(Collectors.toList());
        userApiResponse.setOrderGroupApiResponseList(orderGroupApiResponseList);
        UserOrderInfoApiResponse userOrderInfoApiResponse = UserOrderInfoApiResponse.builder()
                .userApiResponse(userApiResponse)
                .build();

        return Header.OK(userOrderInfoApiResponse);
        //item
       // List<Item> itemList = orderGroupList.g

    }
}
