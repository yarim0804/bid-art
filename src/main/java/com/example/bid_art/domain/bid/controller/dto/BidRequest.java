package com.example.bid_art.domain.bid.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BidRequest {

    @NotBlank(message = "입찰자 이름은 필수입니다.")
    private String bidderName;

    @Min(value = 1, message = "입찰 금액을 입력해주세요.")
    private int amount;
}
