
## Prerequisites

1. Install MySQL (or MariaDB, etc).
2. Run the `./src/sql/softeng.sql` SQL script to create the example database (includes both structure and data).
3. Your MySQL instance must be accessible at the host:port with the user credentials described in `./src/main/resources/gr/uoa/di/softeng/app.properties`.

## Run server

    ./gradlew appRun

You can check that the server is up and running by visiting `http://localhost:9000/control-center/api/health-check`.

In the above URL, the port "9000" is defined in `./build.gradle` and the path "/control-center/api" in `./src/main/webapp/WEB-INF/web.xml`
