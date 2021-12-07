package io.github.palmierisousa.domain.repository;

import io.github.palmierisousa.domain.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Clients extends JpaRepository<Client, Integer> {
    // Query methods: O JpaRepository tem um padrão de criação de queries em tempo de compilação. Se uma query
    // específica não existe no JpaRepository, ela pode ser criada utilizando alguns padrões do sql.
    // Por exemplo, findByNomeContaining, será criada uma query para buscar por NOME que contenha.
    // Vale lembrar que o nome do método deve conter o nome da propriedade da classe entity, deve usar os literals do
    // sql.
    // Outro exemplo, findByNomeOrIdOrderById.

    /*
     *
     * Using Like: select ... like :username
     *   List<User> findByUsernameLike(String username);
     *
     * StartingWith: select ... like :username%
     *   List<User> findByUsernameStartingWith(String username);
     *
     * EndingWith: select ... like %:username
     *   List<User> findByUsernameEndingWith(String username);
     *
     * Containing: select ... like %:username%
     *   List<User> findByUsernameContaining(String username);
     */
//    List<Client> findByNameContaining(String nome);
//
//    List<Client> findByNameOrIdOrderById(String nome, Integer id);
//
//    Client findOneByName(String nome);
//
//    boolean existsByNome(String nome);
//
//    void deleteByNome(String nome);

    @Query("delete from Client c where c.name = :name")
    @Modifying
    void deleteByName(@Param("name") String name);

    @Query(value = "select * from Client c where c.name like %:name% ", nativeQuery = true)
    List<Client> findByName(@Param("name") String name);

    @Query("select c from Client c left join fetch c.orders where c.id = :id")
    Client findClientFetchOrders(@Param("id") Integer id);
}