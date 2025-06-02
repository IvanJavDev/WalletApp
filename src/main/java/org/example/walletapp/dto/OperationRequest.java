package org.example.walletapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationRequest {

    @NotNull
    private Long walletId;

    @NotNull
    private OperationType operationType;

    @NotNull
    private BigDecimal amount;

}
