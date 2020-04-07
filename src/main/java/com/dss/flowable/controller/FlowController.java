package com.dss.flowable.controller;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class FlowController {

    private RuntimeService runtimeService;
    private HistoryService historyService;

    public FlowController(final RuntimeService runtimeService, final HistoryService historyService) {
        this.runtimeService = runtimeService;
        this.historyService = historyService;
    }

    @GetMapping("/{melicode}/findSchoolByMelicode")
    public ResponseEntity<String> findSchoolByMelicode(@PathVariable("melicode") String melicode) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("melicode", melicode);
        final ProcessInstance process = runtimeService.startProcessInstanceByKey("findSchoolByMelicode", variables);
        final HistoricVariableInstance hvi = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(process.getId())
                .variableName("result").singleResult();
        String school = (String) hvi.getValue();
        return new ResponseEntity<String>(school, HttpStatus.OK);
    }
}
