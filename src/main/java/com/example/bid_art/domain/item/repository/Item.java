package com.example.bid_art.domain.item.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "물품명은 필수입니다.")
    private String name;

    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private int price;

    public Item(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    /**
     * 입찰을 반영해 현재가(최고가)를 갱신한다.
     * 현재가보다 높은 금액만 허용한다.
     */
    public void placeBid(int amount) {
        if (amount <= this.price) {
            throw new IllegalArgumentException(
                    "입찰 금액은 현재가보다 높아야 합니다. 현재가=" + this.price + "원");
        }
        this.price = amount;
    }
}