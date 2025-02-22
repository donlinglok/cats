package com.endava.cats.dsl;

import com.endava.cats.dsl.api.Parser;
import com.endava.cats.dsl.impl.AuthScriptProviderParser;
import com.endava.cats.dsl.impl.EnvVariableParser;
import com.endava.cats.dsl.impl.RequestVariableParser;
import com.endava.cats.dsl.impl.SpringELParser;
import io.github.ludovicianul.prettylogger.PrettyLogger;
import io.github.ludovicianul.prettylogger.PrettyLoggerFactory;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;

@Singleton
public class CatsDSLParser {

    private final PrettyLogger logger = PrettyLoggerFactory.getLogger(CatsDSLParser.class);
    private static final Map<String, Parser> PARSERS = Map.of(
            "T(", new SpringELParser(),
            "$$", new EnvVariableParser(),
            "$request", new RequestVariableParser(),
            "auth_script", new AuthScriptProviderParser());

    /**
     * Gets the appropriate parser based on the {@code valueFromFile} and runs it against the given payload.
     *
     * @param valueFromFile the expression retrieved from the CATS files
     * @param context       a given context: JSON request or response, a Map of values
     * @return the result after the appropriate parser runs
     */
    public String parseAndGetResult(String valueFromFile, Map<String, String> context) {
        Optional<Map.Entry<String, Parser>> parserEntry = PARSERS.entrySet().stream().filter(entry -> valueFromFile.startsWith(entry.getKey())).findAny();
        if (parserEntry.isPresent()) {
            logger.debug("Identified parser {}", parserEntry.get().getValue().getClass().getSimpleName());
            return parserEntry.get().getValue().parse(valueFromFile, context);
        }

        return valueFromFile;
    }
}
