# Use the official Corretto-Alpine image to create a build artifact.
FROM docker.io/amazoncorretto:17-alpine3.18-jdk as builder

# Download and install Maven 3.9.4
ENV MAVEN_VERSION 3.9.4
RUN wget -q https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    && tar -xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt \
    && rm apache-maven-${MAVEN_VERSION}-bin.tar.gz
ENV PATH="/opt/apache-maven-${MAVEN_VERSION}/bin:${PATH}"

# Copy only the pom.xml to download dependencies
WORKDIR /usr/src/app
COPY pom.xml ./

# Download all dependencies. If the pom.xml hasn't changed, this layer will be cached
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build a release artifact
RUN mvn clean package -DskipTests

# Use the official Amazon Corretto image as the base. It's an OpenJDK distribution from Amazon
# This image uses Alpine Linux for a minimal footprint and includes Amazon Corretto 17 JDK.
FROM docker.io/amazoncorretto:17-alpine3.18-jdk

# Create a user group 'javauser' and user 'javauser'
# Create a user within the docker container (different than users on the EC2 instance)
RUN addgroup -S javagroup && adduser -S javauser -G javagroup

# Set the working directory and switch to the 'javauser' user
WORKDIR /usr/app
USER javauser

# Copy the jar file built in the first stage
COPY --from=builder /usr/src/app/target/app.jar ./app.jar

# Your application's port number
EXPOSE 8080

# Run your jar file
ENTRYPOINT ["java","-jar","./app.jar"]