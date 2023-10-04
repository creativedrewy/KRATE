package com.solanamobile.krate.kratedb

import app.cash.sqldelight.db.SqlDriver
import com.solanamobile.krate.kratedb.Database

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): Database {
    val driver = driverFactory.createDriver()
    val database = Database(driver)

    // Do more work with the database (see below).
}