package sb.rabbitmqlogconsumer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sb.rabbitmqlogconsumer.dto.LogRecordEvent;
import sb.rabbitmqlogconsumer.queue.produser.MessageProducer;

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
    public ResponseEntity<String> sendLog(@RequestBody LogRecordEvent message) {
        try {
            messageProducer.send(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("");
    }
}
