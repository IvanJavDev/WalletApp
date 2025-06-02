package org.example.walletapp.service;


import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.example.walletapp.dto.OperationRequest;
import org.example.walletapp.dto.WalletResponse;
import org.example.walletapp.entity.WalletEntity;
import org.example.walletapp.exception.NotEnoughMoneyException;
import org.example.walletapp.exception.WalletNotFoundException;
import org.example.walletapp.repository.WalletRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class WalletServiceImp implements WalletService {

    private final WalletRepository walletRepository;

    @Override

    public WalletResponse operate(OperationRequest operationRequest) {
        WalletEntity walletEntity = walletRepository.findById(operationRequest.getWalletId())
                .orElseGet(() -> {
                    WalletEntity wallet = new WalletEntity();
                    wallet.setWalletId(operationRequest.getWalletId());
                    wallet.setBalance(BigDecimal.valueOf(0));
                    return walletRepository.save(wallet);
                });
        switch (operationRequest.getOperationType()) {
            case DEPOSIT -> walletEntity.setBalance(walletEntity.getBalance().add(operationRequest.getAmount()));
            case WITHDRAW -> {
                if (walletEntity.getBalance().stripTrailingZeros().compareTo(operationRequest.getAmount().stripTrailingZeros()) < 0) {
                    System.out.println("[DEBUG] Баланс: " + walletEntity.getBalance() + ", запрошено: " + operationRequest.getAmount());
                    throw new NotEnoughMoneyException("Not enough money for withdraw");
                }else {
                walletEntity.setBalance(walletEntity.getBalance().subtract(operationRequest.getAmount()));
            }
                }
        }
        try {
            WalletEntity savedWalletEntity = walletRepository.save(walletEntity);
            return new WalletResponse(savedWalletEntity.getWalletId(), savedWalletEntity.getBalance());
        } catch (OptimisticLockingFailureException | OptimisticLockException e) {
            throw new RuntimeException("Concurrency error");
        }
    }


    @Override
    public WalletResponse getBalance(Long walletId) {
        WalletEntity walletEntity = walletRepository.findById(walletId).orElseThrow(() ->
                new WalletNotFoundException("wallet not found , try again"));
        return new WalletResponse(walletEntity.getWalletId(), walletEntity.getBalance());
    }
}
