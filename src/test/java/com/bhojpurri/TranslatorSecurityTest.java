package com.bhojpurri;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.net.http.HttpClient;
import javax.net.ssl.SSLContext;

public class TranslatorSecurityTest {
    @Test public void missingAndPlaceholderKeysFailWithoutDisclosingValues() {
        for (String value : new String[] {null, "", "   ", "your_groq_api_key", "YOUR_GROQ_API_KEY_HERE"}) {
            try {
                Translator.requireApiKey("GROQ_API_KEY", value);
                fail("Expected missing configuration error");
            } catch (IllegalStateException expected) {
                assertEquals("GROQ_API_KEY is not configured. Set it in .env or the process environment.", expected.getMessage());
            }
        }
    }

    @Test public void configuredValuesAreTrimmed() {
        assertEquals("test-only-value", Translator.requireApiKey("OPENL_API_KEY", " test-only-value "));
    }

    @Test public void clientUsesDefaultTlsContext() throws Exception {
        Translator translator = new Translator();
        Field field = Translator.class.getDeclaredField("httpClient");
        field.setAccessible(true);
        assertSame(SSLContext.getDefault(), ((HttpClient) field.get(translator)).sslContext());
    }

    @Test public void emptyTranslationDoesNotNeedCredentialsOrNetwork() throws Exception {
        assertEquals("", new Translator().translateTo("  ", "bho"));
    }
}
