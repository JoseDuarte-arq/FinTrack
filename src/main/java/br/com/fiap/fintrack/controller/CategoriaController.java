package br.com.fiap.fintrack.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.fintrack.model.Categoria;

@RestController
@RequestMapping("categoria")
public class CategoriaController {

    Logger log = LoggerFactory.getLogger(getClass());

    List<Categoria> repository = new ArrayList<>();

    @GetMapping
    public List<Categoria> index(){
        return repository;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Categoria create(@RequestBody Categoria categoria){ // injeta + binding
        log.info("cadastrando categoria {}", categoria );
        repository.add(categoria);
        return categoria;
    }

    @GetMapping("{id}")
    public ResponseEntity<Categoria> show(@PathVariable Long id){
        log.info("buscando categoria com id {} ", id);

        // for(Categoria categoria : repository){
        //     if (categoria.id().equals(id)) 
        //         return ResponseEntity.ok(categoria);
        // }
        var categoriaEncontrada = getCategoriaById(id);


        if (categoriaEncontrada.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(categoriaEncontrada.get());

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> destroy(@PathVariable Long id){
        log.info("apagando categoria {}", id);

        var categoriaEncontrada = getCategoriaById(id);

        if (categoriaEncontrada.isEmpty())
            return ResponseEntity.notFound().build();

        repository.remove(categoriaEncontrada.get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Categoria> update(
        @PathVariable Long id,
        @RequestBody Categoria categoria
    ){
        log.info("atualizando categoria {} para {}", id, categoria);
        // buscar a categoria antiga -> 404
        var categoriaEncontrada = getCategoriaById(id);

        if (categoriaEncontrada.isEmpty())
            return ResponseEntity.notFound().build();

        var categoriaAntiga = categoriaEncontrada.get();

        // criar categoria nova com os dados body
        var categoriaNova = new Categoria(id, categoria.nome(), categoria.icone());

        // remover categoria antiga
        repository.remove(categoriaAntiga);

        // add categoria nova
        repository.add(categoriaNova);

        return ResponseEntity.ok(categoriaNova);
    }








    private Optional<Categoria> getCategoriaById(Long id) {
        var categoriaEncontrada = repository
                                    .stream()
                                    .filter( c -> c.id().equals(id))
                                    .findFirst();
        return categoriaEncontrada;
    }


    
}
