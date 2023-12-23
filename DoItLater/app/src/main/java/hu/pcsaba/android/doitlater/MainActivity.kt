package hu.pcsaba.android.doitlater

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation.findNavController
import hu.pcsaba.android.doitlater.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
	lateinit var binding: ActivityMainBinding
	lateinit var toggle: ActionBarDrawerToggle
	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.apply {
			toggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout, R.string.open, R.string.close)
			drawerLayout.addDrawerListener(toggle)
			toggle.syncState()
			supportActionBar?.setDisplayHomeAsUpEnabled(true)
			navView.setNavigationItemSelectedListener {
				when(it.itemId) {
					R.id.menu_list -> {
						val navController = findNavController(this@MainActivity, R.id.nav_host_fragment)
						navController.navigate(R.id.listFragment);
					}
					R.id.menu_deleted -> {
						val navController = findNavController(this@MainActivity, R.id.nav_host_fragment)
						navController.navigate(R.id.listDeletedFragment);
					}
				}
				drawerLayout.closeDrawer(GravityCompat.START);
				true
			}
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if(toggle.onOptionsItemSelected(item))
			return true
		return super.onOptionsItemSelected(item)
	}
}