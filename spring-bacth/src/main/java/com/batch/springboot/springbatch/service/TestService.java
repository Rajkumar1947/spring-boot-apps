package com.batch.springboot.springbatch.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    public void test(String a) {
        System.out.println("called from service:: " +a );
    }
}
