package com.example.study.service;

import com.example.study.model.entity.OrderDetail;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.OrderDetailApiRequest;
import com.example.study.model.network.response.OrderDetailApiResponse;
import com.example.study.repository.ItemRepository;
import com.example.study.repository.OrderDetailRepository;
import com.example.study.repository.OrderGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailApiLogicService extends BaseService<OrderDetailApiRequest, OrderDetailApiResponse,OrderDetail> {

@Autowired
    private ItemRepository itemRepository;
@Autowired
    private OrderGroupRepository orderGroupRepository;
@Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public Header<OrderDetailApiResponse> create(Header<OrderDetailApiRequest> request) {
        OrderDetailApiRequest body =  request.getData();
        System.out.println("생성 들어온다.");
      /*  {
            "transaction_time": "2019-10-30T12:06:15.3532683",
                "result_code": "OK",
                "description": "OK",
                "data":{
            "status": "COMPLETE",
                    "arrival_date" : "2019-10-20T12:06:15.3532683",
                    "quantity": 1,
                    "total_price": 123455,
                    "order_group_id": 1,
                    "item_id": 1
             }
        }
*/
        OrderDetail orderDetail = OrderDetail.builder()
                .status(body.getStatus())
                .quantity(body.getQuantity())
                .totalPrice(body.getTotalPrice())
                .orderGroup(orderGroupRepository.getOne(body.getOrderGroupId()))
                .item(itemRepository.getOne(body.getItemId())).build();
        OrderDetail newOrderDetail = orderDetailRepository.save(orderDetail);

        return Header.OK(response(newOrderDetail));
    }



    @Override
    public Header<OrderDetailApiResponse> read(Long id) {
        return orderDetailRepository.findById(id)
                .map(this::response)
                .map(Header::OK)
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }
    @Override
    public Header<OrderDetailApiResponse> update(Header<OrderDetailApiRequest> request) {
        System.out.println("update Servoce들어왔따");
        OrderDetailApiRequest body = request.getData();
    //arrivalDate
        return orderDetailRepository.findById(body.getId())
                .map(orderDetail ->{//있는경우
                    orderDetail
                            .setStatus(body.getStatus())
                            .setArrivalDate(body.getArrivalDate())
                            .setQuantity(body.getQuantity())
                            .setTotalPrice(body.getTotalPrice())
                            .setOrderGroup(orderGroupRepository.getOne(body.getId()))
                            .setItem(itemRepository.getOne(body.getItemId()));

                    return orderDetail;
                })
                .map(changeOrderDetail -> orderDetailRepository.save(changeOrderDetail))
                .map(newOrderDetail -> response(newOrderDetail))
                .map(Header::OK)
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        return orderDetailRepository.findById(id)
                .map(orderDetail -> {
                    orderDetailRepository.delete(orderDetail);
                    return Header.OK();

                })
                .orElseGet(()->Header.ERROR("delete데이터 없음"));
    }

    @Override
    public Header<List<OrderDetailApiResponse>> search(Pageable pageable) {
        Page<OrderDetail> orderDetails =orderDetailRepository.findAll(pageable);

        List<OrderDetailApiResponse> OrderDetailApiResponseList = orderDetails.stream()
                .map(orderDetail -> response(orderDetail))
                .collect(Collectors.toList());


        return Header.OK(OrderDetailApiResponseList);
    }

    //공통되는 로직으로 응답에 대한 공통 메서드를 만든다
    private OrderDetailApiResponse response(OrderDetail orderDetail){

        OrderDetailApiResponse body = OrderDetailApiResponse.builder()
                .id(orderDetail.getId())
                .status(orderDetail.getStatus())
                .arrivalDate(orderDetail.getArrivalDate())
                .quantity(orderDetail.getQuantity())
                .totalPrice(orderDetail.getTotalPrice())
                .orderGroupId(orderDetail.getOrderGroup().getId())
                .itemId(orderDetail.getItem().getId())
                .build();

        return  body;
    }//Header




}
