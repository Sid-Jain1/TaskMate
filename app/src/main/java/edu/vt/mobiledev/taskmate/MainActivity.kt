package edu.vt.mobiledev.taskmate
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.vt.mobiledev.taskmate.databinding.ActivityMainBinding
//Siddharth Jain
//PID: siddharthjain
class MainActivity : AppCompatActivity() {
    //Siddharth Jain
    //PID: siddharthjain
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}