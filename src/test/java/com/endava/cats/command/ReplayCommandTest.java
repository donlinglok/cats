package com.endava.cats.command;

import com.endava.cats.io.ServiceCaller;
import com.endava.cats.model.CatsResponse;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

@QuarkusTest
class ReplayCommandTest {

    @InjectMock
    private ServiceCaller serviceCaller;

    private ReplayCommand replayCommand;

    @BeforeEach
    public void setup() {
        replayCommand = new ReplayCommand(serviceCaller);
    }

    @Test
    void shouldNotExecuteIfTestCasesNotSupplied() {
        replayCommand.tests = Collections.emptyList();

        replayCommand.run();
        Mockito.verifyNoInteractions(serviceCaller);
    }

    @Test
    void shouldExecuteIfTestCasesSupplied() throws Exception {
        replayCommand.tests = List.of("src/test/resources/Test12.json");
        Mockito.when(serviceCaller.callService(Mockito.any(), Mockito.anySet())).thenReturn(Mockito.mock(CatsResponse.class));
        replayCommand.run();
        Mockito.verify(serviceCaller, Mockito.times(1)).callService(Mockito.any(), Mockito.eq(Collections.emptySet()));
    }

    @Test
    void shouldThrowExceptionWhenTestCasesInvalid() {
        replayCommand.tests = List.of("Test12");
        replayCommand.run();

        Mockito.verifyNoInteractions(serviceCaller);
    }
}