package com.zhuzx.user.repository;

import com.zhuzx.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @ClassName UserRepository
 * @Description TODO
 * @Author zhuzhenxiong
 * @Date 2021/4/2 14:02
 * @Version 1.0
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);
}
