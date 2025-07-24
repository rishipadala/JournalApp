package com.rishipadala.journalApp.Service;


import com.rishipadala.journalApp.Entity.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class UserArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(User.builder().userName("Baji").password("Baji@123").build()),
                Arguments.of(User.builder().userName("Kazutora").password("Kazutora@123").build())
        );
    }
}
