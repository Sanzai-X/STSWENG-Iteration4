package com.orangeandbronze.enlistment;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.retry.annotation.*;

@SpringBootApplication
@EnableRetry
public class EnlistmentMvpApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnlistmentMvpApplication.class, args);
    }


}

