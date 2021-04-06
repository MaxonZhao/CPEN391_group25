package com.cpen391.flappyUI

class GameSettings private constructor(){
    private var controlMethod: Boolean = true
    private var birdColor: String = "ye"
    private var diffLevel: String = "e"
    fun getControlMethod() : Boolean{
        return controlMethod
    }
    fun setControlMethod(controlMethod: Boolean){
        this@GameSettings.controlMethod = controlMethod
    }

    fun getBirdColor() : String{
        return birdColor
    }
    fun setBirdColor(color: String){
        birdColor = color
    }

    fun getDiffLevel() : String{
        return diffLevel
    }
    fun setDiffLevel(diffLevel: String){
        this@GameSettings.diffLevel = diffLevel
    }

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