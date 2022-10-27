package client;

import org.immutables.value.Value;

@Value.Immutable
@TenitStyle
public interface FeatureEnabledIF {
  boolean isEnabled();
}
