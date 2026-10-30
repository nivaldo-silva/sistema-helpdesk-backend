package io.github.nivaldosilva.helpdesk.infra.security;

import io.github.nivaldosilva.helpdesk.modules.usuario.Usuario;
import io.github.nivaldosilva.helpdesk.modules.usuario.UsuarioDTO;
import io.github.nivaldosilva.helpdesk.modules.usuario.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AutenticacaoController implements AutenticacaoDocs {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;
	private final UsuarioService usuarioService;

	@PostMapping("/login")
	public ResponseEntity<Credenciais.Response> login(@Valid @RequestBody Credenciais.Request request) {
		var authentication = authenticationManager.authenticate(
				UsernamePasswordAuthenticationToken.unauthenticated(request.login(), request.senha()));

		Usuario usuario = (Usuario) authentication.getPrincipal();
		usuarioService.registrarAcesso(usuario.getId());
		return ResponseEntity.ok(tokenService.gerar(usuario));
	}

	@PostMapping("/registro")
	public ResponseEntity<UsuarioDTO.Response> registrar(@Valid @RequestBody UsuarioDTO.Request request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criarCliente(request, null));
	}
}