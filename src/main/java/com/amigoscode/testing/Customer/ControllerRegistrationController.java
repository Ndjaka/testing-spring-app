package com.amigoscode.testing.Customer;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/customer-registration")
public class ControllerRegistrationController {

    @PutMapping
    public void registerNewCustomer(
           @Valid @RequestBody CustomerRegistrationRequest request){

    }
}
