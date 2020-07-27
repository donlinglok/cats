package com.endava.cats.fuzzer.fields;

import com.endava.cats.generator.simple.StringGenerator;
import com.endava.cats.io.ServiceCaller;
import com.endava.cats.report.TestCaseListener;
import com.endava.cats.util.CatsUtil;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class StringFieldsRightBoundaryFuzzer extends BaseBoundaryFieldFuzzer {


    @Autowired
    public StringFieldsRightBoundaryFuzzer(ServiceCaller sc, TestCaseListener lr, CatsUtil cu) {
        super(sc, lr, cu);
    }

    @Override
    protected List<Class<? extends Schema<?>>> getSchemasThatTheFuzzerWillApplyTo() {
        return Collections.singletonList(StringSchema.class);
    }

    @Override
    protected String getBoundaryValue(Schema<?> schema) {
        return StringGenerator.generateRightBoundString(schema);
    }

    @Override
    protected boolean hasBoundaryDefined(Schema<?> schema) {
        return schema.getMaxLength() != null;
    }


    @Override
    public String description() {
        return "iterate through each String field and send requests with outside the range values on the right side in the targeted field";
    }
}