package io.dsub.discogs.api.config;

import io.dsub.discogs.api.Constants;
import io.dsub.discogs.api.core.exception.EnvironmentVariableException;
import io.dsub.discogs.api.core.exception.MissingRequiredEnvironmentVariableException;
import io.dsub.discogs.api.core.util.StringUtil;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;

import static io.dsub.discogs.api.Constants.*;

@Getter
public class DatabaseProperties implements InitializingBean {

    private final String username;
    private final String password;
    private final String url;

    public DatabaseProperties(Environment env) {
        username = readEncodedEnvironmentVariable(env, DB_USER_ENV_KEY);
        password = readEncodedEnvironmentVariable(env, DB_PASS_ENV_KEY);
        url = env.getProperty(DB_URL_ENV_KEY);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        validateUsername();
        validateUrl();
    }

    private void validateUsername() throws EnvironmentVariableException {
        if (this.username == null || this.username.isBlank()) {
            throw new MissingRequiredEnvironmentVariableException(DB_USER_ENV_KEY);
        }
    }

    private void validateUrl() throws EnvironmentVariableException {
        if (this.url == null || this.url.isBlank()) {
            throw new MissingRequiredEnvironmentVariableException(Constants.DB_URL_ENV_KEY);
        }
    }

    private String readEncodedEnvironmentVariable(Environment env, String key) {
        String value = env.getProperty(key);
        return StringUtil.encodeToUTF8(value);
    }
}
