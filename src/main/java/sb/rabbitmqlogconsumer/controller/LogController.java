package sb.rabbitmqlogconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sb.rabbitmqlogconsumer.produser.MessageProducer;

@RestController
@RequestMapping("/log")

public class LogController {
    @Autowired
    private  MessageProducer messageProducer;
    @GetMapping("")
    public ResponseEntity<String> getLog() {
        return ResponseEntity.ok("");
    }
    @PostMapping("")
    public ResponseEntity<String> sendLog(@RequestBody String message) {
        messageProducer.send(message);
        return ResponseEntity.ok("");
    }
}
