package io.github.nivaldosilva.helpdesk.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID>, JpaSpecificationExecutor<Usuario> {

	@Override
	@EntityGraph(attributePaths = "empresa")
	Optional<Usuario> findById(UUID id);

	@Override
	@EntityGraph(attributePaths = "empresa")
	Page<Usuario> findAll(Specification<Usuario> spec, Pageable pageable);

	Optional<Usuario> findByEmailIgnoreCase(String email);
	boolean existsByEmailIgnoreCase(String email);
	boolean existsByEmailIgnoreCaseAndIdNot(String email, UUID id);
	boolean existsByIdAndAtivoTrue(UUID id);
}