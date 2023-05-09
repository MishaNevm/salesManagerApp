package com.example.clientService.repositoryes;

import com.example.clientService.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByShortName (String shortName);
    Optional<Client> findByInn (String inn);
    Optional<Client> findByKpp (String kpp);
    Optional<Client> findByOgrn (String ogrn);

}
