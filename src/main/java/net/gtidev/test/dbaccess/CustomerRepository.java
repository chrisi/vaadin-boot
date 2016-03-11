package net.gtidev.test.dbaccess;

import net.gtidev.test.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
  List<Customer> findByLastNameStartsWithIgnoreCase(String lastName);
}
