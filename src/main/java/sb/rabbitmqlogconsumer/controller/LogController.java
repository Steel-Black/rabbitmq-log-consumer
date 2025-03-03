package sb.rabbitmqlogconsumer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sb.rabbitmqlogconsumer.produser.MessageProducer;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {
    private final MessageProducer messageProducer;
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
