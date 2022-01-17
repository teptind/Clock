import clock.MockClock
import statistics.EventStatisticImpl
import java.time.Duration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import statistics.EventStatistic


private const val EPS = 1e-4
private const val MINUTES_IN_HOUR = 60

class EventStatisticTest {

    private fun incEvent(statistics: EventStatistic, eventName: String, times: Int) {
        repeat(times) {
            statistics.incEvent(eventName)
        }
    }

    @Test
    fun `should return empty statistics when no events`() {
        val statistics = EventStatisticImpl(MockClock())

        assertTrue(statistics.getAllEventStatistic().isEmpty())
    }

    @Test
    fun `should return zero when non-existing name`() {
        val statistics = EventStatisticImpl(MockClock())

        statistics.incEvent("one")

        assertEquals(statistics.getEventStatisticByName("non-existing"), 0.0, EPS)
    }

    @Test
    fun `should count statistics by name correctly in a simple scenario`() {
        val statistics = EventStatisticImpl(MockClock())

        incEvent(statistics,"one", 1)
        incEvent(statistics, "two", 2)

        assertEquals(1.0 / MINUTES_IN_HOUR,statistics.getEventStatisticByName("one"), EPS)
        assertEquals(2.0 / MINUTES_IN_HOUR, statistics.getEventStatisticByName("two"), EPS)
    }

    @Test
    fun `should return all statistics correctly`() {
        val statistics = EventStatisticImpl(MockClock())

        incEvent(statistics,"one", 1)
        incEvent(statistics,"three", 3)

        val allStatistic = statistics.getAllEventStatistic()

        assertEquals(2, allStatistic.size)

        assertEquals(1.0 / MINUTES_IN_HOUR, allStatistic["one"]!!, EPS)
        assertEquals(3.0 / MINUTES_IN_HOUR, allStatistic["three"]!!, EPS)
    }

    @Test
    fun `complex small test`() {
        val clock = MockClock()
        val statistics = EventStatisticImpl(clock)

        incEvent(statistics,"KeyUp", 2)

        clock.add(Duration.ofHours(1))

        incEvent(statistics,"KeyDown", 2)

        assertEquals(0.0, statistics.getEventStatisticByName("KeyUp"))
        assertEquals(2.0 / MINUTES_IN_HOUR, statistics.getEventStatisticByName("KeyDown"))

        val allStatistic = statistics.getAllEventStatistic()

        assertEquals(1, allStatistic.size)
        assertEquals(2.0 / MINUTES_IN_HOUR, allStatistic["KeyDown"]!!, EPS)
    }

}