package br.com.fernando.api.k2partnering.resources;

import br.com.fernando.api.k2partnering.domain.Categorias;
import br.com.fernando.api.k2partnering.domain.Produtos;
import br.com.fernando.api.k2partnering.dto.CategoriaDTO;
import br.com.fernando.api.k2partnering.service.CategoriaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaService service;

    @ApiOperation(value = "Busca por id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Categorias> find(@PathVariable Integer id) {
        Categorias obj = service.find (id);
        return ResponseEntity.ok ().body (obj);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Busca todas Categorias")
    public ResponseEntity<List<CategoriaDTO>> findAll() {
        List<Categorias> list = service.findAll ();
        List<CategoriaDTO> listDto = list.stream ().map (CategoriaDTO::new).collect (Collectors.toList ());
        return ResponseEntity.ok ().body (listDto);
    }

    @ApiOperation(value="Insere Categoria")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto) {
        Categorias obj = service.fromDTO(objDto);
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value="Atualiza Categotia")
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id) {
        Categorias obj = service.fromDTO(objDto);
        obj.setId(id);
        obj = service.update(obj);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value="Deleta Categoria")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Não é possível excluir uma categoria que possui produtos"),
            @ApiResponse(code = 404, message = "Código inexistente") })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}