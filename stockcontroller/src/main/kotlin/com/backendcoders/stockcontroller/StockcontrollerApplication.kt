package com.backendcoders.stockcontroller

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class StockcontrollerApplication

fun main(args: Array<String>) {
	runApplication<StockcontrollerApplication>(*args)
}
