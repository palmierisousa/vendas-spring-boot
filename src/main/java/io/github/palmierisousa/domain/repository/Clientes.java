package io.github.palmierisousa.domain.repository;

import io.github.palmierisousa.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Clientes extends JpaRepository<Cliente, Integer> {
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
    List<Cliente> findByNomeContaining(String nome);

    List<Cliente> findByNomeOrIdOrderById(String nome, Integer id);

    Cliente findOneByNome(String nome);

    boolean existsByNome(String nome);

    void deleteByNome(String nome);

    @Query("delete from Cliente c where c.nome = :nome")
    @Modifying
    void deletarPorNome(@Param("nome") String nome);

    @Query(value = "select * from cliente c where c.nome like %:nome% ", nativeQuery = true)
    List<Cliente> encontrarPorNome(@Param("nome") String nome);

    @Query("select c from Cliente c left join fetch c.pedidos where c.id = :id")
    Cliente findClienteFetchPedidos(@Param("id") Integer id);
}