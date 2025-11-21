package com.api.pjpPequenosPassos.controller;

	import com.api.pjpPequenosPassos.model.Profissional;
	import com.api.pjpPequenosPassos.service.ProfissionalService;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.*;

	import java.util.List;
	import java.util.UUID;

	@RestController
	@RequestMapping("/profissionais")
	@CrossOrigin(origins = "*")
	public class ProfissionalController {

	    private final ProfissionalService profissionalService;

	    public ProfissionalController(ProfissionalService profissionalService) {
	        this.profissionalService = profissionalService;
	    }

	    //  CRIAR INDICAÇÃO
	    @PostMapping("/indicacao")
	    public ResponseEntity<Profissional> indicarProfissional(@RequestBody Profissional profissional) {
	        try {
	            profissional.setIndicadoPorPais(true); // sempre indicado por pais
	            Profissional salvo = profissionalService.criar(profissional);
	            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().build();
	        }
	    }

	    //  LISTAR TODAS INDICAÇÕES
	    @GetMapping("/indicacoes")
	    public ResponseEntity<List<Profissional>> listarIndicacoes() {
	        return ResponseEntity.ok(profissionalService.listar());
	    }

	    //  BUSCAR POR ID
	    @GetMapping("/{id}")
	    public ResponseEntity<Profissional> buscarPorId(@PathVariable UUID id) {
	        try {
	            return ResponseEntity.ok(profissionalService.buscarPorId(id));
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    //  FILTRAR POR CIDADE OU ÁREA
	    @GetMapping("/buscar")
	    public ResponseEntity<List<Profissional>> buscarPorFiltro(
	            @RequestParam(required = false) String cidade,
	            @RequestParam(required = false) String area) {

	        return ResponseEntity.ok(
	                profissionalService.buscarPorCidadeOuArea(cidade, area)
	        );
	    }

	    //  BUSCAR POR NOME (nova rota bem útil)
	    @GetMapping("/nome/{nome}")
	    public ResponseEntity<List<Profissional>> buscarPorNome(@PathVariable String nome) {
	        return ResponseEntity.ok(profissionalService.buscarPorNome(nome));
	    }

	    //  MELHORES AVALIADOS (nota mínima)
	    @GetMapping("/avaliacao/{nota}")
	    public ResponseEntity<List<Profissional>> buscarPorAvaliacao(@PathVariable Integer nota) {
	        try {
	            return ResponseEntity.ok(profissionalService.buscarPorAvaliacaoMinima(nota));
	        } catch (RuntimeException e) {
	            return ResponseEntity.badRequest().build();
	        }
	    }

	    //  ÚLTIMOS PROFISSIONAIS ADICIONADOS
	    @GetMapping("/ultimos")
	    public ResponseEntity<List<Profissional>> ultimosAdicionados() {
	        return ResponseEntity.ok(profissionalService.listarUltimos());
	    }

	    //  ATUALIZAR COMPLETO (PUT)
	    @PutMapping("/{id}")
	    public ResponseEntity<Profissional> atualizar(
	            @PathVariable UUID id,
	            @RequestBody Profissional novosDados) {

	        try {
	            return ResponseEntity.ok(profissionalService.atualizar(id, novosDados));
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    //  ATUALIZAÇÃO PARCIAL (PATCH)
	    @PatchMapping("/{id}")
	    public ResponseEntity<Profissional> atualizarParcial(
	            @PathVariable UUID id,
	            @RequestBody Profissional novosDados) {

	        try {
	            return ResponseEntity.ok(profissionalService.atualizarParcial(id, novosDados));
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    //  EXCLUÍR PROFISSIONAL
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> apagar(@PathVariable UUID id) {
	        try {
	            profissionalService.deletar(id);
	            return ResponseEntity.noContent().build(); // 204
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build(); // 404
	        }
	    }
	}
