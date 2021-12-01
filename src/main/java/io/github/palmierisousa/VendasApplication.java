package io.github.palmierisousa;

import io.github.palmierisousa.domain.entity.Cliente;
import io.github.palmierisousa.domain.entity.Pedido;
import io.github.palmierisousa.domain.repository.Clientes;
import io.github.palmierisousa.domain.repository.Pedidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class VendasApplication {

    @Bean
    public CommandLineRunner init(@Autowired Clientes clientes, @Autowired Pedidos pedidos){
        return args -> {
            System.out.println("Salvando clientes");
            clientes.save(new Cliente("Palmieri"));
            clientes.save(new Cliente("Ayres"));

            List<Cliente> todosClientes = clientes.findAll();
            todosClientes.forEach(System.out::println);

            System.out.println("Adicionando pedidos");
            Pedido p = new Pedido();
            p.setCliente(clientes.findOneByNome("Palmieri"));
            p.setDataPedido(LocalDate.now());
            p.setTotal(BigDecimal.valueOf(100));
            pedidos.save(p);

            System.out.println("Buscando cliente e seu pedido");
            Cliente cliente = clientes.findClienteFetchPedidos(clientes.findOneByNome("Palmieri").getId());
            System.out.println(cliente);
            System.out.println(cliente.getPedidos());

            System.out.println("Buscando os pedidos de um cliente");
            pedidos.findByCliente(clientes.findOneByNome("Palmieri")).forEach(System.out::println);

            System.out.println("Buscando clientes por nome");
            List<Cliente> result = clientes.encontrarPorNome("Palm");
            result.forEach(System.out::println);

            System.out.println("Testando query methods");
            boolean existe = clientes.existsByNome("Sousa");
            System.out.println("Existe um cliente com o nome Palmieri? " + existe);

            System.out.println("Atualizando clientes");
            todosClientes.forEach(c -> {
                c.setNome(c.getNome() + " atualizado.");
                clientes.save(c);
            });

            todosClientes = clientes.findAll();
            todosClientes.forEach(System.out::println);

            System.out.println("Buscando clientes que contenha Ay");
            clientes.findByNomeContaining("Ay").forEach(System.out::println);

            System.out.println("Deletando clientes");
            clientes.findAll().forEach(clientes::delete);

            todosClientes = clientes.findAll();
            if(todosClientes.isEmpty()){
                System.out.println("Nenhum cliente encontrado.");
            } else {
                todosClientes.forEach(System.out::println);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }
}