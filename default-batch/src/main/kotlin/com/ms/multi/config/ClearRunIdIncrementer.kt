package com.ms.multi.config

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.JobParametersIncrementer
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ClearRunIdIncrementer : JobParametersIncrementer {
    override fun getNext(parameters: JobParameters?): JobParameters {
        val builder = parameters?.let { JobParametersBuilder(it) } ?: JobParametersBuilder()
        val id = FORMAT.format(ZonedDateTime.now())

        return builder.addString(RUN_ID_KEY, id).toJobParameters()
    }

    companion object {
        private const val RUN_ID_KEY = "run.id"
        private val FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS").withZone(ZoneId.of("Asia/Seoul"))
    }
}
