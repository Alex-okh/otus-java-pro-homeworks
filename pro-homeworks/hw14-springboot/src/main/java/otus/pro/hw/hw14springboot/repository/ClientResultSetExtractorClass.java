package otus.pro.hw.hw14springboot.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import otus.pro.hw.hw14springboot.model.Address;
import otus.pro.hw.hw14springboot.model.Client;
import otus.pro.hw.hw14springboot.model.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ClientResultSetExtractorClass implements ResultSetExtractor<List<Client>> {

    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clientList = new ArrayList<Client>();
        Long previousId = null;
        while (rs.next()) {
            var clientId = rs.getLong(1);
            Client client;
            if (previousId == null || previousId != clientId) {
                client = new Client(clientId, rs.getString(2), new Address(rs.getLong(3), rs.getString(4)),
                                    new HashSet<>(), false);
                clientList.add(client);
                previousId = clientId;
            } else {
                client = clientList.getLast();
            }
            var phoneNumber = rs.getString(6);
            if (client != null && phoneNumber != null) {
                client.getPhones()
                      .add(new Phone(rs.getLong(5), phoneNumber));
            }
        }
        return clientList;
    }
}
