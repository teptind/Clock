package statistics


interface EventStatistic {
    fun incEvent(eventName: String)
    fun getEventStatisticByName(eventName: String): Double
    fun getAllEventStatistic(): Map<String, Double>
    fun printStatistic()
}