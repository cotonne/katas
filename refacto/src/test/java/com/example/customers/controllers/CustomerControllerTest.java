package com.example.customers.controllers;

import com.example.customers.dao.CustomerH2;
import com.example.customers.dao.LegacyRepository;
import com.example.customers.dao.Point;
import com.example.customers.domain.Customer;
import com.example.customers.domain.Title;
import com.example.customers.service.QueueService;
import com.example.customers.utils.CustomerH2Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @MockBean
    private CustomerH2Dao customerH2DAO;
    @MockBean
    private GeocodingService geocodingService;
    @MockBean
    private LegacyRepository legacyRepository;
    @MockBean
    private QueueService partnerService;

    @Autowired
    private MockMvc mvc;

    private WebApplicationContext context;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Before
    public void setup() {
//        mvc = webAppContextSetup(context)
//                .build();
    }

    @Test
    public void should_response_to_base_request() throws Exception {
        mvc.perform(get("/customers"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_save_a_customer() throws Exception {
        final Customer customer = new Customer();
        customer.identifier = "X001";
        customer.address = "somewhere";
        customer.title = Title.MS;

        // User not found
        given(customerH2DAO.findOneByRef("X001")).willReturn(Optional.empty());

        given(legacyRepository.save(customer)).willReturn(false);
        CustomerH2 savedCustomer = new CustomerH2();
        final CustomerH2 mappedCustomer = CustomerH2Mapper.format(customer);
        given(customerH2DAO.saveAndFlush(mappedCustomer)).willReturn(savedCustomer);

        Customer[] customers = new Customer[]{customer};
        // Save MR to legacy
        mvc.perform(put("/customers")
                .content(this.json(customers))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        // Woman should be persisted in the repository
        verify(legacyRepository, times(1)).save(customer);
        // Should be persisted in repository
        verify(customerH2DAO, times(1)).saveAndFlush(mappedCustomer);
        // An history of all modifications is persisted
        verify(customerH2DAO, times(1)).save(savedCustomer);
        // Woman should not be sent to partner
        verify(partnerService, times(0)).notifyNewCustomer(any());
    }

    @Test
    public void should_update_a_customer() throws Exception {
        Customer customer = new Customer();
        customer.identifier = "X001";
        customer.address = "somewhere";
        customer.title = Title.MR;
        Customer[] customers = new Customer[]{customer};

        CustomerH2 persistedCustomer = new CustomerH2();
        customer.identifier = "X001";
        customer.address = "nowhere";
        customer.title = Title.MR;

        // User not found
        given(customerH2DAO.findOneByRef("X001")).willReturn(Optional.of(persistedCustomer));

        CustomerH2 savedCustomer = new CustomerH2();
        final CustomerH2 mappedCustomer = CustomerH2Mapper.format(customer);
        given(customerH2DAO.saveAndFlush(mappedCustomer)).willReturn(savedCustomer);
        Point point = new Point();
        point.x = 1;
        point.y = 2;
        given(geocodingService.requestGeoCoordinates(customer)).willReturn(point);
        // Save MR to legacy
        mvc.perform(put("/customers")
                .content(this.json(customers))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        // Woman should not be saved in legacy system
        verify(legacyRepository, times(0)).save(customer);
        // Should be persisted in repository
        verify(customerH2DAO, times(1)).saveAndFlush(mappedCustomer);
        // An history of all modifications is persisted
        verify(customerH2DAO, times(1)).save(savedCustomer);
        // Man should be sent to partner
        verify(partnerService, times(1)).notifyNewCustomer(any());
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.stream(converters)
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
}
