package org.example.walletapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WalletResponse {
    private Long walletId;
    private BigDecimal balance;
}
