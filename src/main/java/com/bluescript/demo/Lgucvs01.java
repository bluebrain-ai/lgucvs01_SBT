package com.bluescript.demo;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import com.bluescript.demo.repository.KsdscustRepository;
import com.bluescript.demo.repository.ksdsCustEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import io.swagger.annotations.ApiResponses;
import com.bluescript.demo.model.ErrorMsg;
import com.bluescript.demo.model.EmVariable;
import com.bluescript.demo.model.Dfhcommarea;
import com.bluescript.demo.model.CaCustomerRequest;
import com.bluescript.demo.model.CaCustsecrRequest;
import com.bluescript.demo.model.CaPolicyRequest;
import com.bluescript.demo.model.CaPolicyCommon;
import com.bluescript.demo.model.CaEndowment;
import com.bluescript.demo.model.CaHouse;
import com.bluescript.demo.model.CaMotor;
import com.bluescript.demo.model.CaCommercial;
import com.bluescript.demo.model.CaClaim;

@Getter
@Setter
@RequiredArgsConstructor
@Log4j2
@Component

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 400, message = "This is a bad request, please follow the API documentation for the proper request format"),
        @io.swagger.annotations.ApiResponse(code = 401, message = "Due to security constraints, your access request cannot be authorized"),
        @io.swagger.annotations.ApiResponse(code = 500, message = "The server/Application is down. Please contact support team.") })

public class Lgucvs01 {

    @Autowired
    private ErrorMsg errorMsg;
    @Autowired
    private EmVariable emVariable;
    @Autowired
    private Dfhcommarea dfhcommarea;
    @Autowired
    private CaCustomerRequest caCustomerRequest;
    @Autowired
    private CaCustsecrRequest caCustsecrRequest;
    @Autowired
    private CaPolicyRequest caPolicyRequest;
    @Autowired
    private CaPolicyCommon caPolicyCommon;
    @Autowired
    private CaEndowment caEndowment;
    @Autowired
    private CaHouse caHouse;
    @Autowired
    private CaMotor caMotor;
    @Autowired
    private CaCommercial caCommercial;
    @Autowired
    private CaClaim caClaim;

    @Autowired
    private KsdscustRepository ksdscust;
    private int wsResp;
    private int wsResp2;
    private int wsCommareaLen;
    private int wsCommareaLenf;
    private String wsTime;
    private String wsDate;
    private String caData;
    private String wsAbstime;

    @Value("${api.LGSTSQ.host}")
    private String LGSTSQ_HOST;
    @Value("${api.LGSTSQ.uri}")
    private String LGSTSQ_URI;

    private int eibcalen;
    private String caErrorMsg;
    private String wsCustomerArea;

    @PostMapping("/lgucvs01")
    public ResponseEntity<Dfhcommarea> mainline(@RequestBody Dfhcommarea payload) {
        log.debug("Methodmainlinestarted..");
        BeanUtils.copyProperties(payload, dfhcommarea);
        try {
            wsCustomerArea = ksdscust.findById(dfhcommarea.getCaCustomerNum()).get().getCustomerArea();
        } catch (Exception e) {

            log.error("No Record Found" + e.getLocalizedMessage() + " " + e.getMessage());
            wsResp = 1;
        }

        if (wsResp > 0) {
            dfhcommarea.setCaReturnCode(81);
            writeErrorMessage();
            log.error("Error code :, LGV1");
            throw new RuntimeException("LGV1");
            /* return */

        }
        try {
            ksdsCustEntity ksds = new ksdsCustEntity(dfhcommarea.getCaCustomerNum(), dfhcommarea.toString());
            ksdscust.save(ksds);
            /* update findBy first and then use save previous read is with update */} catch (Exception e) {

            log.error("No Record Found" + e.getLocalizedMessage() + " " + e.getMessage());
            wsResp = 1;

        }

        if (wsResp > 0) {
            dfhcommarea.setCaReturnCode(82);
            writeErrorMessage();
            log.error("Error code :, LGV2");
            throw new RuntimeException("LGV2");
            /* return */

        }

        log.debug("Method mainline completed..");
        return new ResponseEntity<>(dfhcommarea, HttpStatus.OK);
    }

    public void writeErrorMessage() {
        log.info("MethodwriteErrorMessagestarted..");
        wsAbstime = LocalTime.now().toString();
        wsAbstime = LocalTime.now().toString();
        wsDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMDDYYYY"));
        wsTime = LocalTime.now().toString();
        errorMsg.setEmDate(wsDate.substring(0, 8));
        errorMsg.setEmTime(wsTime.substring(0, 6));
        WebClient webclientBuilder = WebClient.create(LGSTSQ_HOST);
        try {
            Mono<ErrorMsg> lgstsqResp = webclientBuilder.post().uri(LGSTSQ_URI)
                    .body(Mono.just(errorMsg), ErrorMsg.class).retrieve().bodyToMono(ErrorMsg.class)
                    .timeout(Duration.ofMillis(10_000));
            errorMsg = lgstsqResp.block();
        } catch (Exception e) {
            log.error(e);
        }
        if (eibcalen > 0) {
            if (eibcalen < 91) {
                caData = (dfhcommarea.getCaRequestId().substring(0, 9));
                try {
                    Mono<String> lgstsqResp = webclientBuilder.post().uri(LGSTSQ_URI)
                            .body(Mono.just(caErrorMsg), String.class).retrieve().bodyToMono(String.class)
                            .timeout(Duration.ofMillis(10_000));
                    caErrorMsg = lgstsqResp.block();
                } catch (Exception e) {
                    log.error(e);
                }

            } else {
                caData = (dfhcommarea.getCaRequestId() + dfhcommarea.getCaReturnCode() + dfhcommarea.getCaCustomerNum()
                        + dfhcommarea.getCaRequestSpecific().substring(0, 72));
                try {
                    Mono<String> lgstsqResp = webclientBuilder.post().uri(LGSTSQ_URI)
                            .body(Mono.just(caErrorMsg), String.class).retrieve().bodyToMono(String.class)
                            .timeout(Duration.ofMillis(10_000));
                    caErrorMsg = lgstsqResp.block();
                } catch (Exception e) {
                    log.error(e);
                }

            }

        }

        log.debug("Method writeErrorMessage completed..");
    }

    /* End of program */
}