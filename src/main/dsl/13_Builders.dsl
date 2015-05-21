send status.withSchedule(to: me) {
    schedule(period: 5.seconds, exactly: 2.times)
    schedule(period: 30.seconds, exactly: 5.times)
}