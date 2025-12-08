package com.anondo.taskmanager.views

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.anondo.taskmanager.views.AboutAppFragment
import com.anondo.taskmanager.views.HomeFragment
import com.anondo.taskmanager.views.PrivacyPolicyFragment
import com.anondo.taskmanager.R
import com.anondo.taskmanager.views.TaskAdapter
import com.anondo.taskmanager.db.TaskDao
import com.anondo.taskmanager.db.Task_Data_Class
import com.anondo.taskmanager.databinding.ActivityMainBinding
import com.anondo.taskmanager.reducecode.EncryptDecrypt

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

        val actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.main , binding.materialToolbar , R.string.drawer_open , R.string.drawer_close)
        binding.main.addDrawerListener(actionBarDrawerToggle)

        actionBarDrawerToggle.syncState()

        binding.navView.setNavigationItemSelectedListener {itemMenu ->

            if (itemMenu.itemId==R.id.home){

                binding.frameLayout.removeAllViews()
                val fragmentManager : FragmentManager = supportFragmentManager
                val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout , HomeFragment())
                fragmentTransaction.commit()


                Toast.makeText(applicationContext , "Home" , Toast.LENGTH_SHORT).show()
                binding.main.closeDrawers()

            }else if (itemMenu.itemId==R.id.privacyPolicy){

                binding.frameLayout.removeAllViews()
                val fragmentManager : FragmentManager = supportFragmentManager
                val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.frameLayout , PrivacyPolicyFragment())
                fragmentTransaction.commit()

                Toast.makeText(applicationContext , "Privacy Policy" , Toast.LENGTH_SHORT).show()
                binding.main.closeDrawers()

            }else if (itemMenu.itemId==R.id.aboutApp){

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