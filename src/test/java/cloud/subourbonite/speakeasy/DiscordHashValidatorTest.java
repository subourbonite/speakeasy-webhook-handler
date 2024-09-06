package cloud.subourbonite.speakeasy;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

import software.pando.crypto.nacl.Crypto;

import java.util.Map;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscordHashValidatorTest {
    KeyPair keyPair = Crypto.signingKeyPair();

    @Test
    void testSignatureValidation() throws IOException, URISyntaxException {
        DiscordHashValidator validator = new DiscordHashValidator("", "",
                "7d36900458a5bdcc92dbcb5a17fd1099bed501d6454c2b694d5c033139ece1ad");

        String body = Files.readString(Paths.get(Thread.currentThread().getContextClassLoader()
                .getResource("DiscordHashValidationPayload.json").toURI()));

        APIGatewayV2HTTPResponse response = validator.handleRequest(
                APIGatewayV2HTTPEvent.builder()
                        .withBody(body)
                        .withIsBase64Encoded(true)
                        .withHeaders(Map.of("x-signature-timestamp", "1725595486", "x-signature-ed25519",
                                "deb413186162d80ded5ab181ab0f57e7c6519e2355dcef7e1cd5a29dde3da4af705ec88134337968c4c3125e63def094bc2ae6c6b497439f48557da158ce0c05"))
                        .build(),
                new Context() {
                    @Override
                    public String getAwsRequestId() {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'getAwsRequestId'");
                    }

                    @Override
                    public String getLogGroupName() {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'getLogGroupName'");
                    }

                    @Override
                    public String getLogStreamName() {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'getLogStreamName'");
                    }

                    @Override
                    public String getFunctionName() {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'getFunctionName'");
                    }

                    @Override
                    public String getFunctionVersion() {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'getFunctionVersion'");
                    }

                    @Override
                    public String getInvokedFunctionArn() {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'getInvokedFunctionArn'");
                    }

                    @Override
                    public CognitoIdentity getIdentity() {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'getIdentity'");
                    }

                    @Override
                    public ClientContext getClientContext() {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'getClientContext'");
                    }

                    @Override
                    public int getRemainingTimeInMillis() {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'getRemainingTimeInMillis'");
                    }

                    @Override
                    public int getMemoryLimitInMB() {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'getMemoryLimitInMB'");
                    }

                    @Override
                    public LambdaLogger getLogger() {
                        return new LambdaLogger() {
                            private static final Logger logger = LoggerFactory
                                    .getLogger(DiscordHashValidatorTest.class);

                            public void log(String message) {
                                logger.info(message);
                            }

                            public void log(byte[] message) {
                                logger.info(new String(message));
                            }
                        };
                    }
                });

        assertEquals(200, response.getStatusCode());
        assertEquals("{ \"type\": 1 }", response.getBody());
    }
}