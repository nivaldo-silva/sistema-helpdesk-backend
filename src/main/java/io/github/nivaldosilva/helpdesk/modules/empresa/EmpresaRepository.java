package io.github.nivaldosilva.helpdesk.modules.empresa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {
	boolean existsByCnpj(String cnpj);
}