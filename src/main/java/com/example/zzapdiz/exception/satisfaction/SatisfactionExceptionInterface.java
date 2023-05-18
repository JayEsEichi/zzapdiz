package com.example.zzapdiz.exception.satisfaction;

import org.springframework.stereotype.Component;

@Component
public interface SatisfactionExceptionInterface {

    /** 별점 0점은 불가능 **/
    Boolean checkStarRate(int starRate);

}
