import java.util.concurrent.TimeUnit

period = TimeUnit.SECONDS.toMillis(10)
me << schedule(period, 1)
