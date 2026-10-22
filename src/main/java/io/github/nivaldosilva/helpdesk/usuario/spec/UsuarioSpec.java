package io.github.nivaldosilva.helpdesk.usuario.spec;

import io.github.nivaldosilva.helpdesk.usuario.Usuario;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
		@Spec(path = "nome", spec = LikeIgnoreCase.class),
		@Spec(path = "email", spec = LikeIgnoreCase.class),
		@Spec(path = "perfil", spec = Equal.class),
		@Spec(path = "setor", spec = Equal.class),
		@Spec(path = "empresa.id", params = "empresa_id", spec = Equal.class),
		@Spec(path = "ativo", spec = Equal.class)
})
public interface UsuarioSpec extends Specification<Usuario> {
}