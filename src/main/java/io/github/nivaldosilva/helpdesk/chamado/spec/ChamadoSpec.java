package io.github.nivaldosilva.helpdesk.chamado.spec;

import io.github.nivaldosilva.helpdesk.chamado.Chamado;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
		@Spec(path = "protocolo", spec = LikeIgnoreCase.class),
		@Spec(path = "titulo", spec = LikeIgnoreCase.class),
		@Spec(path = "status", spec = Equal.class),
		@Spec(path = "prioridade", spec = Equal.class),
		@Spec(path = "categoria", spec = Equal.class),
		@Spec(path = "canalOrigem", params = "canal_origem", spec = Equal.class),
		@Spec(path = "slaEstourado", params = "sla_estourado", spec = Equal.class),
		@Spec(path = "empresa.id", params = "empresa_id", spec = Equal.class),
		@Spec(path = "solicitante.id", params = "solicitante_id", spec = Equal.class),
		@Spec(path = "responsavel.id", params = "responsavel_id", spec = Equal.class),
		@Spec(path = "criadoEm", params = "data_inicio", config = "yyyy-MM-dd'T'HH:mm:ss", spec = GreaterThanOrEqual.class),
		@Spec(path = "criadoEm", params = "data_fim", config = "yyyy-MM-dd'T'HH:mm:ss", spec = LessThanOrEqual.class)
})
public interface ChamadoSpec extends Specification<Chamado> {
}