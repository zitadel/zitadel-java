## ZITADEL Example Project with Spring Boot and Spring Security

This example contains two Spring Boot Apps (_api_ and _app_) which use the [ZITADEL](https://zitadel.ch/) SaaS identity provider as OpenID Provider.

- The app _web_ uses the internal OAuth2 access token (opaque token) provided by ZITADEL to access the _api_.
- The _api_ acts as an OAuth2 resource server.

# Features

- OpenID Connect based Login
- Logout support via OpenID Connect end session endpoint
- Access Token Relay
- Opaque Reference Tokens and Token Introspection

# Applications

## API

The Spring Boot app _api_ is configured as an API in ZITADEL and uses the Spring Security Resource Server support.

Base URL: http://localhost:18090

## Web

The Spring Boot app _web_ is configured as confidential Web App and OpenID Connect client in ZITADEL and uses the Spring Security OAuth2 client library
for authentication.

Base URL: `http://localhost:18080/webapp`
Redirect URL: `http://localhost:18080/webapp/login/oauth2/code/zitadel`
Post Logout URL: `http://localhost:18080/webapp`

# Build

```
mvn clean package -DskipTests
```

# Configuration

Before you can run your applications _api_ and _web_, you need a client ID and a client secret from ZITADEL, respectively.

## ZITADEL

![Client Configurations](/java/spring-boot/spring-boot-zitadel-config.png)

![API App Configurations](/java/spring-boot/spring-boot-zitadel-config-api-app.png)

![Web App Configurations](/java/spring-boot/spring-boot-zitadel-config-web-app2.png)

## Spring

The _api_ application requires the following JVM Properties to be configured:

```
-Dspring.security.oauth2.resourceserver.opaquetoken.client-id=...api-client-id
-Dspring.security.oauth2.resourceserver.opaquetoken.client-secret=...api-client-secret
```

The _web_ application requires the following JVM Properties to be configured:

```
-Dspring.security.oauth2.client.registration.zitadel.client-id=...web-client-id
-Dspring.security.oauth2.client.registration.zitadel.client-secret=...web-client-secret
```

# Run

```bash
# Run the api application in one terminal
java \
  -Dspring.security.oauth2.resourceserver.opaquetoken.client-id=<see configuration below> \
  -Dspring.security.oauth2.resourceserver.opaquetoken.client-secret=<see configuration below> \
  -jar api/target/api-0.0.1-SNAPSHOT.jar
```

```bash
# Run the web application in another terminal
java \
  -Dspring.security.oauth2.client.registration.zitadel.client-id=<see configuration below> \
  -Dspring.security.oauth2.client.registration.zitadel.client-secret=<see configuration below> \
  -jar web/target/web-0.0.1-SNAPSHOT.jar
```

Open your browser and navigate to http://localhost:18080/webapp/

# Demo

![Demo](spring-boot-zitadel-demo.gif)

# Misc

- This example uses opaque reference tokens as access tokens
- For the sake of simplicity CSRF protection and https are disabled
- Note in order to allow `http://` URIs we need to enable the `development mode in the respective client configuration.
