package io.github.nivaldosilva.helpdesk.modules.comentario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ComentarioRepository extends JpaRepository<Comentario, UUID> {

	@EntityGraph(attributePaths = "autor")
	Page<Comentario> findAllByChamadoIdOrderByCriadoEmAsc(UUID chamadoId, Pageable pageable);

	@EntityGraph(attributePaths = "autor")
	Page<Comentario> findAllByChamadoIdAndNotaInternaFalseOrderByCriadoEmAsc(UUID chamadoId, Pageable pageable);
}