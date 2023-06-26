package com.example.clientService.repositoryes;

import com.example.clientService.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByShortName (String shortName);
    Optional<Client> findByInn (String inn);
    Optional<Client> findByKpp (String kpp);
    Optional<Client> findByOgrn (String ogrn);

    @Query("SELECT c FROM Client c WHERE c.shortName = :shortName " +
            "or c.inn = :inn " +
            "or c.kpp = :kpp " +
            "or c.ogrn = :ogrn")
    List<Client> findByAnyParameter(@Param("shortName") String shortName,
                                    @Param("inn") String inn,
                                    @Param("kpp") String kpp,
                                    @Param("ogrn") String ogrn);
}
