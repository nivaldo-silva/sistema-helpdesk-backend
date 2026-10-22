package io.github.nivaldosilva.helpdesk.core.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

	private static final ZoneId ZONA_PADRAO = ZoneId.of("America/Recife");
	private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	@ExceptionHandler(RegistroNaoEncontradoException.class)
	public ProblemDetail handleResourceNotFound(RegistroNaoEncontradoException ex, HttpServletRequest request) {
		return buildProblemDetail(HttpStatus.NOT_FOUND, "Registro não encontrado", ex.getMessage(), request);
	}

	@ExceptionHandler(OperacaoNaoPermitidaException.class)
	public ProblemDetail handleBusinessRuleViolation(OperacaoNaoPermitidaException ex, HttpServletRequest request) {
		return buildProblemDetail(HttpStatus.BAD_REQUEST, "Operação não permitida", ex.getMessage(), request);
	}

	@ExceptionHandler(AcessoNegadoAoRecursoException.class)
	public ProblemDetail handleAccessDeniedToResource(AcessoNegadoAoRecursoException ex, HttpServletRequest request) {
		return buildProblemDetail(HttpStatus.FORBIDDEN, "Acesso negado", ex.getMessage(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
		ProblemDetail problemDetail = buildProblemDetail(
				HttpStatus.BAD_REQUEST,
				"Erro de validação",
				"Um ou mais campos são inválidos",
				request
		);

		Map<String, String> erros = new LinkedHashMap<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			erros.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		problemDetail.setProperty("erros", erros);
		return problemDetail;
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ProblemDetail handleHttpMessageNotReadable(HttpServletRequest request) {
		return buildProblemDetail(
				HttpStatus.BAD_REQUEST,
				"Requisição inválida",
				"O corpo da requisição está ausente ou mal formatado",
				request
		);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ProblemDetail handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
		String detalhe = String.format(
				"O parâmetro '%s' recebeu o valor '%s', que é inválido para o tipo esperado (%s)",
				ex.getName(),
				ex.getValue(),
				ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido"
		);
		return buildProblemDetail(HttpStatus.BAD_REQUEST, "Parâmetro inválido", detalhe, request);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ProblemDetail handleNoResourceFound(HttpServletRequest request) {
		return buildProblemDetail(
				HttpStatus.NOT_FOUND,
				"Rota não encontrada",
				"O recurso solicitado não existe: " + request.getRequestURI(),
				request
		);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ProblemDetail handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
		String detalhe = String.format(
				"O método '%s' não é suportado para este recurso. Métodos suportados: %s",
				ex.getMethod(),
				ex.getSupportedHttpMethods()
		);
		return buildProblemDetail(HttpStatus.METHOD_NOT_ALLOWED, "Método não permitido", detalhe, request);
	}


	@ExceptionHandler(AccessDeniedException.class)
	public ProblemDetail handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
		return buildProblemDetail(
				HttpStatus.FORBIDDEN,
				"Acesso negado",
				"Você não tem permissão para realizar esta operação",
				request
		);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
		log.warn("Violação de integridade em {} {}: {}", request.getMethod(), request.getRequestURI(),
				ex.getMostSpecificCause().getClass().getSimpleName());
		return buildProblemDetail(
				HttpStatus.CONFLICT,
				"Violação de integridade de dados",
				"A operação viola uma restrição de integridade do banco de dados (ex.: registro duplicado ou vínculo existente)",
				request
		);
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleGenericException(Exception ex, HttpServletRequest request) {
		log.error("Erro inesperado em {} {}", request.getMethod(), request.getRequestURI(), ex);
		return buildProblemDetail(
				HttpStatus.INTERNAL_SERVER_ERROR,
				"Erro interno do servidor",
				"Ocorreu um erro inesperado. Tente novamente mais tarde.",
				request
		);
	}

	private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, HttpServletRequest request) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
		problemDetail.setTitle(title);
		problemDetail.setInstance(URI.create(request.getRequestURI()));
		problemDetail.setProperty("timestamp", ZonedDateTime.now(ZONA_PADRAO).format(FORMATO_DATA));
		return problemDetail;
	}

	@ExceptionHandler({BadCredentialsException.class, DisabledException.class})
	public ProblemDetail handleCredenciais(HttpServletRequest request) {
		return buildProblemDetail(HttpStatus.UNAUTHORIZED, "Falha na autenticação",
				"Login ou senha inválidos", request);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ProblemDetail handleAuthenticationException(HttpServletRequest request) {
		return buildProblemDetail(HttpStatus.UNAUTHORIZED, "Falha na autenticação",
				"Token ausente, inválido ou expirado", request);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ProblemDetail handleMissingRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
		return buildProblemDetail(HttpStatus.BAD_REQUEST, "Parâmetro obrigatório ausente",
				"O parâmetro '" + ex.getParameterName() + "' é obrigatório", request);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ProblemDetail handleHttpMediaTypeNotSupported(HttpServletRequest request) {
		return buildProblemDetail(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Tipo de conteúdo não suportado",
				"Envie o corpo da requisição como application/json", request);
	}

	@ExceptionHandler(OptimisticLockingFailureException.class)
	public ProblemDetail handleOptimisticLocking(HttpServletRequest request) {
		return buildProblemDetail(HttpStatus.CONFLICT, "Conflito de atualização",
				"O registro foi alterado por outra pessoa. Recarregue e tente novamente", request);
	}
}