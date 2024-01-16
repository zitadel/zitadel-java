## ZITADEL Example Project with Spring Boot and Spring Security

This example contains two Spring Boot Apps (_api_ and _app_) which use the [ZITADEL](https://zitadel.com/) SaaS identity provider as OpenID Provider.

- The app _web_ uses the internal OAuth2 access token (opaque token) provided by ZITADEL to access the _api_.
- The _api_ acts as an OAuth2 resource server.

# Features

- OpenID Connect based Login
- Logout support via OpenID Connect end session endpoint
- Access Token Relay
- Opaque Reference Tokens and Token Introspection

# Applications

To run the example you need to configure the applications in ZITADEL and provide the generated properties.
Please check out the full guides ([web](https://zitadel.com/docs/examples/login/java-spring) and [api](https://zitadel.com/docs/examples/secure-api/java-spring)) on this example as well.

## API

The Spring Boot app _api_ is configured as an API in ZITADEL and uses the Spring Security Resource Server support.

Base URL: http://localhost:18090

## Web

The Spring Boot app _web_ is configured as confidential Web App and OpenID Connect client in ZITADEL and uses the Spring Security OAuth2 client library
for authentication.

Base URL: `http://localhost:18080/webapp`

Redirect URI:
```
http://localhost:18080/webapp/login/oauth2/code/zitadel
```

Post Logout URL:
```
http://localhost:18080/webapp
```

# Build

```
mvn clean package -DskipTests
```

# Run

The _api_ application requires the following JVM Properties to be configured:
```bash
# Run the api application in one terminal
java \
  -Dspring.security.oauth2.resourceserver.opaquetoken.introspection-uri=<see configuration above> \
  -Dspring.security.oauth2.resourceserver.opaquetoken.client-id=<see configuration above> \
  -Dspring.security.oauth2.resourceserver.opaquetoken.client-secret=<see configuration above> \
  -jar api/target/api-0.0.2-SNAPSHOT.jar
```

The _web_ application requires the following JVM Properties to be configured:
```bash
# Run the web application in another terminal
java \
  -Dspring.security.oauth2.client.provider.zitadel.issuer-uri=<see configuration above> \
  -Dspring.security.oauth2.client.registration.zitadel.client-id=<see configuration above> \
  -jar web/target/web-0.0.2-SNAPSHOT.jar
```

Open your browser and navigate to http://localhost:18080/webapp/

# Misc

- This example uses opaque reference tokens as access tokens
- For the sake of simplicity CSRF protection and https are disabled
- Note in order to allow `http://` URIs we need to enable the `development mode in the respective client configuration.
