package com.digital.bank.endpoint.rest.controller;

import com.digital.bank.component.TransferBodyComponent;
import com.digital.bank.component.TransferComponent;
import com.digital.bank.component.TransferGroupComponent;
import com.digital.bank.endpoint.rest.mapper.TransferGroupMapper;
import com.digital.bank.service.TransferGroupService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer-group")
public class TransferGroupController {
  private final TransferGroupService service;
  private final TransferGroupMapper mapper;

  public TransferGroupController(TransferGroupService service, TransferGroupMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping("")
  public List<TransferGroupComponent> getAllTransferGroups() {
    return this.service.getAllTransferGroups().stream()
        .map(this.mapper::toComponent)
        .collect(Collectors.toList());
  }

  @PostMapping("")
  public List<TransferComponent> createOrUpdateTransferGroups(
      @RequestBody List<TransferBodyComponent> toSave) {
    return this.service.createOrUpdateTransferGroups(toSave);
  }

  @GetMapping("/{id}")
  public TransferGroupComponent getTransferGroupById(@PathVariable String id) {
    return this.mapper.toComponent(this.service.getTransferGroupById(id));
  }

  @DeleteMapping("/{id}")
  public TransferGroupComponent deleteTransferGroupById(@PathVariable String id) {
    return this.mapper.toComponent(this.service.deleteTransferGroupById(id));
  }
}
