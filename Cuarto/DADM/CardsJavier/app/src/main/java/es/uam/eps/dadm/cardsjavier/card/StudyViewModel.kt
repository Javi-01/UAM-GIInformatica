package es.uam.eps.dadm.cardsjavier.card

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import es.uam.eps.dadm.cardsjavier.SettingsActivity
import es.uam.eps.dadm.cardsjavier.database.CardDatabase
import java.time.*
import java.util.concurrent.Executors


class StudyViewModel(activity: Application): AndroidViewModel(activity) {
    private val executor = Executors.newSingleThreadExecutor()
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    var card: Card? = null
    var cards: LiveData<List<Card>> = CardDatabase.getInstance(context).cardDao.getCards()

    var dueCard: LiveData<Card?> = Transformations.map(cards) {
        it.filter { card -> card.isDue(LocalDateTime.now()) }.randomOrNull()
    }

    var nDUeCards: LiveData<Int> = Transformations.map(cards) {
        it.filter { card -> card.isDue(LocalDateTime.now()) }.size
    }
    fun update(quality: Int){
        card?.updateCard(quality)
        card?.answered = false

        executor.execute {
            CardDatabase.getInstance(context).cardDao.updateCard(card!!)
        }
    }
}