package org.example.walletapp.controllers;

import lombok.RequiredArgsConstructor;
import org.example.walletapp.dto.OperationRequest;
import org.example.walletapp.dto.WalletResponse;
import org.example.walletapp.entity.WalletEntity;
import org.example.walletapp.repository.WalletRepository;
import org.example.walletapp.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/wallet")
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletResponse> operate(@RequestBody @Valid OperationRequest request) {
        return ResponseEntity.ok(walletService.operate(request));

    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable Long walletId) {
        return ResponseEntity.ok(walletService.getBalance(walletId));
    }

}
