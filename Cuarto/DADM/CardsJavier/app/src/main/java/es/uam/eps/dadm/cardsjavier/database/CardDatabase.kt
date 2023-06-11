package es.uam.eps.dadm.cardsjavier.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.uam.eps.dadm.cardsjavier.card.Card
import es.uam.eps.dadm.cardsjavier.deck.Deck

@Database(entities = [Card::class, Deck::class], version = 1, exportSchema = false)
abstract class CardDatabase: RoomDatabase(){
    abstract val cardDao: CardDao

    companion object {
        @Volatile
        private var INSTANCE: CardDatabase? = null

        fun getInstance(context: Context): CardDatabase {
            var instance = INSTANCE

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    CardDatabase::class.java,
                    "cards_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
            }
            return instance
        }
    }
}