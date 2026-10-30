package io.github.nivaldosilva.helpdesk.modules.empresa;

import io.github.nivaldosilva.helpdesk.common.exception.OperacaoNaoPermitidaException;
import io.github.nivaldosilva.helpdesk.common.exception.RegistroNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmpresaService {

	private final EmpresaRepository empresaRepository;

	@Transactional
	public Empresa criar(EmpresaDTO.Request request) {
		if (empresaRepository.existsByCnpj(request.cnpj())) {
			throw new OperacaoNaoPermitidaException("Já existe uma empresa cadastrada com este CNPJ.");
		}

		Empresa salva = empresaRepository.save(EmpresaMapper.toEntity(request));
		log.info("Empresa {} criada", salva.getId());
		return salva;
	}

	@Transactional(readOnly = true)
	public Empresa buscarPorId(UUID id) {
		return empresaRepository.findById(id)
				.orElseThrow(() -> new RegistroNaoEncontradoException("Empresa não encontrada."));
	}

	@Transactional(readOnly = true)
	public Page<Empresa> listar(Pageable pageable) {
		return empresaRepository.findAll(pageable);
	}

	@Transactional
	public Empresa atualizar(UUID id, EmpresaDTO.AtualizacaoRequest request) {
		Empresa empresa = buscarPorId(id);
		empresa.atualizarDados(
				request.nome().trim(),
				EmpresaMapper.normalizarEmail(request.email()),
				request.telefone());

		log.info("Empresa {} atualizada", id);
		return empresa;
	}

	@Transactional
	public void desativar(UUID id) {
		buscarPorId(id).desativar();
		log.info("Empresa {} desativada", id);
	}

	@Transactional
	public void reativar(UUID id) {
		buscarPorId(id).reativar();
		log.info("Empresa {} reativada", id);
	}
}