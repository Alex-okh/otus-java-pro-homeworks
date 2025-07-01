package otus.pro.hw.hw14springboot.repository;

import org.springframework.data.repository.ListCrudRepository;
import otus.pro.hw.hw14springboot.model.Client;

public interface ClientRepository extends ListCrudRepository<Client, Long> {

}
