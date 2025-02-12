package com.endava.cats.generator.format.impl;

import com.endava.cats.generator.format.api.ValidDataFormatGenerator;
import io.swagger.v3.oas.models.media.Schema;

import javax.inject.Singleton;
import java.time.Period;
import java.util.Random;

@Singleton
public class PeriodGenerator implements ValidDataFormatGenerator {
    private final Random random = new Random();

    @Override
    public Object generate(Schema<?> schema) {
        return Period.of(random.nextInt(30), random.nextInt(26), random.nextInt(22));
    }

    @Override
    public boolean appliesTo(String format, String propertyName) {
        return "period".equalsIgnoreCase(format);
    }
}
