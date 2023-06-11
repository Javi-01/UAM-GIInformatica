package es.uam.eps.dadm.cardsjavier.card

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import es.uam.eps.dadm.cardsjavier.database.CardDatabase
import es.uam.eps.dadm.cardsjavier.deck.DeckWithCards
import java.time.*


class StatisticsViewModel(activity: Application): AndroidViewModel(activity) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val deckId = MutableLiveData<Long>()
    val deck: LiveData<DeckWithCards> = Transformations.switchMap(deckId) {
        CardDatabase.getInstance(context).cardDao.getDeckWithCards(it)
    }

    fun loadStatisticsDeckId(id: Long) {
        deckId.value = id
    }


    fun deckQuality(d: DeckWithCards?): List<Int>{
        val eas = mutableListOf<Int>()
        d?.cards?.forEach { eas.add(it.quality) }
        return eas
    }

    fun deckNextPractices(d: DeckWithCards?): List<Int>{
        val next = mutableListOf<Int>()
        d?.cards?.forEach { next.add(daysUntilDate(LocalDateTime.parse(it.nextPracticeDate)).toInt()) }
        return next
    }


    private fun daysUntilDate(date: LocalDateTime): Long {
        // Obtener la fecha y hora actual en la zona horaria del dispositivo
        val now = LocalDateTime.now(ZoneId.systemDefault())

        // Si la fecha ya ha pasado, retornar 0
        if (now.isAfter(date)) {
            return 0
        }

        // Obtener la fecha de hoy a las 12:00 AM
        val today = LocalDateTime.of(LocalDate.now(), LocalTime.MIN)

        // Obtener la fecha de pasadmo mañana
        val tomorrow = today.plusDays(2)

        // Obtener la fecha de la fecha límite a las 12:00 AM
        val deadline = date.toLocalDate().atStartOfDay()

        // Determinar si la fecha límite es mañana o después
        val isTomorrowOrLater = deadline.isAfter(tomorrow) || deadline == tomorrow

        // Si la fecha límite es mañana o después, calcular la cantidad de días
        // restantes incluyendo la fecha de hoy
        return if (isTomorrowOrLater) {
            val days = deadline.toLocalDate().toEpochDay() - today.toLocalDate().toEpochDay() + 1
            days
        } else {
            // Si la fecha límite es hoy, retornar 1
            1
        }
    }
}