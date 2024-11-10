# Use uma imagem base do OpenJDK
FROM openjdk:21-jdk-slim

# Define o diretório de trabalho
WORKDIR /app

# Copie o arquivo JAR da aplicação para o contêiner
COPY target/jpa-0.0.1-SNAPSHOT.jar finance-api.jar


# Expõe a porta do servidor configurada no application.properties
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "finance-api.jar"]
