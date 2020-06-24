package com.roda.paqueue.ui.queue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.roda.paqueue.R
import com.roda.paqueue.models.Queue
import io.realm.Realm
import java.util.*

class ListAdapter(context: Context?, onClickListener: OnClickListener) : RecyclerView.Adapter<ListAdapter.UserViewHolder>() {

    private val queueList: ArrayList<Queue> = ArrayList()
    private var listener: OnClickListener = onClickListener
    private var mContext: Context? = null

    init {
        mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.queue_row, parent, false)
        return UserViewHolder(mContext, view, listener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setQueue(queueList[position])

        holder.bind()

        val queue = queueList[position]
        if(queue.status == "ACTIVE") {
            mContext?.resources?.getColor(R.color.greenBg)?.let {
                holder.card.setBackgroundColor(
                    it
                )
            }
        }
    }

    override fun getItemCount() = queueList.size

    fun addQueues(queues: List<Queue>) {
        queueList.addAll(queues)
    }

    fun removeQueue(queue: Queue?) {
        if (queueList.size == 0) {
            return
        }
        queueList.remove(queue)
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                queue?.deleteFromRealm()
            }
        }
    }

    class UserViewHolder(context: Context?, itemView: View, private var onClickListener: OnClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        var playerOne: TextView = itemView.findViewById(R.id.playerOne)
        var playerTwo: TextView = itemView.findViewById(R.id.playerTwo)
        var playerThree: TextView = itemView.findViewById(R.id.playerThree)
        var playerFour: TextView = itemView.findViewById(R.id.playerFour)
        var ratingBarPlayerOne: RatingBar = itemView.findViewById(R.id.ratingBarPlayerOneLevel)
        var ratingBarPlayerTwo: RatingBar = itemView.findViewById(R.id.ratingBarPlayerTwoLevel)
        var ratingBarPlayerThree: RatingBar = itemView.findViewById(R.id.ratingBarPlayerThreeLevel)
        var ratingBarPlayerFour: RatingBar = itemView.findViewById(R.id.ratingBarPlayerFourLevel)
        var courtNumber: TextView = itemView.findViewById(R.id.courtNumber)
        var card: CardView = itemView.findViewById(R.id.cvQueueRow)
        private var mContext: Context? = context

        fun bind() {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            onClickListener.onItemClick(adapterPosition, v)
        }

        override fun onLongClick(v: View?): Boolean {
            onClickListener.onItemLongClick(adapterPosition, v)
            return true
        }

        fun setQueue(queue: Queue) {
            playerOne.text = queue.players[0]?.name
            playerTwo.text = queue.players[1]?.name
            playerThree.text = queue.players[2]?.name
            playerFour.text = queue.players[3]?.name
            ratingBarPlayerOne.rating = queue.players[0]?.level!!
            ratingBarPlayerTwo.rating = queue.players[1]?.level!!
            ratingBarPlayerThree.rating = queue.players[2]?.level!!
            ratingBarPlayerFour.rating = queue.players[3]?.level!!
            courtNumber.text = queue.court_number.toString()
        }
    }

    interface OnClickListener {
        fun onItemClick(position: Int, itemView: View?)
        fun onItemLongClick(position: Int, itemView: View?)
    }
}
