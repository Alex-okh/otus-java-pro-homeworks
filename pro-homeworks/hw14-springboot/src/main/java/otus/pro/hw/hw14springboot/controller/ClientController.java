package otus.pro.hw.hw14springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import otus.pro.hw.hw14springboot.model.Address;
import otus.pro.hw.hw14springboot.model.Client;
import otus.pro.hw.hw14springboot.model.Phone;
import otus.pro.hw.hw14springboot.service.ClientService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@Controller
public class ClientController {
    private final ClientService clientService;

    ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @GetMapping("/clients")
    public String clients(Model model) {
        var clientList = clientService.findAll();

        model.addAttribute("clients", clientList);
        model.addAttribute("totalClients", clientList.size());
        model.addAttribute("clientsWithPhones", clientList.stream()
                                                          .filter(c -> c.getPhones() != null && !c.getPhones()
                                                                                                  .isEmpty())
                                                          .count());
        return "clients";
    }

    @GetMapping("/clients/add")
    public String newClient() {
        return "newclient";
    }

    @PostMapping("/newclient")
    public String addClient(Long id, String name, String address, String phone1, String phone2, String phone3,
                            String phone4, String phone5) {
        Set<Phone> phones = Stream.of(phone1, phone2, phone3, phone4, phone5)
                                  .filter(not(String::isBlank))
                                  .map(s -> new Phone(null, s))
                                  .collect(Collectors.toSet());
        Client newclient;
        if (id == null) {
            newclient = new Client(null, name, new Address(null, address.isBlank() ? null : address), phones, true);
        } else {
            newclient = new Client(id, name, new Address(null, address), phones, false);
        }
        clientService.addClient(newclient);
        return "redirect:/clients";
    }

    @PostMapping("/clients/{id}")
    public String editClient(Model model, @PathVariable Long id) {
        Optional<Client> client = clientService.findById(id);
        if (client.isPresent()) {
            Client foundClient = client.get();
            model.addAttribute("client", foundClient);
            return "newclient";
        }
        return "redirect:/clients";
    }

    @DeleteMapping("/clients/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return "redirect:/clients";
    }
}
