package com.cpen391.flappyUI.util
/**
 *  GameSettings
 *
 *  @note: Util file, provide game settings method to activities
 *
 *  @author Robin Lai
 */
class GameSettingsUtil private constructor() {
    private var controlMethod: Boolean = true //tapping
    private var birdColor: String = String()
    private var diffLevel: String = String()
    private var gameSettingsToBlueTooth: String = String()
    fun getControlMethod(): Boolean {
        return controlMethod
    }

    fun setControlMethod(controlMethod: Boolean) {
        this@GameSettingsUtil.controlMethod = controlMethod
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
        this@GameSettingsUtil.diffLevel = diffLevel
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
        private var mInstance: GameSettingsUtil? = null

        @JvmStatic
        val instance: GameSettingsUtil?
            get() {
                if (mInstance == null) {
                    synchronized(GameSettingsUtil::class.java) {
                        if (mInstance == null) {
                            mInstance = GameSettingsUtil()
                        }
                    }
                }
                return mInstance
            }
    }

}