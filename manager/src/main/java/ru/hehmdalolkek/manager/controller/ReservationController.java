package ru.hehmdalolkek.manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hehmdalolkek.basemodels.dto.ReservationDto;

@Controller
@RequiredArgsConstructor
@RequestMapping("/book")
public class ReservationController {

    @ModelAttribute("reservation")
    public ReservationDto reservation() {
        return new ReservationDto();
    }

    @GetMapping
    public String initBookForm() {
        return "book";
    }

    @PostMapping
    public String book(@Valid @ModelAttribute("reservation") ReservationDto reservation,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book";
        }
        return "success";
    }

}
