package com.ojasx.whotouchedmyphone.RoomDb.PIN

class PinRepository(private val dao: PinDao) {


    suspend fun setPin(pin: String) {
        dao.savePin(
            PinEntity(
                id = 0,
                pinHash = hashPin(pin)
            )
        )
    }

    suspend fun verifyPin(inputPin: String): Boolean {
        val stored = dao.getPin() ?: return false
        return stored.pinHash == hashPin(inputPin)
    }

    suspend fun isPinSet(): Boolean {
        return dao.getPin() != null
    }

    suspend fun updatePin(newPin: String) {
        dao.savePin(
            PinEntity(
                id = 0,
                pinHash = hashPin(newPin)
            )
        )
    }
}