package com.example.study.model.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemStatus {

    REGISTERED(0,"등록","사용자 등록상태"),
    UNREGISTERED(1,"해지","사용자 해지상태"),
    WAITTING(2,"대기(검수)","상품 검수 상태")
    ;

    private Integer id;
    private String title;
    private String description;



}
