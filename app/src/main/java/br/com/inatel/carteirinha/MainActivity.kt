package br.com.inatel.carteirinha

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import br.com.inatel.carteirinha.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle

    private val navController: NavController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment
            ) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarNavegacao()
        configurarDrawer()
        aplicarWindowInsets()
    }

    private fun configurarNavegacao() {
        binding.navView.setupWithNavController(navController)
    }

    private fun aplicarWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(
                view.paddingLeft,
                statusBarInsets.top,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }
    }

    private fun configurarDrawer() {

        setSupportActionBar(binding.toolbar)

        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(drawerToggle)

        drawerToggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.drawer_inicio -> {
                    navegarPara(R.id.navigation_home)
                }

                R.id.drawer_grade -> {
                    navegarPara(R.id.navigation_grade)
                }

                R.id.drawer_carteirinha -> {
                    navegarPara(R.id.navigation_carteirinha)
                }

                R.id.drawer_validar_qr -> {
                    navegarPara(R.id.navigation_validacao_qr)
                }

                R.id.drawer_configuracoes -> {
                    // Futuramente.
                }

                R.id.drawer_sobre -> {
                    // Futuramente.
                }
            }

            binding.drawerLayout.closeDrawer(
                GravityCompat.START
            )

            true
        }
    }

    private fun navegarPara(destinationId: Int) {

        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setRestoreState(true)
            .setPopUpTo(
                navController.graph.startDestinationId,
                inclusive = false,
                saveState = true
            )
            .build()

        navController.navigate(destinationId, null, navOptions)
    }

    override fun onSupportNavigateUp(): Boolean {

        return if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {

            binding.drawerLayout.closeDrawer(
                GravityCompat.START
            )

            true

        } else {

            super.onSupportNavigateUp()
        }
    }
}