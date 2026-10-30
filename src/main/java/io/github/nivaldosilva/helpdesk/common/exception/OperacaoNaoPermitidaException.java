package io.github.nivaldosilva.helpdesk.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OperacaoNaoPermitidaException extends RuntimeException {
	public OperacaoNaoPermitidaException(String message) {
		super(message);
	}
}
