package ui.commands

import MillisecondsTimeUnit
import TimerPropertiesBuilder
import alerts.AlertGenerators
import picocli.CommandLine.*
import seconds
import ui.ApplicationUsageError
import kotlin.time.ExperimentalTime

@Command(
    name = "default",
    description = [
        "Create and start a timer with default configuration.",
        "Alias to the next set of commands:",
        "  create -d=DURATION -i=1.s",
        "  task -c=AFTER_FINISH_EVERY -t=30.s -a=SAY_TIME",
        "  start"
    ],
    synopsisHeading = "%n%n",
    descriptionHeading = "%nDescription:%n",
    optionListHeading = "%nOptions:%n"
)
@ExperimentalTime
class DefaultTimerCommand : TimerStartSubCommand() {
    @ParentCommand
    override lateinit var applicationCommand: TimerApplicationCommand

    @Option(
        names = ["-d", "--duration"],
        required = true,
        description = [
            "Set timer duration. Format [NUMBER].[UNIT]. Supported units: ms, s, m, h, d."
        ]
    )
    private lateinit var duration: MillisecondsTimeUnit

    override fun run() {
        if (duration < 30.seconds) {
            throw ApplicationUsageError("Default timer duration can't be less than 30 seconds")
        }

        properties = TimerPropertiesBuilder(duration)
            .tickInterval(1.seconds)
            .remindAfterFinishEvery(30.seconds, AlertGenerators.getSayTimeAlertGenerator())

        timer = createAndStartNewTimer()
    }
}
