package com.anondo.taskmanager.views.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.anondo.taskmanager.R
import com.anondo.taskmanager.databinding.ActivityMainBinding
import com.anondo.taskmanager.views.fragment.AboutAppFragment
import com.anondo.taskmanager.views.fragment.HomeFragment
import com.anondo.taskmanager.views.fragment.PrivacyPolicyFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerNav()
    }

    fun drawerNav(){

        setSupportActionBar(binding.materialToolbar)

        binding.frameLayout.removeAllViews()
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frameLayout , HomeFragment())
        fragmentTransaction.commit()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.main,
            binding.materialToolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        binding.main.addDrawerListener(actionBarDrawerToggle)

        actionBarDrawerToggle.syncState()

        binding.navView.setNavigationItemSelectedListener {itemMenu ->

            if (itemMenu.itemId== R.id.home){

                binding.frameLayout.removeAllViews()
                val fragmentManager : FragmentManager = supportFragmentManager
                val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout , HomeFragment())
                fragmentTransaction.commit()


                Toast.makeText(applicationContext , "Home" , Toast.LENGTH_SHORT).show()
                binding.main.closeDrawers()

            }else if (itemMenu.itemId== R.id.privacyPolicy){

                binding.frameLayout.removeAllViews()
                val fragmentManager : FragmentManager = supportFragmentManager
                val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout , PrivacyPolicyFragment())
                fragmentTransaction.commit()

                Toast.makeText(applicationContext , "Privacy Policy" , Toast.LENGTH_SHORT).show()
                binding.main.closeDrawers()

            }else if (itemMenu.itemId== R.id.aboutApp){

                binding.frameLayout.removeAllViews()
                val fragmentManager : FragmentManager = supportFragmentManager
                val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout , AboutAppFragment())
                fragmentTransaction.commit()

                Toast.makeText(applicationContext , "About App" , Toast.LENGTH_SHORT).show()
                binding.main.closeDrawers()

            }
            return@setNavigationItemSelectedListener true
        }

    }

    override fun onBackPressed() {

        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)

        if (currentFragment is HomeFragment) {
            super.onBackPressed()
        }else{
            binding.frameLayout.removeAllViews()
            val fragmentManager : FragmentManager = supportFragmentManager
            val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.frameLayout , HomeFragment())
            fragmentTransaction.commit()
        }
    }

}