package com.example.bid_art.domain.item.controller;

import com.example.bid_art.domain.item.repository.Item;
import com.example.bid_art.domain.item.repository.ItemRepository;
import com.example.bid_art.domain.item.service.ItemService;
import com.example.bid_art.global.common.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/api/items")
    public ApiResponse<List<Item>> getItems() {
        return ApiResponse.success(itemService.findAllItems());
    }

    @PostMapping("/api/items")
    public ApiResponse<Item> saveItem(@Valid @RequestBody Item item) {
        return ApiResponse.success(itemService.saveItem(item));
    }

    @GetMapping("/api/items/{id}")
    public ApiResponse<Item> getItem(@PathVariable Long id) {
        return ApiResponse.success(itemService.findItemById(id));
    }
}