package cloud.subourbonite.speakeasy;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

import software.pando.crypto.nacl.Crypto;

public class DiscordHashValidator implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private final String appId;
    private final String discordBotToken;
    private final String discordPublicKey;

    protected DiscordHashValidator(String appId, String discordBotToken, String discordPublicKey) {
        this.appId = appId;
        this.discordBotToken = discordBotToken;
        this.discordPublicKey = discordPublicKey;
    }

    public DiscordHashValidator() {
        this(System.getenv("DISCORD_APP_ID"), System.getenv("DISCORD_BOT_TOKEN"), System.getenv("DISCORD_PUBLIC_KEY"));
    }

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent input, Context context) {
        context.getLogger().log("Input: " + input);
        
        if (verifySignature(input, discordPublicKey)) {
            return APIGatewayV2HTTPResponse.builder().withBody("{ \"type\": 1 }").withStatusCode(200).build();
        } else {
            return APIGatewayV2HTTPResponse.builder().withStatusCode(401).build();
        }
    }

    public boolean verifySignature(APIGatewayV2HTTPEvent input, String key) {
        String body;
            
        if(input.getIsBase64Encoded()) {
            body = new String(Base64.getDecoder().decode(input.getBody()), StandardCharsets.UTF_8);
        } else {
            body = input.getBody();
        }

        return Crypto.signVerify(
                Crypto.signingPublicKey(HexFormat.of().parseHex(key)),
                (input.getHeaders().get("x-signature-timestamp") + body).getBytes(StandardCharsets.UTF_8),
                HexFormat.of().parseHex(input.getHeaders().get("x-signature-ed25519")));
    }
}