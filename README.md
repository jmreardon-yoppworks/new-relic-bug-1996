This project demonstrates a bug in New Relic's Slick integration in Slick 3.5.0
or greater. The test scenario is based on a Slick sample and creates a simple
table in an H2 database, then runs 100 simultaneous queries, exceeding the
maximum connection count (default 20) of Slick's connection pool.

Some queries complete, but the operation then stalls. Running the same program
with the New Relic agent disabled completes normally

To run:

```sh
sbt run
```

To disable the New Relic agent, comment out the last line in build.sbt and run again.
