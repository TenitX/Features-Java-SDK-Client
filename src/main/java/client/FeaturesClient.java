package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class FeaturesClient {
  private static final Logger LOG = LoggerFactory.getLogger(FeaturesClient.class);

  private final ObjectMapper objectMapper = new ObjectMapper();
  private static final String API_URL = "https://features.tenitx.com/%s/enabled?user=%s";
  private static Cache<String, FeatureEnabled> CACHE;
  private final OkHttpClient httpClient;

  private final FeaturesClientConfig config;

  public FeaturesClient(FeaturesClientConfig config) {
    this.httpClient = new OkHttpClient();
    this.config = config;
    CACHE =
      CacheBuilder
        .newBuilder()
        .recordStats()
        .concurrencyLevel(1)
        .expireAfterWrite(config.getCacheTime())
        .build();
  }

  public boolean isEnabled(String featureName, String user) {
    Optional<FeatureEnabled> maybeFeature = Optional.ofNullable(
      CACHE.getIfPresent(featureName + "-" + user)
    );
    return maybeFeature
      .map(FeatureEnabled::isEnabled)
      .orElseGet(
        () ->
          isEnabledUncached(featureName, user)
            .map(FeatureEnabled::isEnabled)
            .orElse(false)
      );
  }

  private Optional<FeatureEnabled> isEnabledUncached(String featureName, String user) {
    Optional<FeatureEnabled> maybeFeature = Optional.ofNullable(
      CACHE.getIfPresent(featureName)
    );
    if (maybeFeature.isPresent()) {
      return maybeFeature;
    }
    String url = String.format(API_URL, featureName, user);
    Request request = new Request.Builder()
      .url(url)
      .addHeader("x-tenit-api-token", config.getApiToken())
      .build();

    try (Response response = httpClient.newCall(request).execute()) {
      String r = new String(response.body().bytes());
      maybeFeature = Optional.ofNullable(objectMapper.readValue(r, FeatureEnabled.class));
      maybeFeature.ifPresent(f -> CACHE.put(featureName + "-" + user, f));
      return maybeFeature;
    } catch (IOException e) {
      return Optional.empty();
    }
  }
}
