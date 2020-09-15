package com.roda.paqueue.ui.settings

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.roda.paqueue.R
import com.roda.paqueue.models.Court
import com.roda.paqueue.models.Player
import com.roda.paqueue.ui.queue.QueueManager
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        settingsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    // enable options menu in this fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle menu item clicks
        when (item.itemId) {
            R.id.reset_games -> {
                Realm.getDefaultInstance().use { realm ->
                    val players = realm.where<Player>().findAll()
                    players.forEach { player ->
                        realm.executeTransaction {
                            player.num_games = 0
                            player.queue_count = 0
                            player.queues_games = 0.0f
                        }
                    }
                }
                Toast.makeText(this.context, "Games cleared", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}