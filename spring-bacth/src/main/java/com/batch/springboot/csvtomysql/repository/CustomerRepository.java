package com.batch.springboot.csvtomysql.repository;

import com.batch.springboot.csvtomysql.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<User, Integer> {
}
