package io.github.palmierisousa.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/clientes")
public class ClienteControllerDemo {

    /*
    * Consumes e Produces indicam respectivamente os tipos de mime type aceitos para (1) a requisição que chega para o
    * método helloClienteFake e para (2) o tipo de dado enviado no response.
    *
    * O param annotation @RequestBody indica o tipo de objeto recebido no request body da requisição que chega.
    * */
//    @RequestMapping(
//            value = "/hello/{nome}",
//            method = RequestMethod.GET,
//            consumes = { "application/json", "application/xml" },
//            produces = { "application/json", "application/xml" }
//    )
//    @ResponseBody
//    public Cliente helloClienteFake(@PathVariable("nome") String nomeCliente, @RequestBody Cliente cliente) {
//        return new Cliente("Palmieri");
//    }

    // Método somente para demonstração
    @RequestMapping(value = "/hello/{nome}", method = RequestMethod.GET)
    @ResponseBody
    public String helloCliente(@PathVariable("nome") String nomeCliente) {
        return String.format("Hello %s", nomeCliente);
    }
}
