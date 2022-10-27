# Tenit Feature Flags Java Client

## Maven
Add this repository to your maven dependency file:
```xml
  <repositories>
    <repository>
      <id>tenit-public-maven-snapshot</id>
      <url>https://storage.googleapis.com/maven.tenitx.com/public/snapshot</url>
    </repository>
  </repositories>
```

Then add the dependency:
```xml
<dependency>
  <groupId>com.tenitx</groupId>
  <artifactId>FeaturesClient</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

## Usage

```java
FeaturesClient featuresClient = new FeaturesClient(
  FeaturesClientConfig
    .builder()
    .withApiToken("your-api-token")
    // Default is to cache the response for no time.
    .withCacheTime(Duration.ofMinutes(1))
    .build()
);
boolean isEnabled = featuresClient.isEnabled("my-feature", "abc-123");
```

### FeaturesClientConfig
The input object when creating the client has some fields that allow for customizing the client experience.

| Field     | Description                                                                                                                                                                                                             | Required         |
|-----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------|
| apiToken  | An api token with permission to read feature flags. Find or create your token here: [API Tokens Page](https://app.tenitx.com/features/api-tokens)                                                                       | Yes              |
| cacheTime | A Duration value of how long the client should cache whether a user has a feature enabled for. This is an optimization feature that is not required, and we generally only recommend caching for a few minutes at most. | No (Default: 0s) |

## Tenit Features
Not yet using Tenit Features to manage feature flags in your apps? [Learn more about it here.](https://tenitx.com/feature-flags?utm_source=github&utm_content=features_java_client)