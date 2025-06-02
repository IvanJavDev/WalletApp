package org.example.walletapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.walletapp.dto.OperationRequest;
import org.example.walletapp.dto.OperationType;
import org.example.walletapp.dto.WalletResponse;
import org.example.walletapp.exception.WalletNotFoundException;
import org.example.walletapp.service.WalletServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WalletServiceImp walletService;

    private Long testWalletId = 1L;

    @Test
    void getWallet_ShouldReturnBalance() throws Exception {
        WalletResponse expectedResponse = new WalletResponse(1L, new BigDecimal("1000.00"));
        given(walletService.getBalance(testWalletId)).willReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/wallet/" + testWalletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(testWalletId))
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    void operate_ShouldDepositFunds() throws Exception {
        OperationRequest request = new OperationRequest(
                testWalletId,
                OperationType.DEPOSIT,
                new BigDecimal("500.00")
        );
        WalletResponse expectedResponse = new WalletResponse(1L, new BigDecimal("1500.00"));
        given(walletService.operate(request)).willReturn(expectedResponse);
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(testWalletId))
                .andExpect(jsonPath("$.balance").value(1500.00));
    }

    @Test
    void operate_ShouldWithdrawFunds() throws Exception {
        OperationRequest request = new OperationRequest(
                testWalletId,
                OperationType.WITHDRAW,
                new BigDecimal("200.00")
        );
        WalletResponse expectedResponse = new WalletResponse(1L, new BigDecimal("1300.00"));
        given(walletService.operate(request)).willReturn(expectedResponse);
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(testWalletId))
                .andExpect(jsonPath("$.balance").value(1300.00));
    }


    @Test
    void getWallet_ShouldReturnNotFound_ForNonExistingWallet() throws Exception {
        Long nonExistingWalletId = 999999L;
        given(walletService.getBalance(nonExistingWalletId))
                .willThrow(new WalletNotFoundException("Wallet not found"));
        mockMvc.perform(get("/api/v1/wallet/" + nonExistingWalletId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof WalletNotFoundException))
                .andExpect(result -> assertEquals("Wallet not found", result.getResolvedException().getMessage()));
    }
}
