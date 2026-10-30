package io.github.nivaldosilva.helpdesk.modules.chamado;

import io.github.nivaldosilva.helpdesk.modules.chamado.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;
import java.util.UUID;

public interface ChamadoRepository extends JpaRepository<Chamado, UUID>, JpaSpecificationExecutor<Chamado> {

	@Override
	@EntityGraph(attributePaths = {"empresa", "solicitante", "responsavel"})
	Optional<Chamado> findOne(Specification<Chamado> spec);

	@Override
	@EntityGraph(attributePaths = {"empresa", "solicitante", "responsavel"})
	Page<Chamado> findAll(Specification<Chamado> spec, Pageable pageable);

	@EntityGraph(attributePaths = {"empresa", "solicitante", "responsavel"})
	Page<Chamado> findAllByStatus(Status status, Pageable pageable);
}