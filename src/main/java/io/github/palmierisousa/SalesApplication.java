package io.github.palmierisousa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SalesApplication {
    //TODO refatorar os dtos
    //TODO proibir adicionar entidades repetidas, por exemplo, clientes, usuários, produtos, pedidos
    //TODO tornar admin ou não um usuário
    //TODO selecionar usuário
    //TODO atualizar pedido
    public static void main(String[] args) {
        SpringApplication.run(SalesApplication.class, args);
    }
}