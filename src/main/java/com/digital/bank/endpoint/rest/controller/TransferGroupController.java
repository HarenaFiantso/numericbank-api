package com.digital.bank.endpoint.rest.controller;

import com.digital.bank.component.TransferGroupComponent;
import com.digital.bank.endpoint.rest.mapper.TransferGroupMapper;
import com.digital.bank.model.TransferGroup;
import com.digital.bank.repository.TransferGroupRepository;
import com.digital.bank.service.TransferGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

  @PutMapping
  public List<TransferGroupComponent> createOrUpdateTransferGroups(
      @RequestBody List<TransferGroup> toSave) {
    return this.service.createOrUpdateTransferGroups(toSave).stream()
        .map(this.mapper::toComponent)
        .collect(Collectors.toList());
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
