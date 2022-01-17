package statistics

import clock.Clock
import java.time.Instant
import java.util.ArrayDeque
import java.util.HashMap
import java.util.Queue

private const val MINUTES_IN_HOUR = 60

class EventStatisticImpl(private val clock: Clock) : EventStatistic {
    private val eventCounters: MutableMap<String, Int> = HashMap()
    private val eventsQueue: Queue<Pair<String, Instant>> = ArrayDeque()

    override fun incEvent(eventName: String) {
        val curTime = clock.instant()

        takeAndProcessUntil(clock.instant())

        eventsQueue.add(Pair(eventName, curTime))
        eventCounters.merge(eventName, 1, Int::plus)
    }

    override fun getEventStatisticByName(eventName: String): Double {
        takeAndProcessUntil(clock.instant())

        return (eventCounters[eventName]?.toDouble() ?: 0.0) / MINUTES_IN_HOUR
    }

    override fun getAllEventStatistic(): Map<String, Double> {
        takeAndProcessUntil(clock.instant())

        return eventCounters.mapValues { it.value.toDouble() / MINUTES_IN_HOUR }
    }

    override fun printStatistic() {
        println(getAllEventStatistic())
    }

    private fun takeAndProcessUntil(bound: Instant) {
        while (!eventsQueue.isEmpty() && eventsQueue.peek().second.isBefore(bound)) {
            val eventName = eventsQueue.poll().first

            eventCounters.computeIfPresent(eventName) { _, curCnt ->
                if (curCnt > 1) curCnt - 1 else null
            }
        }
    }
}