package center.kar

import org.slf4j.Logger
import org.slf4j.LoggerFactory


private class Anonymous

val Any?.logger: Logger
    get() = LoggerFactory.getLogger(this?.javaClass ?: Anonymous::class.java)