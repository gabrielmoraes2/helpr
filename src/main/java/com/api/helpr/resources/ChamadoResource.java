package com.api.helpr.resources;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.helpr.domain.Chamado;
import com.api.helpr.domain.LogChamadoPrioridade;
import com.api.helpr.domain.LogChamadoStatus;
import com.api.helpr.domain.dtos.ChamadoDTO;
import com.api.helpr.services.ChamadoService;

@RestController
@RequestMapping(value="/service/chamados")
public class ChamadoResource {
	
	@Autowired
	private ChamadoService service;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ChamadoDTO> findById(@PathVariable Integer id){
		Chamado obj = service.findById(id);
		return ResponseEntity.ok().body(new ChamadoDTO(obj));
	}
	
	@GetMapping
	public ResponseEntity<List<ChamadoDTO>> findAll(){
		List<Chamado> list = service.findAll();
		List<ChamadoDTO> listDto = list.stream().map(obj -> new ChamadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
	
	@GetMapping(value = "/relatorios/cliente/{cliente}")
	public ResponseEntity<List<ChamadoDTO>> findReportChamadoCliente(@PathVariable Integer cliente) {
		List<Chamado> reportList = service.reportChamadoCliente(cliente);
		List<ChamadoDTO> listDto = reportList.stream().map(rel -> new ChamadoDTO(rel)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
	
	@GetMapping(value="/relatorios/tecnico/{tecnico}")
	public ResponseEntity<List<ChamadoDTO>> findReportChamadoTecnico(@PathVariable Integer tecnico){
		List<Chamado> reportList = service.reportChamadoTecnico(tecnico);
		List<ChamadoDTO> listDto = reportList.stream().map(rel -> new ChamadoDTO(rel)).collect(Collectors.toList()); 
		return ResponseEntity.ok().body(listDto);
	}

	@GetMapping(value = "/relatorios/chamado")
	public ResponseEntity<List<ChamadoDTO>> findReportChamadoChamado(){
		LocalDate dataDia = LocalDate.now();
		List<Chamado> reportList = service.reportChamadoChamado(dataDia);
		List<ChamadoDTO> listDto = reportList.stream().map(rel -> new ChamadoDTO(rel)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
	

    @GetMapping(value="/relatorios/status/{status}")
	public ResponseEntity<List<ChamadoDTO>> findReportChamadoStatus(@PathVariable Integer status){
		List<Chamado> list = service.reportChamadoStatus(status);
		List<ChamadoDTO> listDto = list.stream().map(obj -> new ChamadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
    
 	@PreAuthorize("hasAnyRole('ROLE_TECNICO')")
	@PostMapping
	public ResponseEntity<ChamadoDTO> create(@Valid @RequestBody ChamadoDTO objDto){
		Chamado obj = service.create(objDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PreAuthorize("hasAnyRole('ROLE_TECNICO')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<ChamadoDTO> update(@PathVariable Integer id, @Valid @RequestBody ChamadoDTO objDto){
		Chamado newObj = service.update(id, objDto);
		return ResponseEntity.ok().body(new ChamadoDTO(newObj));
	}	
	
	@GetMapping(value="/log/status")
	public ResponseEntity<List<LogChamadoStatus>> findLogStatusChamado(){
		LocalDate dataDia = LocalDate.now();
		List <LogChamadoStatus> logList = service.findDiaLogChamado(dataDia);
		return ResponseEntity.ok().body(logList);
	}
	   @GetMapping(value="/log/prioridade")
		public ResponseEntity<List<LogChamadoPrioridade>> findLogPrioridadeChamado(){
			LocalDate dataDia = LocalDate.now();
			List <LogChamadoPrioridade> logList = service.findDiaLogChamadoPrioridades(dataDia);
			return ResponseEntity.ok().body(logList);
		}

	@PreAuthorize("hasAnyRole('ROLE_TECNICO')")
	@GetMapping(value= "/relatorio-semanal/tecnico/{id}")
	public ResponseEntity<List<ChamadoDTO>> findRelChamadoSemanalTecnico(@PathVariable Integer id) {
		List<Chamado> relatorioList = service.relChamadoSemanalTecnico(id);
		List<ChamadoDTO> listRelDto = relatorioList.stream().map(rel -> new ChamadoDTO(rel)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listRelDto);
	}

	@PreAuthorize("hasAnyRole('ROLE_TECNICO')")
	@GetMapping(value= "/relatorio-hoje/tecnicos")
	public ResponseEntity<List<ChamadoDTO>> findRelChamadoHojeTecnicos() {
		List<Chamado> relatorioListAtual = service.relChamadosHojeTecnicos();
		List<ChamadoDTO> listRelDto = relatorioListAtual.stream().map(rel -> new ChamadoDTO(rel)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listRelDto);
	}

	@GetMapping(value = "/titulo/{titulo}")
	public ResponseEntity<List<ChamadoDTO>> findByTitulo(@PathVariable String titulo){
		List<Chamado> list = service.findByTitulo(titulo);
		List<ChamadoDTO> listDto = list.stream().map(obj -> new ChamadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
}
