package com.example.customers.controllers;

import com.example.customers.controller.Step;
import com.example.customers.dao.CustomerH2;
import com.example.customers.dao.LegacyRepository;
import com.example.customers.domain.Customer;
import com.example.customers.domain.Report;
import com.example.customers.domain.ReportType;
import com.example.customers.domain.Title;
import com.example.customers.domain.exception.SaveException;
import com.example.customers.service.NewCustomerNotification;
import com.example.customers.service.QueueService;
import com.example.customers.utils.CustomerH2Mapper;
import com.example.customers.utils.CustomerMapper;
import com.example.customers.utils.ReportHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
    @Inject
    private CustomerH2Dao customerH2DAO;
    @Inject
    private GeocodingService geocodingService;
    @Inject
    private LegacyRepository legacyRepository;
    @Inject
    private QueueService partnerService;

    @RequestMapping(method = GET)
    @ResponseBody
    public String ok() {
        return "OK";
    }

    @RequestMapping(method = PUT)
    @ResponseBody
    public Report save(@RequestBody List<Customer> customers) {
        LOGGER.info("Saving customers...");
        Report report = new Report();
        report.entity = ReportType.CUSTOMER;
        customers.forEach(customer -> {
            LOGGER.debug("customer: {} ", customer);
            try {
                saveOne(customer);
                ReportHelper.indicateSuccess(report, customer.identifier);
            } catch (SaveException e) {
                LOGGER.error("Exception while saving {} into {}. Cause {}", customer.identifier, e.getStep().toString(), e.getMessage());
                ReportHelper.indicateFailure(report, customer.identifier, e.getStep(), e);
            }
        });
        LOGGER.info("Done - Results : OK: {}, ERRORS: {}", report.ok.size(), report.ko.size());
        return report;
    }

    @Transactional
    private Customer saveOne(Customer customer) throws SaveException {
        Step curStep = Step.UNKNOWN;
        try {
            curStep = Step.RETRIEVING_PREVIOUS_CUSTOMER;
            Optional<CustomerH2> existingCustomerOpt = customerH2DAO.findOneByRef(customer.identifier);

            if (existingCustomerOpt.isPresent()) {
                CustomerH2 existingCustomer = existingCustomerOpt.get();
                customer.id = existingCustomer.id;
                customer.dateCreated = existingCustomer.creation;
                customer.version = existingCustomer.version;
                // Update geocoordinates if needed
                if (!existingCustomer.hasPosition()
                        || !(   existingCustomer.address != null &&
                                existingCustomer.address.equals(customer.address))) {
                    try {
                        customer.position = geocodingService.requestGeoCoordinates(customer);
                    } catch (GeocodingException e) {
                        customer.position = null;
                    }
                }
            } else {
                customer.position = geocodingService.requestGeoCoordinates(customer);
            }

            if (!Title.MR.equals(customer.title) && Title.MS.equals(customer.title)) {
                curStep = Step.SAVE_TO_LEGACY;
                boolean v = legacyRepository.save(customer);
                System.out.println(v);
            }

            curStep = Step.SAVE_TO_BASE;
            CustomerH2 savedCustomer = customerH2DAO.saveAndFlush(CustomerH2Mapper.format(customer));

            curStep = Step.SAVE_TO_HISTORY;
            customerH2DAO.save(savedCustomer);

            curStep = Step.SEND_TO_QUEUE;
            // Notify Partner if new customer
            notifyIfNewPartnerAccount(customer, existingCustomerOpt);

            return customer;
        } catch (Exception e) {
            throw new SaveException(curStep, "Load failed to load a customer", e);
        }
    }

    private void notifyIfNewPartnerAccount(Customer newCustomer, Optional<CustomerH2> existingCustomerOpt) {
        if (Title.MR.equals(newCustomer.title)) {
            NewCustomerNotification newCustomerNotif = CustomerMapper.getNewAccountNotification(newCustomer);
            // If customer exists
            if (existingCustomerOpt.isPresent()) {
                // Generate a notification for existting customer
                NewCustomerNotification existingCustomerNotif = CustomerMapper.getNewAccountNotification(existingCustomerOpt.get());
                if (!newCustomerNotif.equals(existingCustomerNotif)) {
                    partnerService.notifyNewCustomer(newCustomerNotif);
                }
            } else {
                partnerService.notifyNewCustomer(newCustomerNotif);
            }
        }
    }

}
