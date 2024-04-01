package com.digital.bank.endpoint.rest.controller;

import com.digital.bank.component.TransferBodyComponent;
import com.digital.bank.component.TransferComponent;
import com.digital.bank.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
@AllArgsConstructor
public class TransferController {

  private final TransferService service;
  @PostMapping("")
  public TransferComponent makeTransfer(@RequestBody TransferBodyComponent toMake) {
    return this.service.makeTransfer(toMake);
  }
}
