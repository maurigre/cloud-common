
# Cloud Integration Library

[![build status](https://img.shields.io/github/actions/workflow/status/maurigre/cloud-common/maven.yml?branch=main&label=build&color=18BA91&style=flat-square)](https://github.com/maurigre/cloud-common/actions/workflows/maven.yml)
[![release](https://img.shields.io/github/v/release/maurigre/cloud-common?label=release&color=18BA91&style=flat-square)](https://github.com/maurigre/cloud-common/releases/latest)
![downloads](https://img.shields.io/github/downloads/maurigre/cloud-common/total?color=18BA91&style=flat-square)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?color=18BA91&style=flat-square)](CONTRIBUTING.md)

This project, developed in **Java 21**, implements a library that facilitates the development of integrations with cloud services, using custom annotations. It provides a simplified interface for communication with **Google Pub/Sub** and implements retry mechanisms for **Feign** calls, optimizing the reliability and robustness of applications.

## Features

### Pub/Sub
- **@PubSubListener**: Facilitates the creation of listeners for Google Pub/Sub topics, processing messages asynchronously and with support for custom error handling.
- **PubSubListenerErrorHandler**: Free interface to implement a handler that handles and redirects failed messages, allowing discarding, retrying or sending to secondary queues.

### Feign Retry
- **@FeignRetry**: Adds automatic support for resending attempts in API calls using Feign, with flexible backoff configuration and maximum number of attempts.
- **Exponential Backoff Support**: Allows the configuration of retry strategies with exponential backoff, avoiding request overloads in external APIs.

## Requirements

- **Java 21**: This project requires Java 21 to run.
- **Spring Boot**: This project was developed on top of v3.4.0 of Spring Boot.
- **Spring Cloud**: This project was developed on top of v2024.0.0 of Spring Cloud.
- **Google Cloud**: This project was developed on top of v26.51.0 of Google Cloud.
- **Maven**: Used as a build system to manage dependencies and plugins.

## Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/maurigre/cloud-common.git
    ```

2. Navigate to the project directory:

    ```bash
    cd cloud-common
    ```

3. Build the project:

    ```bash
    mvn clean install
    ```

## Configuration

### Maven
Add the following dependencies to your `pom.xml`:

```xml
<dependencies>
    <!-- Feign and Pub/Sub -->
    <dependency>
        <groupId>io.github.maurigre.cloud</groupId>
        <artifactId>common-bom</artifactId>
        <version>1.0.0</version>
    </dependency>

    <!-- Feign -->
    <dependency>
        <groupId>io.github.maurigre.cloud</groupId>
        <artifactId>feign-retry-annotation</artifactId>
        <version>1.0.0</version>
    </dependency>

    <!-- Pub/Sub -->
    <dependency>
        <groupId>io.github.maurigre.cloud</groupId>
        <artifactId>pubsub-annotation</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

Make sure you are using **Java 21** in your Maven build configuration:

```xml
<properties>
    <java.version>21</java.version>
</properties>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.10.1</version>
            <configuration>
                <source>${java.version}</source>
                <target>${java.version}</source>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## Usage

### Pub/Sub Listener

To create a listener for Pub/Sub, use the `@PubSubListener` annotation on the method that will process the messages. Example:

```java
@PubSubListener(subscription = "my-subscription", errorHandler = "MyPubSubErrorHandler")
public void onMessage(PubsubMessage message) {
    // Processa a mensagem recebida
    log.info("Mensagem recebida: " + message.getData().toStringUtf8());
}
```

### Feign Retry

#### 1. Setting values directly in code

```java
@FeignClient(name = "my-service")
public interface MyFeignClient {

    @FeignRetry(maxAttempts = 5, backoff = @Backoff(delay = 2000, multiplier = 2))
    @GetMapping("/api/resource")
    ResponseEntity<Resource> getResource();
}
```

#### 2. Using variables configured in `application.yml`

```java
@FeignClient(name = "my-service")
public interface MyFeignClient {

    @FeignRetry(maxAttempts = "${feign.retry.max-attempts}",
        backoff = @Backoff(
            delay = "${feign.retry.backoff.delay}",
            maxDelay = "${feign.retry.backoff.max-delay}",
            multiplier = "${feign.retry.backoff.multiplier}"))
    @GetMapping("/api/resource")
    ResponseEntity<Resource> getResource();
}
```

### Suggested Retry configuration via `application.yml`

```yaml
feign:
    retry:
        max-attempts: 5
    backoff:
        delay: 2000
        max-delay: 10000
        multiplier: 2
```

## Error Handling in Pub/Sub

Implement the error handler to manage messages that failed to process:

```java
@Component(value = "pubSubErrorHandler")
public class PubSubErrorHandler implements PubSubListenerErrorHandler {

    @Override
    public void handleError(PubsubMessage message, Throwable throwable) {
        // Lógica de tratamento de erro
        log.error("Erro ao processar mensagem: " + message.getMessageId(), throwable);
        // Enviar para fila de Dead Letter, descartar ou tentar novamente
    }
}
```
## Contribution

Contributions are welcome! To contribute:

1. Fork the project.
2. Create a branch with your feature: `git checkout -b my-feature`.
3. Commit your changes: `git commit -m 'My new feature'`.
4. Push to the remote repository: `git push origin my-feature`.
5. Open a Pull Request.

## License

This project is licensed under the [MIT License](LICENSE).

## Contact

For questions or suggestions, please contact [maurigre@gmail.com](mailto:maurigre@gmail.com).
