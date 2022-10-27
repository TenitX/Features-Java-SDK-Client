package client;

import org.immutables.value.Value;

import java.time.Duration;

@Value.Immutable
@TenitStyle
public interface FeaturesClientConfigIF {
  String getApiToken();

  @Value.Default
  default Duration getCacheTime() {
    return Duration.ZERO;
  }
}
