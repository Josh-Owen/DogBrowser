package josh.owen.dogbrowser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import josh.owen.dogbrowser.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.listOfBreedsFragment))
        NavigationUI.setupActionBarWithNavController(this, navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    //region Navigation
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    //endregion
}