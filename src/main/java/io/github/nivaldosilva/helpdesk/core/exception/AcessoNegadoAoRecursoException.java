package io.github.nivaldosilva.helpdesk.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AcessoNegadoAoRecursoException extends RuntimeException {
	public AcessoNegadoAoRecursoException(String message) {
		super(message);
	}
}
