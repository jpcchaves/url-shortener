FROM azul/zulu-openjdk:17
VOLUME /tpm
ADD ./target/urlshortener-0.0.1.jar app.jar
EXPOSE 8080
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]