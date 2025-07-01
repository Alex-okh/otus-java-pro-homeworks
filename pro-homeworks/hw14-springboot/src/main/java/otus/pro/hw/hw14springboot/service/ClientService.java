package otus.pro.hw.hw14springboot.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionManager;
import otus.pro.hw.hw14springboot.model.Client;
import otus.pro.hw.hw14springboot.repository.ClientRepository;

import java.util.List;

@Service
public class ClientService {

    private final TransactionManager transactionManager;
    private final ClientRepository clientRepository;

    public ClientService(TransactionManager transactionManager, ClientRepository clientRepository) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public void addClient(Client client) {
        clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
