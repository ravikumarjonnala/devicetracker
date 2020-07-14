# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/maven-plugin/reference/html/#build-image)

# Assignment Information
##### Launching using supplied jar
1. In case it is not needed to build the assignment, then the provided jar file can be used to start the embedded tomcat server and also launch the web application.
1. Download the jar file devicetracker-1.0.0.jar to a directory say, <code>C:\assignment</code>.
1. Open a command window and navigate to the directory containing the jar file.
1. Run the command <code>java -jar devicetracker-1.0.0.jar</code>.
1. Once the server starts up, open a browser and open the URL "http://localhost:8080/swagger-ui.html".
1. The dataset can be loaded using the API for POST operation and the device information can be retrieved using the GET operation.

##### Extracting the assignment
1. Unzip the delivered zip file into a <code>basedir</code> directory. Say, C:\assignment. You should now be seeing a directory <code>{basedir}\devicetracker</code>.

##### Running unit/integration tests
1. Ensure that the system property JAVA_HOME is set, pointing to the installed JDK home directory.
1. Open a command window. Navigate to the <code>{basedir}\devicetracker</code> directory.
1. Run the command <code>mvnw site</code>. Several exceptions will be logged to the console and this is expected. These are attributed to the unit tests for invalid values in the input dataset.
1. Test results will be available within <code>{basedir}\devicetracker\target\site</code> directory. Open index.html with your browser and navigate to 'Project Report -> Surefire Report' page.

##### Configuration prior to running the application
1. The application runs by default on port 8080. If this port is unavailable on the host, configure the property <code>server.port</code> in application.properties within <code>src/main/resources</code> directory.
1. The logging level for the application is set to INFO. If additional logs are needed, configure using the property <code>logging.level.root</code> setting it to DEBUG.

##### Building the Application
1. Open a command window and navigate to <code>{basedir}\devicetracker</code> directory.
1. Run the command <code>mvn clean install</code>. The jar file will be available within the target directory.

##### Running the Application
1. Open command prompt. Navigate to the directory where the source is extracted to (say, <code>{basedir}\devicetracker</code>).
1. Run the command <code>mvnw spring-boot:run</code>. On the first run, the dependencies would be downloaded and it might take slightly longer for the application to be available.
1. Have the input dataset (dataset.xlsm) available in a directory say, C:\datasetDir.
1. Open a browser and launch the Swagger API doc/test page http://localhost:8080/swagger-ui.html.
1. Expand the section device-information-controller to view the POST and GET operations in this application.
1. To use the device location retrieval API, it is first necessary to load the data using the POST operation.
1. Load the dataset mentioned in the previous steps using the 'Try it out' button and populating the payload appropriately.
1. Once the data load is successful, use the device location retrieval API using the GET operation.  

##### Assumptions on Input Dataset
1. The dataset to load the events from, is in excel format (I could not use the provided dataset as a CSV file - (a)line number three begins with some weird character while parsing and (b)precision in datetime column will be lost once I save the excel as a CSV text file).
1. The dataset file will have two rows at the top that are for human-readable purpose only.
1. The excel file will have the required data in the first sheet only.
1. The input dataset is not ordered.
1. The datetime column is in UTC

