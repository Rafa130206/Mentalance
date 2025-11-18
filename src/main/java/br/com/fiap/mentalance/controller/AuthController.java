package br.com.fiap.mentalance.controller;

import br.com.fiap.mentalance.dto.UsuarioRegistroRequest;
import br.com.fiap.mentalance.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("usuario", new UsuarioRegistroRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registrar(@Valid @ModelAttribute("usuario") UsuarioRegistroRequest request,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", request);
            return "register";
        }

        try {
            // Garante que cargo tenha um valor padrão se vazio
            if (request.getCargo() == null || request.getCargo().trim().isEmpty()) {
                request.setCargo("Usuario");
            }
            
            usuarioService.registrarUsuario(request, false);
            model.addAttribute("mensagemSucesso", "Conta criada com sucesso. Faça login para continuar.");
            return "login";
        } catch (Exception ex) {
            model.addAttribute("usuario", request);
            model.addAttribute("mensagemErro", "Erro ao criar conta: " + ex.getMessage());
            ex.printStackTrace(); // Log do erro no console
            return "register";
        }
    }
}

