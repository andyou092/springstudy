package com.example.study.repository;

import com.example.study.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    //이런것들이 쿼리 메소드이다.
    //selct * from user where account = ? << test01 , aaaa
   // Optional<User> findByAccount(String account);

   // Optional<User> findByEmail(String email);
    //id를 역순해서 핸드폰 번호를 매칭을 시켜서 출력

    User findFirstByPhoneNumberOrderByIdDesc(String phoneNumber);
    //selct * from user where account = ? and email =?  << test01 , aaaa
   // Optional<User> findByAccountAndEmail(String account, String email);







}
