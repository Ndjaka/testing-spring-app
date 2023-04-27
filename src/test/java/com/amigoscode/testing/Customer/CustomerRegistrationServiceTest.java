package com.amigoscode.testing.Customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Captor
    private ArgumentCaptor<Customer>  customerArgumentCaptor;
    private  CustomerRegistrationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new CustomerRegistrationService(customerRepository);
    }

    @Test
    void itShouldSaveNewCustomer() {
        // Given
        String phoneNumber = "00099" ;
        Customer customer = new Customer(UUID.randomUUID(),"Maryam", phoneNumber);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        //When
        underTest.registerCustomer(request);

        //Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualToComparingFieldByField(customer);
    }

    @Test
    void itShouldSaveNewCustomerWhenIdIsNull() {
        // Given
        String phoneNumber = "00099" ;
        Customer customer = new Customer(null,"Maryam", phoneNumber);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        //When
        underTest.registerCustomer(request);

        //Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualToIgnoringGivenFields(customer,"id");
        assertThat(customerArgumentCaptorValue.getId()).isNotNull();
    }


    @Test
    void itShouldNotSaveCustomerWhenCustomerExists() {
        // Given
        String phoneNumber = "00099" ;
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id,"Maryam", phoneNumber);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));
        //When
        underTest.registerCustomer(request);

        //Then
         then(customerRepository).should(never()).save(any());
//        then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumber);
//        then(customerRepository).shouldHaveNoMoreInteractions();

    }

    @Test
    void itShouldThrownWhenPhoneNumberIsTaken() {
        // Given
        String phoneNumber = "00099" ;
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id,"Maryam", phoneNumber);
        Customer customerTwo = new Customer(id,"Jhon", phoneNumber);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customerTwo));
        //When
        //Then
        assertThatThrownBy(() -> underTest.registerCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("phone number [%s] is taken", phoneNumber));

        //Finally
        then(customerRepository).should(never()).save(any(Customer.class));



    }
}