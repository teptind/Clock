package clock

import java.time.Instant
import java.time.temporal.TemporalAmount

class MockClock(private var curTime: Instant = Instant.now()) : Clock {
    override fun instant(): Instant = curTime

    fun add(amount: TemporalAmount) {
        curTime = curTime.plus(amount)
    }
}