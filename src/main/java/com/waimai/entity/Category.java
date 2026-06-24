package com.waimai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waimai.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private Integer sortOrder = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurantId", insertable = false, updatable = false)
    @JsonIgnore
    private Restaurant restaurant;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Dish> dishes = new ArrayList<>();
}
