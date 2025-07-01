package otus.pro.hw.hw14springboot.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import otus.pro.hw.hw14springboot.model.Client;

import java.util.List;

public interface ClientRepository extends ListCrudRepository<Client, Long> {

    @Override
    @Query(value = """
            SELECT 	c.id as client_id,
            		c.name as client_name,
            		a.id as address_id,
            		a.street as address_street,
            		p.id as phone_id,
            		p.number as phone_number
            from
            		client c
            		left outer join address a on c.id = a.client_id\s
            		left outer join phone p on c.id = p.client_id
            order by client_id

            """, resultSetExtractorClass = ClientResultSetExtractorClass.class)
    List<Client> findAll();
}
