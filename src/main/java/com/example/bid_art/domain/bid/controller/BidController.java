package com.example.bid_art.domain.bid.controller;

import com.example.bid_art.domain.bid.controller.dto.BidRequest;
import com.example.bid_art.domain.bid.controller.dto.BidResponse;
import com.example.bid_art.domain.bid.service.BidService;
import com.example.bid_art.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items/{itemId}/bids")
public class BidController {

    private final BidService bidService;

    @GetMapping
    public ApiResponse<List<BidResponse>> getBids(@PathVariable Long itemId) {
        return ApiResponse.success(bidService.getBids(itemId));
    }

    @PostMapping
    public ApiResponse<BidResponse> placeBid(@PathVariable Long itemId,
                                             @Valid @RequestBody BidRequest request) {
        return ApiResponse.success(bidService.placeBid(itemId, request));
    }
}
