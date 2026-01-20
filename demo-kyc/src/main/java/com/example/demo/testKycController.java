package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/kyc")
public class testKycController {
	 @GetMapping("/test")
	    public String test() {
	        return "KYC SERVICE WORKING";
	    }
}
