package es.uam.eps.dadm.cardsjavier.deck

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import es.uam.eps.dadm.cardsjavier.database.CardDatabase

class DeckListViewModel(application: Application): AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val userId = MutableLiveData<String>()
    val decks: LiveData<List<DeckWithCards>> = Transformations.switchMap(userId) {
        CardDatabase.getInstance(context).cardDao.getDecksWithCards(it)
    }

    fun loadUserId(id: String) {
        userId.value = id
    }
}