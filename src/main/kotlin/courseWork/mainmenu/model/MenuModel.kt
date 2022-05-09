package courseWork.mainmenu.model

import courseWork.gamemenu.model.ModelChangeListener

interface ModelChangeListener {
    fun onModelChanged()
}

class MenuModel {
    private val listeners: MutableSet<ModelChangeListener> = mutableSetOf()

    fun addModelChangeListener(listener: ModelChangeListener) {
        listeners.add(listener)
    }

    fun removeModelChangeListener(listener: ModelChangeListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.onModelChanged() }
    }


}