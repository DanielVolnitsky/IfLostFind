package com.daniel.iflostfind.controller;

import com.daniel.iflostfind.service.converter.impl.LossConverter;
import com.daniel.iflostfind.service.dto.LossDto;
import com.daniel.iflostfind.domain.Loss;
import com.daniel.iflostfind.domain.LossType;
import com.daniel.iflostfind.service.GoogleMapApiService;
import com.daniel.iflostfind.service.LossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class LossController {

    static final String LOSS_REPORT_PAGE_PATH = "/loss/report";
    static final String LOSS_REPORT_PAGE = "loss_report";

    private final LossConverter lossConverter;
    private final LossService lossService;
    private final GoogleMapApiService googleMapApiService;

    @Autowired
    public LossController(LossConverter lossConverter, LossService lossService, GoogleMapApiService googleMapApiService) {
        this.lossConverter = lossConverter;
        this.lossService = lossService;
        this.googleMapApiService = googleMapApiService;
    }

    @GetMapping(LOSS_REPORT_PAGE_PATH)
    public String toLossReportPage(Model m) {

        m.addAttribute("google_map_key", googleMapApiService.getMapKey());
        m.addAttribute("loss", new LossDto());
        m.addAttribute("lossTypes", LossType.values());

        return LOSS_REPORT_PAGE;
    }

    @PostMapping(LOSS_REPORT_PAGE_PATH)
    public String createLoss(@ModelAttribute("loss") @Valid LossDto lossDto) {

        Loss loss = lossConverter.convertDtoToEntity(lossDto);
        lossService.add(loss);

        return "redirect:" + LOSS_REPORT_PAGE_PATH;
    }
}
