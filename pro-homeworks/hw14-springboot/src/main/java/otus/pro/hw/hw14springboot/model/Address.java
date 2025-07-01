package otus.pro.hw.hw14springboot.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "address")
public record Address(
        @Id Long id,
        String street
) {}



