package org.julianvelandia.raya

import app.cash.sqldelight.db.SqlDriver
import db.WalletDatabase

fun createDatabase(driverFactory: DatabaseDriverFactory): WalletDatabase {
    val driver: SqlDriver = driverFactory.createDriver()
    val database = WalletDatabase(driver)
    return database
}