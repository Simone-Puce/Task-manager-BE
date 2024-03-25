package com.fincons.taskmanager.controller;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/task-manager")
public class LombokLoggingController {

    private static final Logger serverLogger = LoggerFactory.getLogger(LombokLoggingController.class);
    @GetMapping("${log4j2.get}")
    public String index() {
        serverLogger.trace("A TRACE Message");
        serverLogger.debug("A DEBUG Message");
        serverLogger.info("An INFO Message");
        serverLogger.warn("A WARN Message");
        serverLogger.error("An ERROR Message");

        return "Howdy! Check out the Logs to see the output...";
    }
}
