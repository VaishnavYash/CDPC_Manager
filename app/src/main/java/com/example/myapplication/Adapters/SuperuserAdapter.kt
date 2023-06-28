package com.example.myapplication.Adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.LoginSQL
import com.example.myapplication.R
import com.example.myapplication.SuperUsersObjects

class SuperuserAdapter(
    private val context: Context,
    private var dataSet: MutableList<SuperUsersObjects>
    ):     RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.super_view, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val companyName: TextView = view.findViewById(R.id.company_name)
        val role: TextView = view.findViewById(R.id.role)
        val spocName: TextView = view.findViewById(R.id.spoc_name)
        val delBtn : ImageButton = view.findViewById(R.id.del_btn)
        val infoBtn: ImageButton = view.findViewById(R.id.info_btn)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentPos  = dataSet[position]
        val viewHolder = holder as? ViewHolder

        viewHolder!!.companyName.text = currentPos.cName
        viewHolder.role.text = currentPos.cRole
        viewHolder.spocName.text = currentPos.sName

        holder.delBtn.setOnClickListener {
            removeItem(position)
        }

        holder.infoBtn.setOnClickListener{
            val context = holder.itemView.context
            showPopup(position, context)
        }

    }

    private fun removeItem(position: Int) {
        val deletedItem = dataSet[position] // Get the item to be deleted

        // Delete the item from the database
        val cmpName : String = dataSet[position].cName.toString()
        val hrName : String = dataSet[position].hrName.toString()
        val hrEmail : String = dataSet[position].hrEmail.toString()
        val spocInfo : String = dataSet[position].sName.toString()
        val roleInfo : String = dataSet[position].cRole.toString()

        val db = LoginSQL(context) // Replace with your database class
        db.deleteEntry(spocInfo,cmpName, hrEmail, hrName, roleInfo) // Assuming you have a method deleteEntry() to delete the item from the database by its unique identifier

        // Remove the item from the dataset
        dataSet.removeAt(position)

        // Notify the adapter of the item removal
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataSet.size)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun showPopup(position: Int, context: Context) {

        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.info_popup, null)
        val popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)


        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        val cmpName : TextView = popupView.findViewById(R.id.cmp_name)
        val hrName : TextView = popupView.findViewById(R.id.hr_name)
        val hrEmail : TextView = popupView.findViewById(R.id.hr_email)
        val spocInfo : TextView = popupView.findViewById(R.id.spoc_name_popups)
        val roleInfo : TextView = popupView.findViewById(R.id.role_offer)

        cmpName.text = dataSet[position].cName.toString()
        hrName.text = dataSet[position].hrName.toString()
        hrEmail.text = dataSet[position].hrEmail.toString()
        spocInfo.text = dataSet[position].sName.toString()
        roleInfo.text = dataSet[position].cRole.toString()

    }



}