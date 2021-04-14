package com.cpen391.flappyUI

class GameSettings private constructor() {
    private var controlMethod: Boolean = true //tapping
    private var birdColor: String = String()
    private var diffLevel: String = String()
    private var gameSettingsToBlueTooth: String = String()
    fun getControlMethod(): Boolean {
        return controlMethod
    }

    fun setControlMethod(controlMethod: Boolean) {
        this@GameSettings.controlMethod = controlMethod
    }

    fun getBirdColor(): String {
        return birdColor
    }

    fun setBirdColor(color: String) {
        birdColor = color
    }

    fun getDiffLevel(): String {
        return diffLevel
    }

    fun setDiffLevel(diffLevel: String) {
        this@GameSettings.diffLevel = diffLevel
    }

//    fun setGameSettings(color: String, diffLevel: String, playMode: String){
//        val gameSettings = color + diffLevel + playMode
//        gameSettingsToBlueTooth = gameSettings
//    }
//    fun getGameSetting() : String{
//        return gameSettingsToBlueTooth
//    }

    companion object {
        @Volatile
        private var mInstance: GameSettings? = null

        @JvmStatic
        val instance: GameSettings?
            get() {
                if (mInstance == null) {
                    synchronized(GameSettings::class.java) {
                        if (mInstance == null) {
                            mInstance = GameSettings()
                        }
                    }
                }
                return mInstance
            }
    }

}