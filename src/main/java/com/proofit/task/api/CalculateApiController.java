package com.proofit.task.api;

import com.proofit.task.model.Bicycles;
import com.proofit.task.model.PremiumResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proofit.task.service.CalculateService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@jakarta.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2024-10-30T13:48:14.508+01:00")

@Controller
public class CalculateApiController implements CalculateApi {

    private static final Logger log = LoggerFactory.getLogger(CalculateApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private CalculateService calculateService;

    @org.springframework.beans.factory.annotation.Autowired
    public CalculateApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<PremiumResponse> calculatePost(
            @ApiParam(value = "List of bicycle objects for which to calculate premiums" ,required=true )
            @Valid @RequestBody Bicycles bicycles) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<PremiumResponse>(objectMapper.readValue("{\"empty\": false}", PremiumResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<PremiumResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        PremiumResponse premiumResponse = calculateService.getResponse(bicycles);

        return new ResponseEntity<>(premiumResponse, HttpStatus.OK);
    }

}
