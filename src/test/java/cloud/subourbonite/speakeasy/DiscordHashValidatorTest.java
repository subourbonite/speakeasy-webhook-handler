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
import java.util.Date;
import java.util.HexFormat;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscordHashValidatorTest {
    KeyPair keyPair = Crypto.signingKeyPair();

    @Test
    void demoTestMethod() {
        DiscordHashValidator validator = new DiscordHashValidator("", "",
                HexFormat.of().formatHex(keyPair.getPublic().getEncoded()));

        Long timestamp = new Date().getTime();

        APIGatewayV2HTTPResponse response = validator.handleRequest(
                APIGatewayV2HTTPEvent.builder()
                        .withBody("{ \"type\": 1 }")
                        .withHeaders(Map.of("x-signature-timestamp", timestamp.toString(), "x-signature-ed25519", HexFormat.of().formatHex(Crypto.sign(keyPair.getPrivate(), (timestamp.toString() + "{ \"type\": 1 }").getBytes()))))
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

        assertEquals(response.getStatusCode(), 200);
    }
}