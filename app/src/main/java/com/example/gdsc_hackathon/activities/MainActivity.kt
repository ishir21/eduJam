package com.example.gdsc_hackathon.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.gdsc_hackathon.R
import com.example.gdsc_hackathon.dataModel.Prefs
import com.example.gdsc_hackathon.utils.hideSoftKeyboard
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.navigation.NavigationView as NavigationView

// TODO: hide keyboard when clicked on bottom navigation items
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottom_navigation)


        navController = findNavController(R.id.hostFragment)
        setupBottomNavigation()

        drawerLayout = findViewById(R.id.drawer_layout)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        navigationView = findViewById(R.id.navigation_view)


        //NavigationUI.setupWithNavController(navigation_view,navController)
        NavigationUI.setupWithNavController(navigationView, navController)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notification -> {
                navController.navigate(R.id.notificationFragment)
                return true
            }

            R.id.user_logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you want to logout?").setPositiveButton(
                    "Yes"
                ) { dialogInterface: DialogInterface?, i: Int ->
                    FirebaseAuth.getInstance().signOut()
                    val prefs = Prefs(this)
                    prefs.status = 0
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                    .setNegativeButton(
                        "No"
                    ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.cancel() }
                val alertDialog = builder.create()
                alertDialog.show()
                return true
            }


        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupBottomNavigation() {
        hideSoftKeyboard(this)
        bottomNav.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        //return navController.navigateUp()
        hideSoftKeyboard(this)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    fun refreshCurrentFragment(){
        val id = navController.currentDestination?.id
        navController.popBackStack(id!!,true)
        navController.navigate(id)
    }
}