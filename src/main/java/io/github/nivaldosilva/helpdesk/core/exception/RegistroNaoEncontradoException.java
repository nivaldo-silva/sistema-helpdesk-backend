package io.github.nivaldosilva.helpdesk.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RegistroNaoEncontradoException extends RuntimeException {
	public RegistroNaoEncontradoException(String message) {
		super(message);
	}
}