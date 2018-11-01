# README / DEMO

Based on the Red Hat Microsweeper demo at 
https://github.com/jamesfalkner/microsweeper-demo


LOCAL DEMO

Command prompts - Run locally:

cd C:\Users\bbenz\Documents\GitHub\microsweeper-demo-localjaeger

Added to pom.xml:

 <dependency>
          <groupId>io.thorntail</groupId>
          <artifactId>jaeger</artifactId>
</dependency>


mvn clean thorntail:run

OPen the dmeo at http://localhost:8080


docker run --rm -p5775:5775/udp -p6831:6831/udp -p6832:6832/udp -p5778:5778 -p16686:16686 -p14268:14268 --name=jaeger jaegertracing/all-in-one:latest

open Jaeger UI at http://localhost:16686


Play a few versions of the game.
Check out the Jaeger traces when scores are recorded.



Future steps: outline Thorntail docs with properties:

https://docs.thorntail.io/2.2.0.Final/#_jaeger

All THorntail Jaeger env vars:

https://github.com/thorntail/thorntail-examples/tree/master/jaeger/jaeger-servlet


# 3 places for Setting properties:

1) project properties at src/main/resources/project-defaults.yml 

2) Command line when running with java -jar 

Example: 

-Dswarm.jaeger.sampler-type=const

You can also put them in pom.xml:

Example: 

    <build>
        <finalName>demo</finalName>
        <plugins>
            <plugin>
                <groupId>io.thorntail</groupId>
                <artifactId>thorntail-maven-plugin</artifactId>
                <version>${version.thorntail}</version>
                <configuration>
                    <properties>
                        <java.net.preferIPv4Stack>true</java.net.preferIPv4Stack>
                        <swarm.bind.address>0.0.0.0</swarm.bind.address>
                        <swarm.jaeger.sample-type>const</swarm.jaeger.sample-type>
                    </properties>
                </configuration>

                <executions>
                    <execution>
                        <goals>
                            <goal>package</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>


If problems with commadn line demo shutdown - 
ctrl-alt-delete
task manager
more
scroll down to Java SE
kill task(s)