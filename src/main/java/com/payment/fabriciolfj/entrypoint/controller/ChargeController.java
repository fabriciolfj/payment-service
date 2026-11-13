package com.payment.fabriciolfj.entrypoint.controller;

import com.payment.fabriciolfj.model.ChargeRender;
import com.payment.fabriciolfj.usecase.CreateChargeUseCase;
import com.payment.fabriciolfj.usecase.FindChargeUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.payment.fabriciolfj.constants.HeaderConstants.*;
import static com.payment.fabriciolfj.entrypoint.controller.ChargeMapper.of;


@RestController
@RequestMapping("/api/v1/charge")
@RequiredArgsConstructor
@Slf4j
public class ChargeController {

    private final CreateChargeUseCase createChargeUseCase;
    private final FindChargeUseCase findChargeUseCase;

    @PostMapping
    public ResponseEntity<String> createCharge(@Valid @RequestBody final ChargeRequest request,
                                       @RequestHeader(IDEMPOTENCE_KEY)
                             @Size(min = IDEMPOTENCE_KEY_MIN_SIZE, max = IDEMPOTENCE_KEY_MAX_SIZE, message = "{idempotency.key.size}")
                             final String idempotencyKey) {

        log.info("idempotency receive {}", idempotencyKey);
        return toResponse(createChargeUseCase.execute(of(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findCharge(@PathVariable final UUID id) {
        return toResponse(findChargeUseCase.execute(id));
    }

    private static ResponseEntity<String> toResponse(final ChargeRender render) {
        return ResponseEntity.status(render.status())
                .contentType(MediaType.APPLICATION_JSON)
                .body(render.body());
    }
}
