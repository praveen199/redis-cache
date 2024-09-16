package com.spring.redis.service;

import com.spring.redis.entity.Invoice;
import com.spring.redis.exception.InvoiceNotFoundException;
import com.spring.redis.respository.InvoiceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {

  @Autowired
  private InvoiceRepository invoiceRepo;

  @Override
  public Invoice saveInvoice(Invoice inv) {
    return invoiceRepo.save(inv);
  }

  @Override
  @CachePut(value="Invoice", key="#invId")
  public Invoice updateInvoice(Invoice inv, Integer invId) {
    System.out.println("Data updated from updateInvoice{} from DB");
    Invoice invoice = invoiceRepo.findById(invId)
        .orElseThrow(() -> new InvoiceNotFoundException("Invoice Not Found"));
    invoice.setInvAmount(inv.getInvAmount());
    invoice.setInvName(inv.getInvName());
    return invoiceRepo.save(invoice);
  }

  @Override
  @CacheEvict(value="Invoice", key="#invId")
  // @CacheEvict(value="Invoice", allEntries=true)
  public void deleteInvoice(Integer invId) {
    System.out.println("Data deleted from deleteInvoice{} from DB");
    Invoice invoice = invoiceRepo.findById(invId)
        .orElseThrow(() -> new InvoiceNotFoundException("Invoice Not Found"));
    invoiceRepo.delete(invoice);
  }

  @Override
  @Cacheable(value="Invoice", key="#invId")
  public Invoice getOneInvoice(Integer invId) {
    System.out.println("Data loaded from getOneInvoice{} from DB");
    Invoice invoice = invoiceRepo.findById(invId)
        .orElseThrow(() -> new InvoiceNotFoundException("Invoice Not Found"));
    return invoice;
  }

  @Override
  @Cacheable(value="Invoice")
  public List<Invoice> getAllInvoices() {
    return invoiceRepo.findAll();
  }
}
