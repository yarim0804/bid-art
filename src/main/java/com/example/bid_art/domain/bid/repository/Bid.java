package com.example.bid_art.domain.bid.repository;

import com.example.bid_art.domain.item.repository.Item;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 입찰 대상 물품 (지연 로딩으로 불필요한 조회 방지)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @NotBlank(message = "입찰자 이름은 필수입니다.")
    private String bidderName;

    @Min(value = 0, message = "입찰 금액은 0원 이상이어야 합니다.")
    private int amount;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Bid(Item item, String bidderName, int amount) {
        this.item = item;
        this.bidderName = bidderName;
        this.amount = amount;
    }
}
