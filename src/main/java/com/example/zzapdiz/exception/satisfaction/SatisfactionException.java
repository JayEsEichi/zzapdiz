package com.example.zzapdiz.exception.satisfaction;

import org.springframework.stereotype.Component;

@Component
public class SatisfactionException implements SatisfactionExceptionInterface{

    // 별점 0점은 불가능
    @Override
    public Boolean checkStarRate(int starRate) {

        if(starRate == 0){
            return true;
        }

        return false;
    }
}
