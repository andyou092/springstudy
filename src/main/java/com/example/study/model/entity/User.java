package com.example.study.model.entity;


import com.example.study.model.enumclass.UserStatus;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"orderGroupList"})
@EntityListeners(AuditingEntityListener.class)//AuditingEntityListener이 감시자를 사용 하겠다.
@Builder//생성자를 원하는데로 생성해준다
@Accessors(chain = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserStatus status;  //고정된 값을 사용  REGISTERED/ UNREGISTERED/WAITING
    private String email;
    private String phoneNumber;

    private LocalDateTime registeredAt;
    private LocalDateTime unregisteredAt;

    @CreatedDate
    private LocalDateTime createdAt;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @LastModifiedBy
    private String updatedBy;


    // User 1 : N OrderGroup
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<OrderGroup>  orderGroupList;





    //1 : N 연관관계 설정
  //  @OneToMany(fetch =FetchType.LAZY, mappedBy = "user")//orderDetail에 있는 변수와 명이 같아야한다. 즉 매칭을 시키는것이다.
  //  private List<OrderDetail> orderDetailList;


}
