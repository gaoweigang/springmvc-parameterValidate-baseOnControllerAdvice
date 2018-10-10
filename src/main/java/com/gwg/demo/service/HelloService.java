package com.gwg.demo.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloService {

    public void unknownException(){
         int i = 1 / 0;
    }
}
