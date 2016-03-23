package net.gtidev.test.web;

import lombok.extern.slf4j.Slf4j;
import net.gtidev.test.MemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class MockApiController {

  @Autowired
  private MemoryService memoryService;

  @RequestMapping("/setInt")
  public String setInt(@RequestParam("value") int value) {
    log.info("value has been set to {}", value);
    memoryService.setValue(value);
    return "successfully set value " + value;
  }
}
