package sendaTrader76.bot.config;

import com.oanda.v20.Context;
import com.oanda.v20.account.AccountID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OandaConfig {

    @Value("${oanda.api.url}")
    private String oandaApiUrl;

    @Value("${oanda.access.token}")
    private String oandaAccessToken;

    @Value("${oanda.account.id}")
    private String oandaAccountId;

    @Bean
    public Context oandaContext() {
        return new Context(oandaApiUrl, oandaAccessToken);
    }

    @Bean
    public AccountID oandaAccountId() {
        return new AccountID(oandaAccountId);
    }
}