
## Examples on how to run/test through gradle

    ./gradlew run -Pargs="health-check"
    ./gradlew run -Pargs="users --start=0 --count=10"

## Generating the CLI application

    ./gradlew installDist
    
## Running it     

    ./build/install/control-center/bin/control-center users --start=0 --count=10
