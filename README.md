ZITADEL Example Project with Spring Boot and Spring Security
----

This example contains two Spring Boot Apps (*api* and *app*) which use the [ZITADEL](https://zitadel.ch/) SaaS identity provider as OpenID Provider.

- The app *web* uses the internal OAuth2 access token (opaque token) provided by ZITADEL to access the *api*.
- The *api* acts as an OAuth2 resource server.

# Features
- OpenID Connect based Login
- Logout support via OpenID Connect end session endpoint
- Access Token Relay
- Opaque Reference Tokens and Token Introspection

# Applications

## API

The Spring Boot app *api* is configured as an API in ZITADEL and uses the Spring Security Resource Server support.

Base URL: http://localhost:18090

## Web

The Spring Boot app *web* is configured as confidential Web App and OpenID Connect client in ZITADEL and uses the Spring Security OAuth2 client library
for authentication.

Base URL: `http://localhost:18080/webapp`
Redirect URL: `http://localhost:18080/webapp/login/oauth2/code/zitadel`
Post Logout URL: `http://localhost:18080/webapp`

# Build

```
mvn clean package -DskipTests
```

# Configuration

Before you can run your applications *api* and *web*, you need a client ID and a client secret from ZITADEL, respectively.

## ZITADEL

![Client Configurations](spring-boot-zitadel-config.png)

![API App Configurations](spring-boot-zitadel-config-api-app.png)

![Web App Configurations](spring-boot-zitadel-config-web-app2.png)

## Spring

The *api* application requires the following JVM Properties to be configured:
```
-Dspring.security.oauth2.resourceserver.opaquetoken.client-id=...api-client-id
-Dspring.security.oauth2.resourceserver.opaquetoken.client-secret=...api-client-secret
```

The *web* application requires the following JVM Properties to be configured:
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