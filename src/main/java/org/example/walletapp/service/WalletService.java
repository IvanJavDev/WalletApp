package org.example.walletapp.service;

import org.example.walletapp.dto.OperationRequest;
import org.example.walletapp.dto.WalletResponse;

import java.util.UUID;


public interface WalletService {
    WalletResponse operate(OperationRequest operationRequest);
    WalletResponse getBalance(Long walletId);
}
