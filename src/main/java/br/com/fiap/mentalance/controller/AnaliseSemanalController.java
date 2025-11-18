package br.com.fiap.mentalance.controller;

import br.com.fiap.mentalance.dto.AnaliseSemanalRequest;
import br.com.fiap.mentalance.model.Usuario;
import br.com.fiap.mentalance.service.AnaliseSemanalService;
import br.com.fiap.mentalance.service.SessaoUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/analise-semanal")
@RequiredArgsConstructor
public class AnaliseSemanalController {

    private final AnaliseSemanalService analiseSemanalService;
    private final SessaoUsuarioService sessaoUsuarioService;

    @GetMapping("/novo")
    public String form(Model model) {
        model.addAttribute("analise", new AnaliseSemanalRequest());
        return "analise-semanal-form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("analise") AnaliseSemanalRequest request,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("analise", request);
            return "analise-semanal-form";
        }

        try {
            Usuario usuario = sessaoUsuarioService.getUsuarioAtual();
            // Por padrão, usa JPA. Para usar procedures Oracle, passe true como terceiro parâmetro
            analiseSemanalService.inserirAnaliseSemanal(usuario, request, false);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Análise semanal registrada com sucesso!");
            return "redirect:/";
        } catch (Exception ex) {
            model.addAttribute("analise", request);
            model.addAttribute("mensagemErro", "Erro ao criar análise: " + ex.getMessage());
            return "analise-semanal-form";
        }
    }
}

