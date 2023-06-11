package es.uam.eps.dadm.cardsjavier

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import es.uam.eps.dadm.cardsjavier.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    companion object {
        private const val MAX_NUMBER_CARDS_KEY = "max_number_cards"
        private const val MAX_NUMBER_CARDS_DEFAULT = "20"
        private const val BOARD_VIEW_KEY = "board"
        private const val BOARD_VIEW_DEFAULT = false


        fun getMaximumNumberOfCards(context: Context): String? {
            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(MAX_NUMBER_CARDS_KEY, MAX_NUMBER_CARDS_DEFAULT)
        }

        fun setMaximumNumberOfCards(context: Context, maxCards: Int) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(MAX_NUMBER_CARDS_KEY, MAX_NUMBER_CARDS_DEFAULT)
            editor.apply()
        }

        fun getBoardView(context: Context): Boolean {
            return PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .getBoolean(BOARD_VIEW_KEY, BOARD_VIEW_DEFAULT)
        }

        fun setBoardView(context: Context, board: Boolean) {
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putBoolean(BOARD_VIEW_KEY, BOARD_VIEW_DEFAULT)
            editor.apply()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}