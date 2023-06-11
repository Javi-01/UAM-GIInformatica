package es.uam.eps.dadm.cardsjavier

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import es.uam.eps.dadm.cardsjavier.database.SynchronizationManager
import es.uam.eps.dadm.cardsjavier.databinding.ActivityTitleBinding


class TitleActivity : AppCompatActivity(){

    private lateinit var binding: ActivityTitleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTitleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        binding.navView.setupWithNavController(navHostFragment.navController)

        // Shared Preferences la primera vez (false)
        PreferenceManager.setDefaultValues(
            this, R.xml.root_preferences, true)

        setupMenu()
    }

    private fun setupMenu() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_card_list, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.settings -> {
                        startActivity(Intent(this@TitleActivity, SettingsActivity::class.java))
                    }
                    R.id.log_out -> {
                        val instance = FirebaseAuth.getInstance()
                        if (instance.currentUser != null) {
                            instance.signOut()
                            Toast.makeText(this@TitleActivity, "Sesión cerrada", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@TitleActivity, EmailPasswordActivity::class.java))
                        }
                    }
                    R.id.upload -> {
                        SynchronizationManager(this@TitleActivity).roomToFirebaseSynchronization()
                        Toast.makeText(this@TitleActivity, "Sincronzacion exitosa", Toast.LENGTH_LONG).show()
                    }
                    R.id.download -> {
                        SynchronizationManager(this@TitleActivity).fireBaseToRoomSynchronization()
                        Toast.makeText(this@TitleActivity, "Sincronzacion exitosa", Toast.LENGTH_LONG).show()
                    }
                }
                return true
            }
        })
    }
}