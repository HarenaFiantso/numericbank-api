package com.digital.bank.endpoint.rest.controller;

import com.digital.bank.component.DebtComponent;
import com.digital.bank.endpoint.rest.mapper.DebtMapper;
import com.digital.bank.model.Debt;
import com.digital.bank.service.DebtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/debt")
public class DebtController {
<<<<<<< Updated upstream
    private final DebtService service;
    private final DebtMapper mapper;

    public DebtController(DebtService service, DebtMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public List<DebtComponent> getAllDebts(){
        return this.service.getAllDebts()
                .stream()
                .map(this.mapper::toComponent)
                .collect(Collectors.toList());
    }

    @PutMapping("")
    public List<DebtComponent> createOrUpdateDebts(List<Debt> toSave){
        return this.service.createOrUpdateDebts(toSave)
                .stream()
                .map(this.mapper::toComponent)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DebtComponent getDebtById(@PathVariable String id){
        return this.mapper.toComponent(this.service.getDebtById(id));
    }

    @DeleteMapping("/{id}")
    public DebtComponent deleteDebtById(@PathVariable String id){
        return this.mapper.toComponent(this.service.deleteDebtById(id));
    }
=======
  private final DebtService service;
  private final DebtMapper mapper;

  public DebtController(DebtService service, DebtMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping("")
  public List<DebtComponent> getAllDebts() {
    return this.service.getAllDebts().stream()
        .map(this.mapper::toComponent)
        .collect(Collectors.toList());
  }

  @PutMapping("")
  public List<DebtComponent> createOrUpdateDebts(@RequestBody List<Debt> toSave) {
    return this.service.createOrUpdateDebts(toSave).stream()
        .map(this.mapper::toComponent)
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public DebtComponent getDebtById(@PathVariable String id) {
    return this.mapper.toComponent(this.service.getDebtById(id));
  }

  @DeleteMapping("/{id}")
  public DebtComponent deleteDebtById(@PathVariable String id) {
    return this.mapper.toComponent(this.service.deleteDebtById(id));
  }
>>>>>>> Stashed changes
}
