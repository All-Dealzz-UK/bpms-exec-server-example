# BPM Suite Execution Server Example

## Objectives

This example demonstrates a project authored in Eclipse (JBoss Developer Studio), built using the kie-maven-plugin, deployed to a remote Maven repository, and then deployed to an instance of BPM Suite running in "exec-server" mode via REST.

## Environment

Use of this example requires 
* an existing Maven repository `nexus.example.com`
* an instance of BPM Suite `as1.example.com`
* Maven for building and deploying the example project
* a REST client for interacting with `as1.example.com`. We will include cURL samples

__note:__ Due to https://bugzilla.redhat.com/show_bug.cgi?id=1127032 a BPM server running in exec-server mode needs to have a filter added to web.xml for the REST API to function properly. See BZ for details
```xml
<filter>
  <filter-name>Dynamic JAXBContext Filter</filter-name>
  <filter-class>org.kie.remote.services.rest.jaxb.DynamicJaxbContextFilter</filter-class>
</filter>
<filter-mapping>
  <filter-name>Dynamic JAXBContext Filter</filter-name>
  <url-pattern>/rest/*</url-pattern>
</filter-mapping>
```

### Maven Repository Configuration
#### On Your Laptop
The project in kie-assets contains a `distributionManagement` element in the pom.xml. This should be altered to point at your Maven repository.
```xml
<distributionManagement>
    <repository>
        <id>nexus.example.com</id>
        <url>http://nexus.example.com:8081/nexus/content/repositories/bpm</url>
    </repository>
</distributionManagement>
```

If the server requires authentication, you should provide this in your ~/.m2/settings.xml
```xml
<settings>
    <servers>
        <server>
            <id>nexus.example.com</id> <!-- Must match repo id in distributionManagement -->
            <username>USERNAME</username>
            <password>PASSWORD</password>
        </server>
    </servers>
    ...
</settings>
```
#### On The Server
The execution server will need to know about the Maven repository. This is best achieved by providing a custom settings.xml. In the servers `standalone.xml` add a system property.
```xml
<system-properties>
    <property name="kie.maven.settings.custom" value="/path/to/custom/settings.xml"/>
</system-properties>
```

In the location specified, add a settings.xml that points to the Maven repository.
```xml
<settings>
    <profiles>
        <profile>
            <id>bpm-nexus-profile</id>
            <repositories>
                <repository>
                    <id>nexus.example.com</id>
                    <url>http://nexus.example.com:8081/nexus/content/repositories/bpm</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </repository>
            </repositories>
        </profile>
    </profiles>

    <activeProfiles>
        <activeProfile>bpm-nexus-profile</activeProfile>
    </activeProfiles>
</settings>
```


## Running the Example
Build and deploy kie-assets into the Maven repository
`/kie-assets> mvn clean deploy`

Deploy kie-assets onto the BPM Suite server, using the appropriate admin credentials for that server
`>curl -X POST 'http://username:password@as1.example.com:8080/business-central/rest/deployment/com.example.bpm:kie-assets:1.0.3/deploy'`

### Using the remote Kie API
Under `/runtimemanager-rest-client`, customize RestClient.java with the correct url and admin credentials
```java
private static final String BPMS_USER = "username";
private static final String BPMS_PASSWORD = "password";
private static final String BPMS_HOST = "as1.example.com:8080";
private static final String DEPLOYMENT_ID = "com.example.bpm:kie-assets:1.0.3";
private static final String PROCESS_NAME = "sample-process";
```
Then run RestClient.java using either eclipse, or mvn exec:java

sample-process just writes a comment to System.out on the exec server. Check the logs to validate this.
`10:24:37,550 INFO  [stdout] (http-/0.0.0.0:8080-1) All Is Well User`

### Using the raw REST API
Under `/raw-rest-client`, customize RestClient.java with the correct url and admin credentials
```java
private static final String BPMS_USER = "username";
private static final String BPMS_PASSWORD = "password";
private static final String BPMS_HOST = "as1.example.com:8080";
private static final String DEPLOYMENT_ID = "com.example.bpm:kie-assets:1.0.3";
private static final String PROCESS_NAME = "sample-process";
```
Then run RestClient.java using either eclipse, or mvn exec:java

sample-process just writes a comment to System.out on the exec server. Check the logs to validate this.
`10:24:37,550 INFO  [stdout] (http-/0.0.0.0:8080-1) All Is Well User`


