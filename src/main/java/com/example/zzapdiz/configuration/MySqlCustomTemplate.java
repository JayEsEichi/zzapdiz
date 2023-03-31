package com.example.zzapdiz.configuration;

import com.querydsl.core.types.Ops;
import com.querydsl.jpa.JPQLTemplates;

public class MySqlCustomTemplate extends JPQLTemplates {

    public MySqlCustomTemplate(){
        this(DEFAULT_ESCAPE);
        add(Ops.MathOps.RANDOM, "random()");
        add(Ops.MathOps.RANDOM2, "random({0})");
    }

    private MySqlCustomTemplate(char escape){
        super(escape);
    }
}
