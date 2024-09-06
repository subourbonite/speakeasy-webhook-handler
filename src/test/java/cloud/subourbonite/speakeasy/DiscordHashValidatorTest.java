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
    void testSignatureValidation() {
        DiscordHashValidator validator = new DiscordHashValidator("", "",
                HexFormat.of().formatHex(keyPair.getPublic().getEncoded()));

        Long timestamp = new Date().getTime();

        String body = "{\r\n" + //
                "    \"app_permissions\": \"562949953601536\",\r\n" + //
                "    \"application_id\": \"1276733792542916608\",\r\n" + //
                "    \"authorizing_integration_owners\": {},\r\n" + //
                "    \"entitlements\": [],\r\n" + //
                "    \"id\": \"1281449795176628325\",\r\n" + //
                "    \"token\": \"aW50ZXJhY3Rpb246MTI4MTQ0OTc5NTE3NjYyODMyNTprSTJXNG9Rd0FSQ0hQbGxBRDJLSFA0V2s1TjRJbjhKa1plUjQ0T1F2aFd4MEVybmdWT1cyNnc4NFJLcllCR1NGcFVDbG9VQjZhWnBuZHNZeVE3V0g1cGZhTThlM3lkaHhScDVnOW5jeTlXd0hMd0puRTRQNUxlaUQ3ak9TRGt2cQ\",\r\n"
                + //
                "    \"type\": 1,\r\n" + //
                "    \"user\": {\r\n" + //
                "        \"avatar\": \"c6a249645d46209f337279cd2ca998c7\",\r\n" + //
                "        \"avatar_decoration_data\": null,\r\n" + //
                "        \"bot\": true,\r\n" + //
                "        \"clan\": null,\r\n" + //
                "        \"discriminator\": \"0000\",\r\n" + //
                "        \"global_name\": \"Discord\",\r\n" + //
                "        \"id\": \"643945264868098049\",\r\n" + //
                "        \"public_flags\": 1,\r\n" + //
                "        \"system\": true,\r\n" + //
                "        \"username\": \"discord\"\r\n" + //
                "    },\r\n" + //
                "    \"version\": 1\r\n" + //
                "}";

        APIGatewayV2HTTPResponse response = validator.handleRequest(
                APIGatewayV2HTTPEvent.builder()
                        .withBody(body)
                        .withHeaders(Map.of("x-signature-timestamp", timestamp.toString(), "x-signature-ed25519",
                                HexFormat.of()
                                        .formatHex(Crypto.sign(keyPair.getPrivate(),
                                                (timestamp.toString() + body).getBytes()))))
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
        assertEquals(response.getBody(), "{ \"type\": 1 }");
    }
}