package cn.edu.zju;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);
    private static final AppConfig instance = new AppConfig();

    public static AppConfig getInstance() {
        return instance;
    }

    public AppConfig() {
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties");
            Properties properties = new Properties();
            try {
                properties.load(resourceAsStream);
                this.jdbcUrl = properties.getProperty("jdbc.url");
                this.jdbcUsername = properties.getProperty("jdbc.username");
                this.jdbcPassword = properties.getProperty("jdbc.password");
                this.aiEnabled = Boolean.parseBoolean(properties.getProperty("ai.enabled", "false"));
                this.aiBaseUrl = properties.getProperty("ai.base.url");
                this.aiAuthToken = properties.getProperty("ai.auth.token");
                this.aiModel = properties.getProperty("ai.model", "qwen3-coder-plus");
                this.geminiApiKey = properties.getProperty("gemini.api.key");
                this.geminiModel = properties.getProperty("gemini.model", "gemini-2.5-flash");
            } catch (IOException e) {
                log.info("", e);
            }
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    log.info("", e);
                }
            }
        }
    }

    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;
    private boolean aiEnabled;
    private String aiBaseUrl;
    private String aiAuthToken;
    private String aiModel;
    private String geminiApiKey;
    private String geminiModel;

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUsername() {
        return jdbcUsername;
    }

    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public boolean isAiEnabled() {
        String envEnabled = System.getenv("AI_ENABLED");
        if (envEnabled != null) {
            return Boolean.parseBoolean(envEnabled);
        }
        return aiEnabled;
    }

    public String getAiBaseUrl() {
        String env = System.getenv("ANTHROPIC_BASE_URL");
        return env != null && !env.isBlank() ? env : aiBaseUrl;
    }

    public String getAiAuthToken() {
        String env = System.getenv("ANTHROPIC_AUTH_TOKEN");
        return env != null && !env.isBlank() ? env : aiAuthToken;
    }

    public String getAiModel() {
        String env = System.getenv("ANTHROPIC_MODEL");
        return env != null && !env.isBlank() ? env : aiModel;
    }

    public String getGeminiApiKey() {
        String env = System.getenv("GEMINI_API_KEY");
        if (env != null && !env.isBlank()) {
            return env;
        }
        String googleEnv = System.getenv("GOOGLE_API_KEY");
        return googleEnv != null && !googleEnv.isBlank() ? googleEnv : geminiApiKey;
    }

    public String getGeminiModel() {
        String env = System.getenv("GEMINI_MODEL");
        return env != null && !env.isBlank() ? env : geminiModel;
    }
}
