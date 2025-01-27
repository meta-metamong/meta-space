package com.metamong.mt.global.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MailMessaageFormatterTest {
    private static final String FORMAT = "{0}, {1}, {2}";

    static class MailMessageFormatterDummy extends MailMessageFormatter {

        @Override
        protected String getMessageFormat() {
            return FORMAT;
        }

        @Override
        public MailType getSupportedMailType() {
            return null;
        }
    }
    
    @ParameterizedTest
    @CsvSource(
            value = {
                    "Hello:World:Hi",
                    "13:15:16",
                    "Why:So:serious",
                    "Hello:13:JPA"
            },
            delimiter = ':'
    )
    @DisplayName("format() - 성공 케이스 테스트")
    void format_success(String first, String second, String third) {
        // Given
        MailMessageFormatter formatter = new MailMessageFormatterDummy();
        
        // When
        String result = formatter.format(parse(first), parse(second), parse(third));
        log.info("result={}", result);
        
        // Then
        String expected = first + ", " + second + ", " + third;
        log.info("expected={}", expected);
        assertThat(result).isEqualTo(expected);
    }
    
    private Object parse(String raw) {
        try {
            return Integer.valueOf(raw);
        } catch (NumberFormatException e) {
            return raw;
        }
    }
    
    static Stream<List<Object>> format_IllegalArgumentException_sinceArgumentCountNotMatchToFormat() {
        return Stream.of(
                List.of("1", 2, 3, "4" ),
                List.of("Hello", "World" ),
                List.of(),
                List.of("Why", "So", "Serious", "Hello"),
                List.of(new Object[] { 5, 6 })
        );
    }
    
    @ParameterizedTest
    @MethodSource
    @DisplayName("format() - 인자 개수가 맞지 않으면 실패")
    void format_IllegalArgumentException_sinceArgumentCountNotMatchToFormat(List<Object> arguments) {
        // Given
        MailMessageFormatter formatter = new MailMessageFormatterDummy();
        
        // When, Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> formatter.format(arguments.toArray()));
    }
}
