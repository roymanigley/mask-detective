# Load Test

## run Load Test
    
    mvn gatling:test \
        -Dtarget.host=localhost \
        -Dtarget.port=8080 \
        -Dtest.total-users=200 \
        -Dtest.time-frame=1
    
> - `test.total-users` default 100
> - `test.time-frame` default 1 minute
>
## create a Simulation
> be aware that localhost maybe ignored to proxy your request

    mvn gatling:recorder
    