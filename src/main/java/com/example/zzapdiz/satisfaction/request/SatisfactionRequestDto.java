package com.example.zzapdiz.satisfaction.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SatisfactionRequestDto {
    private Long projectId;
    private String satisfactionContent;
    private Integer satisfactionStarRate;
}
